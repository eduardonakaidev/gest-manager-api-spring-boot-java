package br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.dto;

import java.util.Optional;

public record UpdateProductDTO (Optional<String> name, Optional<String> urlPhotoProduct,Optional<String> description,Optional<String> category,Optional<String> price,Optional<String>stock){
    
}
