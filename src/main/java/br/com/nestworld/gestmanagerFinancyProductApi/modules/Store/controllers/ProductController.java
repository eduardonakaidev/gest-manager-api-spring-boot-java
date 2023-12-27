package br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.dto.GetByCategoryRequestDTO;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.dto.GetByNameProductDTO;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.dto.ProductCreateRequestDTO;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.entities.ProductEntity;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.repositories.ProductRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/create")
    public ResponseEntity<Object> createProduct(@RequestBody @Valid ProductCreateRequestDTO body) {
        ProductEntity productAlreadyExists = this.productRepository.findByName(body.name());
        if (productAlreadyExists == null) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setName(body.name());
            productEntity.setCategory(body.category());
            productEntity.setDescription(body.description());
            productEntity.setPrice(body.price());
            productEntity.setStock(body.stock());
            var result = this.productRepository.save(productEntity);
            return ResponseEntity.ok().body(result);

        }
        return ResponseEntity.badRequest().body(productAlreadyExists);

    }

    @GetMapping("/allProducts")
    public ResponseEntity<Object> GetAllProducts() {
        List<ProductEntity> allproducts;
        allproducts = this.productRepository.findAll();
        if (allproducts == null) {
            return ResponseEntity.badRequest().body("Voçê ainda não registrou nem um produto");
        }
        return ResponseEntity.ok().body(allproducts);
    }

    @PostMapping("/getByName")
    public ResponseEntity<Object> getByName(@RequestBody @Valid GetByNameProductDTO req) {
        String name = req.name();
        if (name.isBlank()) {
            return ResponseEntity.badRequest().body("informe o nome do produto");
        }
        name.trim();

        var resultg = this.productRepository.findByName(name);

        if (resultg == null) {
            return ResponseEntity.badRequest().body("Não á nem um produto registrado com esse nome");
        } else {
            return ResponseEntity.ok().body(resultg);
        }

    }

    @PostMapping("/getByCategory")
    public ResponseEntity<Object> getByCategory(@RequestBody @Valid GetByCategoryRequestDTO req) {
        String categoryString = req.category();
        if (categoryString.isBlank()) {

            System.out.println("Categoria em branco.");
            return ResponseEntity.badRequest().body("informe uma categoria!");

        }
        String categoryTrim = categoryString.trim();
        System.out.println("Categoria após trim: " + categoryTrim);

        List<ProductEntity> result = this.productRepository.findByCategory(categoryTrim);
        System.out.println("Tamanho da lista result: " + result.size());

        if (result.isEmpty()) {
            return ResponseEntity.badRequest().body("Não á produto registrado com essa categoria");
        } else {
            return ResponseEntity.ok().body(result);
        }
    }

    @GetMapping("/allCategory")
    public ResponseEntity<Object> getAllCategory() {
        List<ProductEntity> allProducts;
        List<String> categories;

        allProducts = this.productRepository.findAll();
        if (allProducts == null) {
            return ResponseEntity.badRequest().body("não á produto registrado");
        }

        categories = allProducts.stream().map(product -> ((ProductEntity) product).getCategory())
                .collect(Collectors.toList());

        Set<String> conjunto = new HashSet<>();
        List<String> listaSemDuplicatas = new ArrayList<>();
        for (String item : categories) {
            // Adiciona o item ao conjunto apenas se ainda não estiver presente
            if (conjunto.add(item)) {
                // Se foi adicionado ao conjunto, significa que é único
                listaSemDuplicatas.add(item);
            }
        }
        return ResponseEntity.ok().body(listaSemDuplicatas);
    }

    // @PutMapping("/updateProduct/{productId}")
    // public ResponseEntity<Object> updateProduct(@PathVariable UUID productId,@RequestBody UpdateProductDTO req) {
    //     Optional<ProductEntity> productExists =  this.productRepository.findById(productId);

    //     ProductEntity prdEntityU = productExists.orElse(null);

    //     if(prdEntityU == null ){
    //         return ResponseEntity.badRequest().body("Produto não existe");
    //     }
    //     ProductEntity productUpdated = new ProductEntity();
    //     productUpdated.setName(req.name().toString());
    //     productUpdated.setUrlPhotoProduct(req.urlPhotoProduct().toString());
    //     productUpdated.setDescription(req.description().toString());
    //     productUpdated.setCategory(req.category().toString());
    //     productUpdated.setPrice(req.price().orElseThrow().longValue());
    //     productUpdated.setStock(req.stock().orElseThrow());
        
    //     prdEntityU = this.productRepository.save(null);
    //     return ResponseEntity.ok().body("Produto atualizado com sucesso");
    // }
}
