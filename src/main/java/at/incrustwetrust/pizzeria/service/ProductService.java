package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.entity.Product;
import at.incrustwetrust.pizzeria.exception.ObjectAlreadyExistsException;
import at.incrustwetrust.pizzeria.exception.ObjectNotFoundException;
import at.incrustwetrust.pizzeria.repository.ProductRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        ifProductNameAlreadyExistThrow (product);
        // Productname does not exist -> new Product
        return productRepository.save(product);
    }


    public Product read(String id) {
        return productRepository.findById(id).orElseThrow( () -> new ObjectNotFoundException("Kein Produkt mit der ID " + id + " vorhanden"));
    }

    public List<Product> readAll() {
        return productRepository.findAll();
    }
    public Product delete (String id) {
        Product productCheck = productRepository.findById(id).orElseThrow( () -> new ObjectNotFoundException("Kein Produkt mit der ID " + id + " vorhanden"));
        productRepository.delete(productCheck);
        return productCheck;
    }

    public Product update(Product product, String id) {
        Product productToBeUpdated= productRepository.findById(id).orElseThrow( () -> new ObjectNotFoundException("ProduktId nicht in der Datenbank"));
        ifProductNameAlreadyExistThrow (product, id );
        return productRepository.save(product);

    }


    private void ifProductNameAlreadyExistThrow (Product product) {
        Optional<Product> productCheck = productRepository.findProductByProductName(product.getProductName());
        if (productCheck.isPresent()) {
            throw new ObjectAlreadyExistsException("Es ist bereits ein Produkt mit diesem Namen vorhanden");
        }
    }


    private void ifProductNameAlreadyExistThrow (Product product, String productToBeUpdatedId) {
        Optional<Product> productCheck = productRepository.findProductByProductNameAndProductIdNot(product.getProductName(), productToBeUpdatedId);
        if (productCheck.isPresent()) {
            throw new ObjectAlreadyExistsException("Es ist bereits ein Produkt mit diesem Namen vorhanden");
        }
    }




}
