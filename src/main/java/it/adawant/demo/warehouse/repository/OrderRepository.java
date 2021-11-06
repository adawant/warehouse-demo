package it.adawant.demo.warehouse.repository;

import it.adawant.demo.warehouse.entity.OrderEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, Long> {
}
