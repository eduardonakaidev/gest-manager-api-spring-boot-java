package br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.entities.ProductEntity;

import java.util.List;
import java.util.Optional;

@Service
@EnableJpaRepositories
public interface ProductRepository extends JpaRepository<ProductEntity,UUID>{
    List<ProductEntity> findByCategory(String category);
    ProductEntity findByName(String name);
    Optional<ProductEntity> findById(UUID id);
    
}
