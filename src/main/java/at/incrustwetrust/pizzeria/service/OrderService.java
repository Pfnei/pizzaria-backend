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
//Order Mapper Controller Service and Tests need to be refactored for something that here
// current main, test happen of course in another branch...
	
	// READ ONE
	public OrderResponseDTO read(String orderId, SecurityUser principal) {
		if (principal == null) {
			throw new UnauthorizedActionException("your not logged in");
		}
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("No order found with ID " + orderId));
		
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
					.orElseThrow(() -> new UserNotFoundException("User not found"));
		}
		
		Order orderToSave = orderMapper.toEntity(dto, createdBy);
		List<OrderItem> items = buildOrderItems(dto.getItems(), orderToSave);
		orderToSave.setCreatedBy(createdBy);
		orderToSave.setItems(items);
		orderToSave.setTotal(calculateTotal(items));
		Order savedOrder = orderRepository.save(orderToSave);
		return orderMapper.toResponseDto(savedOrder);
	}
	
	// update still needs to be done, create, read, readalll should fit !
	public OrderResponseDTO update(String id, OrderUpdateDTO dto, SecurityUser principal) {
		// Check if logged in
		loggedInUserCheck(principal);
		
		// Find order
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("No order found with ID " + id));
		
		// Check permission (Admin or Owner)
		if (!principal.isAdmin() && !principal.getId().equals(order.getCreatedBy().getUserId())) {
			throw new OrderNotFoundException("Order not found"); // Disguise as 'not found'
		}
		
		//Map and save
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
							.orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemDto.getProductId()));
					double lineTotal = product.getPrice() * itemDto.getQuantity();
					return new OrderItem(order, product, product.getProductName(), itemDto.getQuantity(), lineTotal);
				})
				.toList();
	}
	
	private double calculateTotal(List<OrderItem> items) {
		return items.stream().mapToDouble(OrderItem::getPrice).sum();
	}
}
