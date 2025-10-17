package com.example.dscommerce.controllers.it;

import com.example.dscommerce.dto.OrderDTO;
import com.example.dscommerce.dto.ProductDTO;
import com.example.dscommerce.entities.*;
import com.example.dscommerce.entities.enums.OrderStatus;
import com.example.dscommerce.repositories.OrderRepository;
import com.example.dscommerce.services.OrderService;
import com.example.dscommerce.tests.OrderFactory;
import com.example.dscommerce.tests.ProductFactory;
import com.example.dscommerce.tests.UserFactory;
import com.example.dscommerce.tests.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private String adminUsername;
    private String adminPassword;
    private String adminToken;
    private String clientUsername;
    private String clientPassword;
    private String clientToken;
    private String invalidToken;
    private Long existingOrderId;
    private Long notExistingOrderId;
    private Order order;
    private OrderDTO dto;
    private User user;
    private Product product;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() throws Exception {
        existingOrderId = 1L;
        notExistingOrderId = 100L;

        adminUsername = "alex@gmail.com";
        adminPassword = "123456";
        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        clientUsername = "maria@gmail.com";
        clientPassword = "123456";
        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        invalidToken = adminToken + "xpto";

        user = UserFactory.createClientUser();
        order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, user, null);

        product = ProductFactory.createProduct();

        orderItem = new OrderItem(order, product, 2, 10.0);
        order.getItems().add(orderItem);
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminIsLogged() throws Exception {

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", existingOrderId)
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingOrderId))
                .andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"))
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.client").exists())
                .andExpect(jsonPath("$.client.name").value("Maria Brown"))
                .andExpect(jsonPath("$.payment").exists())
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.items[0].name").value("The Lord of the Rings"))
                .andExpect(jsonPath("$.items[1].name").value("Macbook Pro"))
                .andExpect(jsonPath("$.total").exists());

    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndClientIsLogged() throws Exception {

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", existingOrderId)
                .header("Authorization", "Bearer " + clientToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingOrderId))
                .andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"))
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.client").exists())
                .andExpect(jsonPath("$.client.name").value("Maria Brown"))
                .andExpect(jsonPath("$.payment").exists())
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.items[0].name").value("The Lord of the Rings"))
                .andExpect(jsonPath("$.items[1].name").value("Macbook Pro"))
                .andExpect(jsonPath("$.total").exists());

    }

    @Test
    public void findByIdShouldReturnForbiddenWhenIdExistsAndClientIsLoggedAndOrderDoesNotBelongToUser() throws Exception {

        Long otherOrderId = 2L;
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", otherOrderId)
                .header("Authorization", "Bearer " + clientToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndAdminIsLogged() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", notExistingOrderId)
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndClientIsLogged() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", notExistingOrderId)
                .header("Authorization", "Bearer " + clientToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnUnauthorizedWhenIdExistsAndClientOrAdminIsNotLogged() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", existingOrderId)
                .header("Authorization", "Bearer " + invalidToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }
}
