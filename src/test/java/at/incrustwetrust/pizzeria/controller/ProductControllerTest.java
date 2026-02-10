package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.dto.product.ProductCreateDTO;
import at.incrustwetrust.pizzeria.dto.product.ProductResponseDTO;
import at.incrustwetrust.pizzeria.dto.product.ProductResponseLightDTO;
import at.incrustwetrust.pizzeria.exception.ProductAlreadyExistsException;
import at.incrustwetrust.pizzeria.exception.ResourceNotFoundException;
import at.incrustwetrust.pizzeria.security.JwtService;
import at.incrustwetrust.pizzeria.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ProductController.class)
@Import({JwtService.class, ProductControllerTest.TestSecurityConfig.class})
class ProductControllerTest {

    @org.springframework.boot.test.context.TestConfiguration
    @org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
    static class TestSecurityConfig {
        @org.springframework.context.annotation.Bean
        public org.springframework.security.web.SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
            return http
                    .csrf(org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;


    // 1. Happy Path: GET /products
    @Test
    @WithMockUser
    void readAll_returnsList() throws Exception {
        ProductResponseLightDTO p = ProductResponseLightDTO.builder()
                .productId("1")
                .productName("Margherita")
                .price(8.50)
                .build();

        when(productService.readAll()).thenReturn(List.of(p));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value("1"))
                .andExpect(jsonPath("$[0].productName").value("Margherita"))
                .andExpect(jsonPath("$[0].price").value(8.50));
    }

    // 2. Exception Mapping: GET /products/{id} -> 404
    @Test
    @WithMockUser
    void read_returns404_whenNotFound() throws Exception {
        when(productService.read("999")).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not found"));
    }

    // 3. Security Check: POST /products ohne Admin -> 403
    @Test
    @WithMockUser(roles = "USER")
    void create_returns403_forNonAdmin() throws Exception {
        ProductCreateDTO dto = ProductCreateDTO.builder().productName("Pizza").price(10.0).build();

        mockMvc.perform(post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    // 4. Happy Path: POST /products als Admin -> 200
    @Test
    @WithMockUser(roles = "ADMIN")
    void create_returnsProduct_whenAdmin() throws Exception {
        ProductCreateDTO dto = ProductCreateDTO.builder()
                .productName("Salami")
                .price(9.0)
                .build();

        ProductResponseDTO resp = ProductResponseDTO.builder()
                .productId("2")
                .productName("Salami")
                .build();

        when(productService.create(any())).thenReturn(resp);

        mockMvc.perform(post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("2"))
                .andExpect(jsonPath("$.productName").value("Salami"));
    }

    // 5. Conflict Mapping: POST /products -> 409
    @Test
    @WithMockUser(roles = "ADMIN")
    void create_returns409_whenAlreadyExists() throws Exception {
        when(productService.create(any())).thenThrow(new ProductAlreadyExistsException("Exists"));

        ProductCreateDTO dto = ProductCreateDTO.builder()
                .productName("Pizza")
                .price(10.0)
                .build();

        mockMvc.perform(post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Exists"));
    }

    // 6. Validation Check: POST /products -> 400
    @Test
    @WithMockUser(roles = "ADMIN")
    void create_returns400_whenInvalid() throws Exception {
        ProductCreateDTO dto = ProductCreateDTO.builder()
                .productName("") // Leer (NotBlank)
                .price(-1.0)     // Negativ (Positive)
                .build();

        mockMvc.perform(post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
