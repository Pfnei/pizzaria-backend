package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.order.*;
import at.incrustwetrust.pizzeria.entity.Order;
import at.incrustwetrust.pizzeria.entity.OrderItem;
import at.incrustwetrust.pizzeria.entity.Product;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.exception.OrderNotFoundException;
import at.incrustwetrust.pizzeria.exception.ResourceNotFoundException;
import at.incrustwetrust.pizzeria.exception.UnauthorizedActionException;
import at.incrustwetrust.pizzeria.exception.UserNotFoundException;
import at.incrustwetrust.pizzeria.mapper.OrderMapper;
import at.incrustwetrust.pizzeria.repository.OrderRepository;
import at.incrustwetrust.pizzeria.repository.ProductRepository;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final OrderMapper orderMapper;
	
	

	public List<OrderResponseDTO> readMyOrders(SecurityUser principal) {
		loggedInUserCheck(principal);
		
		List<Order> orders;
		
		String userId = principal.getId();
		orders = orderRepository.findAllByCreatedBy_UserId(userId);
		
		return orderMapper.toResponseDtoList(orders);
		
	}
	
	
	// READ ALL (optional filter)
	public List<OrderResponseDTO> readAll(Optional<String> createdBy, Optional<String> productId, SecurityUser principal) {
		loggedInUserCheck(principal);
		
		
		List<Order> orders;
		
		if (principal.isAdmin()) {
			if (createdBy.isPresent()) {
				orders = orderRepository.findAllByCreatedBy_UserId(createdBy.get());
			} else if (productId.isPresent()) {
				orders = orderRepository.findDistinctByItems_Product_ProductId(productId.get());
			} else {
				orders = orderRepository.findAll();
			}
		} else {
			String userId = principal.getId();
			orders = orderRepository.findAllByCreatedBy_UserId(userId);
		}
		
		return orderMapper.toResponseDtoList(orders);
		
	}
//Order Mapper Controller Service und Tests müssen refactored werden für etwas , das hier
// aktueller main, test geschen natürlcih in andere branch...
	
	// READ ONE
	public OrderResponseDTO read(String orderId, SecurityUser principal) {
		if (principal == null) {
			throw new UnauthorizedActionException("your not logged in");
		}
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Keine Bestellung mit der ID " + orderId + " vorhanden"));
		
		if (principal.isAdmin()) {
			return orderMapper.toResponseDto(order);
		} else if (!principal.isAdmin()) {
			if (!principal.getId().equals(order.getCreatedBy().getUserId())) {
				throw new OrderNotFoundException("order not found");
			}
			return orderMapper.toResponseDto(order);
		}
		return null;
	}
	
	// CREATE
	public OrderResponseDTO create(OrderCreateDTO dto, SecurityUser principal) {
		
		User createdBy = null;
		if (principal != null) {
			createdBy = userRepository.findById(principal.getId())
					.orElseThrow(() -> new UserNotFoundException("User nicht gefunden"));
		}
		
		Order orderToSave = orderMapper.toEntity(dto, createdBy);
		List<OrderItem> items = buildOrderItems(dto.getItems(), orderToSave);
		orderToSave.setCreatedBy(createdBy);
		orderToSave.setItems(items);
		orderToSave.setTotal(calculateTotal(items));
		Order savedOrder = orderRepository.save(orderToSave);
		return orderMapper.toResponseDto(savedOrder);
	}
	
	// update muss noch gemacht werden, create, read, readalll sollten passen !
	public OrderResponseDTO update(String id, OrderUpdateDTO dto, SecurityUser principal) {
		// Prüfen ob eingeloggt
		loggedInUserCheck(principal);
		
		// Order suchen
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Keine Bestellung mit der ID " + id + " vorhanden"));
		
		// Berechtigung prüfen (Admin oder Besitzer)
		if (!principal.isAdmin() && !principal.getId().equals(order.getCreatedBy().getUserId())) {
			throw new OrderNotFoundException("Bestellung nicht gefunden"); // Tarnung als 'nicht gefunden'
		}
		
		//Mappen und speichern
		orderMapper.updateEntity(dto, order);
		Order saved = orderRepository.save(order);
		return orderMapper.toResponseDto(saved);
	}
	
	private void loggedInUserCheck(SecurityUser principal) {
		if (principal == null) {
			throw new UnauthorizedActionException("your not logged in");
		}
	}
	
	private List<OrderItem> buildOrderItems(List<OrderItemCreateDTO> itemDtos, Order order) {
		return itemDtos.stream()
				.map(itemDto -> {
					Product product = productRepository.findById(itemDto.getProductId())
							.orElseThrow(() -> new ResourceNotFoundException("Produkt nicht gefunden: " + itemDto.getProductId()));
					double lineTotal = product.getPrice() * itemDto.getQuantity();
					return new OrderItem(order, product, product.getProductName(), itemDto.getQuantity(), lineTotal);
				})
				.toList();
	}
	
	private double calculateTotal(List<OrderItem> items) {
		return items.stream().mapToDouble(OrderItem::getPrice).sum();
	}
}
