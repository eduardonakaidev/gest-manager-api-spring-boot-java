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
import org.springframework.http.HttpStatusCode;
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
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.http.middleware.verifyPriceFormat;
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
        if (productAlreadyExists == null) {
            String priceRegex = body.price();
            // Verifica se a string tem pelo menos dois caracteres
            if (verifyPriceFormat.isNumeric(priceRegex)) {
                // Verifica se a string tem pelo menos dois caracteres
                if (priceRegex.length() >= 2) {
                    // Obtém os dois últimos caracteres
                    String ultimosDoisNumeros = priceRegex.substring(priceRegex.length() - 2);
    
                    // Obtém a parte da string excluindo os dois últimos caracteres
                    String parteInicial = priceRegex.substring(0, priceRegex.length() - 2);
    
                    // Adiciona um ponto antes dos dois últimos números
                    priceRegex = parteInicial + "." + ultimosDoisNumeros;
    
           
                } else {
                  return ResponseEntity.badRequest().body("A string price deve ter pelo menos dois caracteres. Exemplo : 0000 para ficar 00.00");
                }
            } else {
                System.out.println("A string deve conter apenas números.");
            }
            String stockverify = body.stock();
            if (!verifyPriceFormat.isInteger(stockverify)) {
                // Realiza as operações necessárias, se desejado
                
                return ResponseEntity.badRequest().body("A string stock não contém apenas números inteiros."+ stockverify);
            } else {
             //continua o codigo por so conter numeros inteiros
       
             // repassa todas informações para o produto
            ProductEntity productEntity = new ProductEntity();
            productEntity.setName(body.name());
            productEntity.setCategory(body.category());
            productEntity.setDescription(body.description());
            productEntity.setPrice(priceRegex);
            productEntity.setStock(stockverify);
            // salva o produto
            var result = this.productRepository.save(productEntity);
            return ResponseEntity.ok().body(result);
            }
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
    @PutMapping("/")
    @Transactional
    public ResponseEntity<Object> updateProductById(@RequestBody @Valid ProductEntity body){
        Optional<ProductEntity> product = this.productRepository.findById(body.getId());
        if(product.isPresent()){
            ProductEntity productEntity = new ProductEntity(product.get().getId(),product.get().getName(),product.get().getUrlPhotoProduct(),product.get().getDescription(),product.get().getCategory(),
            product.get().getPrice(),product.get().getStock(),product.get().getCreatedAt());
            if(!body.getName().isBlank()){
                productEntity.setName(body.getName());
            }
            if(!body.getUrlPhotoProduct().isBlank()){
                productEntity.setUrlPhotoProduct(body.getUrlPhotoProduct());
            }
             if(!body.getCategory().isBlank()){
                productEntity.setCategory(body.getCategory());
            }
             if(!body.getDescription().isBlank()){
                productEntity.setDescription(body.getDescription());
            }
             if(!body.getPrice().isBlank()){
                productEntity.setPrice(body.getPrice());
            }
             if(!body.getStock().isBlank()){
                productEntity.setStock(body.getStock());
            }
            return ResponseEntity.status(HttpStatus.OK).body(productEntity);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id){
        Optional<ProductEntity> productExists = this.productRepository.findById(id);
        if(productExists.isPresent()){
            this.productRepository.delete(productExists.get());
            return ResponseEntity.status(HttpStatus.OK).build();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id invalido // produto não encontrado");
        }
    }

}


 