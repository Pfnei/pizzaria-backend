package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.user.*;
import at.incrustwetrust.pizzeria.entity.Product;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.exception.UnauthorizedActionException;
import at.incrustwetrust.pizzeria.exception.UserAlreadyExistsException;
import at.incrustwetrust.pizzeria.exception.UserNotFoundException;
import at.incrustwetrust.pizzeria.mapper.UserMapper;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;


import java.util.List;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper mapper;
	private final FileStorageService fileService;
	
	// CREATE
	
	public UserResponseDTO create(UserCreateDTO dto, SecurityUser principal) {
		throwIfUsernameOrEmailExists(dto);
		
		User creator = userRepository.findById(principal.getId())
				.orElseThrow(() -> new UserNotFoundException("Creator not found: " + principal.getId()));
		
		// 1. Mapper creates the base entity (createdBy remains empty/ignore here)
		User user = mapper.toEntity(dto, null);
		
		// 2. Set password
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		// 3. Manually set the creator as object
		user.setCreatedBy(creator);
		
		User saved = userRepository.save(user);
		return mapper.toResponseDto(saved);
	}
	
	
	// READ
	
	public UserResponseDTO read(String id, SecurityUser principal) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("No user found with ID: " + id));
		return mapper.toResponseDto(user);
	}
	
	public List<UserResponseDTO> readAll(Optional<String> createdBy, SecurityUser principal) {
		loggedInUserCheck(principal);
		
		List<User> user = null;
		
		
		if (principal.isAdmin()) if (createdBy.isPresent()) {
			user = userRepository.findAllByCreatedBy_UserId(createdBy.get());
		} else {
			user = userRepository.findAll();
		}
		
		return mapper.toResponseDtoList(userRepository.findAll());
	}
	
	// READ ME
	
	
	// UPDATE
	
	/**
	 * Updates user if authorized; enforces admin/self password change
	 */
	@Transactional
	public UserResponseDTO update(UserUpdateDTO dto, String id, SecurityUser principal) {
		User existingUser = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("No user found with ID: " + id));
		
		throwIfUsernameOrEmailExists(dto, id);
		
		boolean isAdmin = principal.isAdmin();
		boolean isSelf = principal.getId().equals(id);
		
		if (!isAdmin && !isSelf) {
			throw new UnauthorizedActionException("You are not allowed to update this user.");
		}
		
		boolean oldAdmin = existingUser.isAdmin();
		boolean oldActive = existingUser.isActive();
		
		mapper.updateEntity(dto, existingUser);
		
		if (!isAdmin) {
			existingUser.setAdmin(oldAdmin);
			existingUser.setActive(oldActive);
		}
		
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		
		User currentUser = userRepository.findById(principal.getId())
				.orElseThrow(() -> new UserNotFoundException("Current user not found"));
		existingUser.setLastUpdatedBy(currentUser);
		
		return mapper.toResponseDto(userRepository.save(existingUser));
	}
	
	@Transactional
	public UserResponseDTO delete(String id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("No user found with ID: " + id));
		
		// If a profile picture exists, we delete it in both cases
		if (user.getProfilePicture() != null) {
			fileService.deleteProfileImage(user.getProfilePicture());
			user.setProfilePicture(null);
		}
		
		// Check: Has the user already placed orders?
		boolean hasOrders = user.getOrders() != null && !user.getOrders().isEmpty();
		
		if (hasOrders) {
			// SOFT DELETE: Since database constraints would prevent deletion
			user.setActive(false);
			user.setAdmin(false);
			return mapper.toResponseDto(userRepository.save(user));
		} else {
			// HARD DELETE: User can be safely deleted
			userRepository.delete(user);
			return mapper.toResponseDto(user);
		}
	}
	
	
	// DUPLICATE CHECKS
	
	private void throwIfUsernameOrEmailExists(UserCreateDTO dto) {
		userRepository.findUserByEmail(dto.getEmail()).ifPresent(u -> {
			throw new UserAlreadyExistsException("A user with this email already exists.");
		});
		userRepository.findUserByUsername(dto.getUsername()).ifPresent(u -> {
			throw new UserAlreadyExistsException("A user with this username already exists.");
		});
	}
	
	private void throwIfUsernameOrEmailExists(UserUpdateDTO dto, String userId) {
		userRepository.findByEmailAndUserIdNot(dto.getEmail(), userId).ifPresent(u -> {
			throw new UserAlreadyExistsException("Another user with this email already exists.");
		});
		userRepository.findUserByUsernameAndUserIdNot(dto.getUsername(), userId).ifPresent(u -> {
			throw new UserAlreadyExistsException("Another user with this username already exists.");
		});
	}
	
	public Optional<User> findEntityById(String userId) {
		// Uses the UserRepository to find the User entity
		return userRepository.findById(userId);
	}
	
	private void loggedInUserCheck(SecurityUser principal) {
		if (principal == null) {
			throw new UnauthorizedActionException("your not logged in");
		}
		
		
	}
}
