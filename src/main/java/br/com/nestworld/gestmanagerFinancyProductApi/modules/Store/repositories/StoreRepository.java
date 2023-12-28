package br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.entities.StoreEntity;




public interface StoreRepository extends JpaRepository<StoreEntity,UUID>{
    
    StoreEntity findByEmail(String email);
}
