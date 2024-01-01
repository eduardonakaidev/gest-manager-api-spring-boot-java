package br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.dto.GetByCategoryRequestDTO;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.dto.GetByNameProductDTO;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.dto.ProductCreateRequestDTO;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.entities.ProductEntity;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/create")
    public ResponseEntity<Object> createProduct(@RequestBody @Valid ProductCreateRequestDTO body) {
        
       

    ProductEntity productAlreadyExists = this.productRepository.findByName(body.name());
    // verificando se o produto ja foi criado anteriormente
    if(productAlreadyExists==null)
    { 
        // repassa todas informações para o produto
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(body.name().trim());
        productEntity.setCategory(body.category().trim());
        productEntity.setDescription(body.description().trim());
        productEntity.setPrice(body.price());
        productEntity.setStock(body.stock());
        
// verificando se o usuario nao ta passando uma string com espaços em branco exemplo: "    " 
     
        if(body.name().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("nome esta em branco ou null");

        }if(body.category().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("categoria esta em branco ou null");
        }

        // salva o produto
        var result = this.productRepository.save(productEntity);
        return ResponseEntity.ok().body(result);
    }

    else
    {

        return ResponseEntity.badRequest().body(productAlreadyExists);
    }
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

    @PutMapping("/update")
@Transactional
public ResponseEntity<Object> updateProductById(@RequestBody @Valid ProductEntity body) {
    Optional<ProductEntity> productOptional = this.productRepository.findById(body.getId());

    if (productOptional.isPresent()) {
        ProductEntity productEntity = productOptional.get();

        if (body.getName() != null && !body.getName().isBlank()) {
            productEntity.setName(body.getName());
        }

        if (body.getUrlPhotoProduct() != null && !body.getUrlPhotoProduct().isBlank()) {
            productEntity.setUrlPhotoProduct(body.getUrlPhotoProduct());
        }

        if (body.getCategory() != null && !body.getCategory().isBlank()) {
            productEntity.setCategory(body.getCategory());
        }

        if (body.getDescription() != null && !body.getDescription().isBlank()) {
            productEntity.setDescription(body.getDescription());
        }

        if (body.getPrice() != null) {
            productEntity.setPrice(body.getPrice());
        }

        if (body.getStock() != null) {
            productEntity.setStock(body.getStock());
        }

        // Salvar o produto atualizado no banco de dados
        this.productRepository.save(productEntity);

        // Retornar uma resposta com o produto atualizado
        return ResponseEntity.ok(productEntity);
    } else {
        // Retornar resposta indicando que o produto não foi encontrado
        return ResponseEntity.notFound().build();
    }
}

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id) {
        Optional<ProductEntity> productExists = this.productRepository.findById(id);
        if (productExists.isPresent()) {
            this.productRepository.delete(productExists.get());
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id invalido // produto não encontrado");
        }
    }

}
