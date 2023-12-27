package br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.entities.StoreEntity;



@Service
public interface StoreRepository extends JpaRepository<StoreEntity,UUID>{
    
    StoreEntity findByEmail(String email);
}
