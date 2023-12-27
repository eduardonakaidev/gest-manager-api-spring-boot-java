package br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.dto;

public record ProductCreateRequestDTO( String name,String description,String category,long price, int stock) {
    
}
