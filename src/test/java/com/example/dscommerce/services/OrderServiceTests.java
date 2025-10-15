package com.example.dscommerce.services;

import com.example.dscommerce.dto.OrderDTO;
import com.example.dscommerce.entities.Order;
import com.example.dscommerce.entities.OrderItem;
import com.example.dscommerce.entities.Product;
import com.example.dscommerce.entities.User;
import com.example.dscommerce.repositories.OrderItemRepository;
import com.example.dscommerce.repositories.OrderRepository;
import com.example.dscommerce.repositories.ProductRepository;
import com.example.dscommerce.services.exceptions.ForbiddenException;
import com.example.dscommerce.services.exceptions.ResourceNotFoundException;
import com.example.dscommerce.tests.OrderFactory;
import com.example.dscommerce.tests.ProductFactory;
import com.example.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AuthService authService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserService userService;

    private Long existingOrderId;
    private Long notExistingOrderId;
    private Long existingProductId;
    private Long notExistingProductId;
    private Order order;
    private OrderDTO dto;
    private User admin;
    private User client;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        existingOrderId = 1L;
        notExistingOrderId = 2L;

        existingProductId = 1L;
        notExistingProductId = 2L;

        admin = UserFactory.createCustomAdminUser(1L, "Jeff");
        client = UserFactory.createCustomClientUser(2L, "Bob");
        order = OrderFactory.createOrder(client);
        dto = new OrderDTO(order);
        product = ProductFactory.createProduct();

        when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));
        when(orderRepository.findById(notExistingOrderId)).thenReturn(Optional.empty());

        when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
        when(productRepository.getReferenceById(notExistingProductId)).thenThrow(ResourceNotFoundException.class);

        when(orderRepository.save(any())).thenReturn(order);
        when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminIsLogged() {
        doNothing().when(authService).validateSelfOrAdmin(any());
        OrderDTO result = orderService.findById(existingOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingOrderId, result.getId());
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndClientIsLogged() {
        doNothing().when(authService).validateSelfOrAdmin(any());
        OrderDTO result = orderService.findById(existingOrderId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingOrderId, result.getId());
    }

    @Test
    public void findByIdShouldThrowForbiddenExceptionWhenIdExistsAndOtherClientIsLogged() {
        doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());

        Assertions.assertThrows(ForbiddenException.class, () -> {
            OrderDTO result = orderService.findById(existingOrderId);
        });
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        doNothing().when(authService).validateSelfOrAdmin(any());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            OrderDTO result = orderService.findById(notExistingOrderId);
        });
    }

    @Test
    public void insertShouldReturnOrderDTOWhenAdminIsLogged() {
        when(userService.authenticated()).thenReturn(admin);
        OrderDTO result = orderService.insert(dto);

        Assertions.assertNotNull(result);
    }

    @Test
    public void insertShouldReturnOrderDTOWhenClientIsLogged() {
        when(userService.authenticated()).thenReturn(client);
        OrderDTO result = orderService.insert(dto);

        Assertions.assertNotNull(result);
    }
    
    @Test
    public void insertShouldThrowUsernameNotFoundExceptionWhenUserIsNotLogged() {
        doThrow(UsernameNotFoundException.class).when(userService).authenticated();

        order.setClient(new User());
        dto = new OrderDTO(order);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            OrderDTO result = orderService.insert(dto);
        });
    }

    @Test
    public void insertShouldThrowResourceNotFoundExceptionWhenOrderProductIdDoesNotExist() {
        when(userService.authenticated()).thenReturn(client);

        product.setId(notExistingProductId);
        OrderItem orderItem = new OrderItem(order, product, 2, 10.00);
        order.getItems().add(orderItem);

        dto = new OrderDTO(order);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            OrderDTO result = orderService.insert(dto);
        });
    }
}
