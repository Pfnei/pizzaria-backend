package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.order.*;
import at.incrustwetrust.pizzeria.entity.Order;
import at.incrustwetrust.pizzeria.entity.OrderItem;
import at.incrustwetrust.pizzeria.entity.Product;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.exception.OrderNotFoundException;
import at.incrustwetrust.pizzeria.exception.ResourceNotFoundException;
import at.incrustwetrust.pizzeria.exception.UnauthorizedActionException;
import at.incrustwetrust.pizzeria.mapper.OrderMapper;
import at.incrustwetrust.pizzeria.repository.OrderRepository;
import at.incrustwetrust.pizzeria.repository.ProductRepository;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    OrderMapper orderMapper;

    @InjectMocks
    OrderService orderService;

    private SecurityUser principal(String id, boolean admin) {
        User u = new User();
        u.setUserId(id);
        u.setAdmin(admin);
        u.setActive(true);
        u.setEmail("u@x");
        u.setUsername("user");
        u.setPassword("pw");
        return new SecurityUser(u);
    }

    @Test
    void readAll_admin_noFilter_returnsAll() {
        SecurityUser admin = principal("a1", true);
        List<Order> orders = List.of(new Order(), new Order());
        when(orderRepository.findAll()).thenReturn(orders);
        List<OrderResponseDTO> mapped = List.of(new OrderResponseDTO(), new OrderResponseDTO());
        when(orderMapper.toResponseDtoList(orders)).thenReturn(mapped);
        List<OrderResponseDTO> res = orderService.readAll(Optional.empty(), Optional.empty(), admin);
        assertThat(res).isSameAs(mapped);
        verify(orderRepository).findAll();
    }

    @Test
    void readAll_admin_withFilter_usesFilter() {
        SecurityUser admin = principal("a1", true);
        List<Order> orders = List.of(new Order());
        when(orderRepository.findAllByCreatedBy_UserId("u1")).thenReturn(orders);
        when(orderMapper.toResponseDtoList(orders)).thenReturn(List.of(new OrderResponseDTO()));
        List<OrderResponseDTO> res = orderService.readAll(Optional.of("u1"), Optional.empty(),admin);
        assertThat(res).hasSize(1);
        verify(orderRepository).findAllByCreatedBy_UserId("u1");
    }

    @Test
    void readAll_user_ignoresFilter_returnsOwn() {
        SecurityUser user = principal("u1", false);
        List<Order> userOrders = List.of(new Order());
        when(orderRepository.findAllByCreatedBy_UserId("u1")).thenReturn(userOrders);
        when(orderMapper.toResponseDtoList(userOrders)).thenReturn(List.of(new OrderResponseDTO()));
        List<OrderResponseDTO> res = orderService.readAll(Optional.of("other"), Optional.empty(), user);
        assertThat(res).hasSize(1);
        verify(orderRepository).findAllByCreatedBy_UserId("u1");
    }

    @Test
    void readAll_throws_whenNotLoggedIn() {
        assertThatThrownBy(() -> orderService.readAll(Optional.empty(), Optional.empty(),null))
                .isInstanceOf(UnauthorizedActionException.class);
    }

    @Test
    void read_admin_canReadAny() {
        SecurityUser admin = principal("a1", true);
        Order order = new Order();
        when(orderRepository.findById("oid")).thenReturn(Optional.of(order));
        OrderResponseDTO dto = new OrderResponseDTO();
        when(orderMapper.toResponseDto(order)).thenReturn(dto);
        OrderResponseDTO res = orderService.read("oid", admin);
        assertThat(res).isSameAs(dto);
    }

    @Test
    void read_user_canReadOwn_elseNotFound() {
        User owner = new User(); owner.setUserId("u1");
        Order order = new Order(); order.setCreatedBy(owner);
        when(orderRepository.findById("oid")).thenReturn(Optional.of(order));

        // own
        OrderResponseDTO dto = new OrderResponseDTO();
        when(orderMapper.toResponseDto(order)).thenReturn(dto);
        OrderResponseDTO res = orderService.read("oid", principal("u1", false));
        assertThat(res).isSameAs(dto);

        // foreign
        assertThatThrownBy(() -> orderService.read("oid", principal("other", false)))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void create_withPrincipal_buildsItems_andCalculatesTotal() {
        SecurityUser principal = principal("u1", false);
        User creator = new User(); creator.setUserId("u1");
        when(userRepository.findById("u1")).thenReturn(Optional.of(creator));

        OrderCreateDTO dto = new OrderCreateDTO();
        OrderItemCreateDTO i1 = OrderItemCreateDTO.builder().productId("p1").quantity(2).build();
        OrderItemCreateDTO i2 = OrderItemCreateDTO.builder().productId("p2").quantity(3).build();
        dto.setItems(List.of(i1, i2));

        Order orderEntity = new Order();
        when(orderMapper.toEntity(eq(dto), eq(creator))).thenReturn(orderEntity);

        Product p1 = new Product(); p1.setProductId("p1"); p1.setPrice(10.0);
        Product p2 = new Product(); p2.setProductId("p2"); p2.setPrice(2.5);
        when(productRepository.findById("p1")).thenReturn(Optional.of(p1));
        when(productRepository.findById("p2")).thenReturn(Optional.of(p2));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        when(orderRepository.save(orderCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

        OrderResponseDTO mapped = new OrderResponseDTO();
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(mapped);

        OrderResponseDTO res = orderService.create(dto, principal);

        Order saved = orderCaptor.getValue();
        assertThat(saved.getItems()).hasSize(2);
        double expectedTotal = 2 * 10.0 + 3 * 2.5;
        assertThat(saved.getTotal()).isEqualTo(expectedTotal);
        assertThat(res).isSameAs(mapped);
    }

    @Test
    void create_throws_whenProductMissing() {
        SecurityUser principal = principal("u1", false);
        User creator = new User(); creator.setUserId("u1");
        when(userRepository.findById("u1")).thenReturn(Optional.of(creator));

        OrderCreateDTO dto = new OrderCreateDTO();
        dto.setItems(List.of(OrderItemCreateDTO.builder().productId("missing").quantity(1).build()));

        when(orderMapper.toEntity(eq(dto), eq(creator))).thenReturn(new Order());
        when(productRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(dto, principal))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_adminOrOwner_allowed_elseNotFound() {
        User owner = new User(); owner.setUserId("u1");
        Order order = new Order(); order.setCreatedBy(owner);
        when(orderRepository.findById("oid")).thenReturn(Optional.of(order));

        // owner
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toResponseDto(order)).thenReturn(new OrderResponseDTO());
        OrderUpdateDTO dto = new OrderUpdateDTO();
        orderService.update("oid", dto, principal("u1", false));
        verify(orderRepository, times(1)).save(order);

        // admin
        orderService.update("oid", dto, principal("admin", true));
        verify(orderRepository, times(2)).save(order);

        // foreign -> not found
        assertThatThrownBy(() -> orderService.update("oid", dto, principal("x", false)))
                .isInstanceOf(OrderNotFoundException.class);
    }
}
