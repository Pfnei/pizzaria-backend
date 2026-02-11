package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.dto.product.*;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    // ... (readAll, read, create, update Tests bleiben gleich)

    // 10. Happy Path: DELETE /products/{id} als Admin -> 200
    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_returnsProduct_whenAdmin() throws Exception {
        // GEÄNDERT: Nutzt jetzt ProductResponseDTO statt DeleteDTO
        ProductResponseDTO resp = ProductResponseDTO.builder()
                .productId("2")
                .productName("Salami")
                .build();

        when(productService.delete("2")).thenReturn(resp);

        mockMvc.perform(delete("/products/2")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("2"))
                .andExpect(jsonPath("$.productName").value("Salami"));
    }

    // 11. Security Check: DELETE /products/{id} ohne Admin -> 403
    @Test
    @WithMockUser(roles = "USER")
    void delete_returns403_forNonAdmin() throws Exception {
        mockMvc.perform(delete("/products/2")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    // 12. Exception Mapping: DELETE /products/{id} -> 404
    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_returns404_whenNotFound() throws Exception {
        when(productService.delete("999")).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(delete("/products/999")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}