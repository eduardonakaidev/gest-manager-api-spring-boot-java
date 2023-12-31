package br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.dto;

public record ProductCreateRequestDTO( String name,String description,String category,int price, int stock) {
    
}
