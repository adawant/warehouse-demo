package it.adawant.demo.warehouse.repository;

import it.adawant.demo.warehouse.entity.ProductEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Long> {
}
