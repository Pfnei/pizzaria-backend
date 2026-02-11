package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.product.*;
import at.incrustwetrust.pizzeria.entity.Allergen;
import at.incrustwetrust.pizzeria.entity.Product;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.exception.ProductAlreadyExistsException;
import at.incrustwetrust.pizzeria.exception.ResourceNotFoundException;
import at.incrustwetrust.pizzeria.mapper.ProductMapper;
import at.incrustwetrust.pizzeria.repository.AllergenRepository;
import at.incrustwetrust.pizzeria.repository.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    AllergenRepository allergenRepository;
    @Mock
    CurrentUserService currentUserService;
    @Mock
    ProductMapper productMapper;
    @Mock
    FileStorageService fileService;

    @InjectMocks
    ProductService productService;

    @Test
    void create_savesProduct_andReturnsResponseDto() {
        ProductCreateDTO dto = ProductCreateDTO.builder()
                .productName("Margherita")
                .price(8.9)
                .allergens(List.of("A", "L"))
                .build();

        Product mappedEntity = new Product();
        User current = new User();
        List<Allergen> allergens = List.of(new Allergen(), new Allergen());
        Product saved = new Product();
        ProductResponseDTO response = new ProductResponseDTO();

        when(productRepository.findProductByProductName("Margherita")).thenReturn(Optional.empty());
        when(productMapper.toEntity(dto)).thenReturn(mappedEntity);
        when(currentUserService.getCurrentUserEntity()).thenReturn(current);
        when(allergenRepository.findAllById(dto.getAllergens())).thenReturn(allergens);
        when(productRepository.save(mappedEntity)).thenReturn(saved);
        when(productMapper.toResponseDto(saved)).thenReturn(response);

        ProductResponseDTO result = productService.create(dto);

        assertThat(result).isSameAs(response);
        assertThat(mappedEntity.getCreatedBy()).isSameAs(current);
        assertThat(mappedEntity.getLastUpdatedBy()).isSameAs(current);
        assertThat(mappedEntity.getAllergens()).isEqualTo(allergens);
    }

    @Test
    void create_throws_whenNameExists() {
        ProductCreateDTO dto = ProductCreateDTO.builder().productName("Margherita").build();
        when(productRepository.findProductByProductName("Margherita"))
                .thenReturn(Optional.of(new Product()));

        assertThatThrownBy(() -> productService.create(dto))
                .isInstanceOf(ProductAlreadyExistsException.class);
        verify(productRepository, never()).save(any());
    }

    @Test
    void read_returnsDto_whenFound() {
        Product entity = new Product();
        when(productRepository.findById("id")).thenReturn(Optional.of(entity));
        ProductResponseDTO dto = new ProductResponseDTO();
        when(productMapper.toResponseDto(entity)).thenReturn(dto);

        ProductResponseDTO result = productService.read("id");
        assertThat(result).isSameAs(dto);
    }

    @Test
    void read_throws_whenNotFound() {
        when(productRepository.findById("id")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productService.read("id"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void readAll_mapsList_toLightDtos() {
        List<Product> products = List.of(new Product(), new Product());
        when(productRepository.findAll()).thenReturn(products);
        List<ProductResponseLightDTO> light = List.of(new ProductResponseLightDTO(), new ProductResponseLightDTO());
        when(productMapper.toResponseLightDtoList(products)).thenReturn(light);

        List<ProductResponseLightDTO> result = productService.readAll();
        assertThat(result).isSameAs(light);
    }

    @Test
    void update_updatesEntity_andReturnsDto() {
        ProductUpdateDTO dto = ProductUpdateDTO.builder()
                .productName("Diavola")
                .allergens(List.of("A"))
                .build();

        Product existing = new Product();
        existing.setAllergens(null);
        when(productRepository.findById("pid")).thenReturn(Optional.of(existing));
        when(productRepository.findProductByProductNameAndProductIdNot("Diavola", "pid"))
                .thenReturn(Optional.empty());

        User current = new User();
        when(currentUserService.getCurrentUserEntity()).thenReturn(current);
        List<Allergen> allergens = List.of(new Allergen());
        when(allergenRepository.findAllById(dto.getAllergens())).thenReturn(allergens);

        doAnswer(inv -> {
            // simulate mapper applying fields
            ProductUpdateDTO d = inv.getArgument(0);
            Product e = inv.getArgument(1);
            e.setProductName(d.getProductName());
            return null;
        }).when(productMapper).updateEntity(eq(dto), eq(existing));

        when(productRepository.save(existing)).thenReturn(existing);
        ProductResponseDTO mapped = new ProductResponseDTO();
        when(productMapper.toResponseDto(existing)).thenReturn(mapped);

        ProductResponseDTO result = productService.update(dto, "pid");

        assertThat(existing.getLastUpdatedBy()).isSameAs(current);
        assertThat(existing.getAllergens()).isEqualTo(allergens);
        assertThat(result).isSameAs(mapped);
    }

    @Test
    void update_clearsAndReplacesAllergens_whenExistingNotNull() {
        ProductUpdateDTO dto = ProductUpdateDTO.builder().allergens(List.of("A", "L")).build();
        Product existing = new Product();
        existing.setAllergens(new java.util.ArrayList<>(List.of(new Allergen())));
        when(productRepository.findById("pid")).thenReturn(Optional.of(existing));
        when(productRepository.findProductByProductNameAndProductIdNot(null, "pid"))
                .thenReturn(Optional.empty());
        when(currentUserService.getCurrentUserEntity()).thenReturn(new User());

        List<Allergen> newAllergens = List.of(new Allergen(), new Allergen());
        when(allergenRepository.findAllById(dto.getAllergens())).thenReturn(newAllergens);
        when(productRepository.save(existing)).thenReturn(existing);
        when(productMapper.toResponseDto(existing)).thenReturn(new ProductResponseDTO());

        productService.update(dto, "pid");
        assertThat(existing.getAllergens()).containsExactlyElementsOf(newAllergens);
    }

    @Test
    void update_throws_whenDuplicateNameForOtherId() {
        ProductUpdateDTO dto = ProductUpdateDTO.builder().productName("Margherita").build();
        when(productRepository.findById("pid")).thenReturn(Optional.of(new Product()));
        when(productRepository.findProductByProductNameAndProductIdNot("Margherita", "pid"))
                .thenReturn(Optional.of(new Product()));

        assertThatThrownBy(() -> productService.update(dto, "pid"))
                .isInstanceOf(ProductAlreadyExistsException.class);
        verify(productRepository, never()).save(any());
    }

    @Test
    void update_throws_whenIdNotFound() {
        when(productRepository.findById("pid")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productService.update(new ProductUpdateDTO(), "pid"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_deletesImageIfPresent_andReturnsDto() {
        Product existing = new Product();
        existing.setProductPicture("img.png");
        when(productRepository.findById("pid")).thenReturn(Optional.of(existing));

        ProductResponseDTO mapped = new ProductResponseDTO();
        when(productMapper.toResponseDto(existing)).thenReturn(mapped);

        ProductDeleteDTO result = productService.delete("pid");

        verify(fileService).deleteProductImage("img.png");
        verify(productRepository).delete(existing);
        assertThat(result).isSameAs(mapped);
    }

    @Test
    void delete_throws_whenNotFound() {
        when(productRepository.findById("pid")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productService.delete("pid"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
