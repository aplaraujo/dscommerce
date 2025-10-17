package com.example.dscommerce.controllers.it;

import com.example.dscommerce.dto.ProductDTO;
import com.example.dscommerce.entities.Category;
import com.example.dscommerce.entities.Product;
import com.example.dscommerce.repositories.ProductRepository;
import com.example.dscommerce.services.ProductService;
import com.example.dscommerce.tests.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private String productName;
    private String adminUsername;
    private String adminPassword;
    private String adminToken;
    private String clientUsername;
    private String clientPassword;
    private String clientToken;
    private String invalidToken;
    private Long existingProductId;
    private Long notExistingProductId;
    private Long dependentProductId;
    private Long countTotalProducts;
    private Product product;
    private ProductDTO dto;
    private Category category;

    @BeforeEach
    void setUp() throws Exception {
        existingProductId = 1L;
        notExistingProductId = 100L;
        dependentProductId = 3L;

        countTotalProducts = 25L;
        productName = "Macbook";
        adminUsername = "alex@gmail.com";
        adminPassword = "123456";
        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        clientUsername = "maria@gmail.com";
        clientPassword = "123456";
        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        invalidToken = adminToken + "xpto";

        category = new Category(2L, "Roupas");
        product = new Product(null, "Meu novo produto", "Lorem ipsum, dolor sit amet consectetur adipisicing elit. Qui ad, adipisci illum ipsam velit et odit eaque reprehenderit ex maxime delectus dolore labore, quisquam quae tempora natus esse aliquam veniam doloremque quam minima culpa alias maiores commodi. Perferendis enim", 100.0, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg");
        product.getCategories().add(category);

        dto = new ProductDTO(product);
    }

    @Test
    public void findAllShouldReturnPageWhenNameParamIsEmpty() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/products").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].id").value(1L));
        result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
        result.andExpect(jsonPath("$.content[0].price").value(90.5));
        result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
    }

    @Test
    public void findAllShouldReturnPageWhenNameParamIsNotEmpty() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/products?name={productName}", productName).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].id").value(3L));
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[0].price").value(1250.0));
        result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));
    }

    @Test
    public void insertShouldReturnProductDTOCreatedWhenValidDataAndAdminIsLogged() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(countTotalProducts + 1))
                .andExpect(jsonPath("$.name").value("Meu novo produto"))
                .andExpect(jsonPath("$.description").value("Lorem ipsum, dolor sit amet consectetur adipisicing elit. Qui ad, adipisci illum ipsam velit et odit eaque reprehenderit ex maxime delectus dolore labore, quisquam quae tempora natus esse aliquam veniam doloremque quam minima culpa alias maiores commodi. Perferendis enim"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"))
                .andExpect(jsonPath("$.categories[0].id").value(2L));

    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenInvalidNameAndAdminIsLogged() throws Exception {
        product.setName("    ");
        dto = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(dto);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenInvalidDescriptionAndAdminIsLogged() throws Exception {
        product.setDescription("hey");
        dto = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(dto);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .header("Authorization", "Bearer " + adminToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenPriceIsNegativeAndAdminIsLogged() throws Exception {
        product.setPrice(-5.00);
        dto = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(dto);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .header("Authorization", "Bearer " + adminToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenPriceIsZeroAndAdminIsLogged() throws Exception {
        product.setPrice(0.0);
        dto = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(dto);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .header("Authorization", "Bearer " + adminToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenProductDoesNotHaveCategoryAndAdminIsLogged() throws Exception {
        product.getCategories().clear();
        dto = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(dto);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .header("Authorization", "Bearer " + adminToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertShouldReturnForbiddenWhenClientIsLogged() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .header("Authorization", "Bearer " + clientToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isForbidden());
    }

    @Test
    public void insertShouldReturnUnauthorizedWhenAdminIsNotLogged() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .header("Authorization", "Bearer " + invalidToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExistsAndAdminIsLogged() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        String expectedName = dto.getName();
        String expectedDescription = dto.getDescription();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingProductId)
                .header("Authorization", "Bearer " + adminToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingProductId))
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.description").value(expectedDescription));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExistAndAdminIsLogged() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", notExistingProductId)
                .header("Authorization", "Bearer " + adminToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExistsAndAdminIsLogged() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", existingProductId)
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExistAndAdminIsLogged() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", notExistingProductId)
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS) 
    public void deleteShouldReturnBadRequestWhenDependentIdExistsAndAdminIsLogged() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", dependentProductId)
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void deleteShouldReturnForbiddenWhenIdExistsAndClientIsLogged() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", existingProductId)
                .header("Authorization", "Bearer " + clientToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void deleteShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", existingProductId)
                .header("Authorization", "Bearer " + invalidToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }
}
