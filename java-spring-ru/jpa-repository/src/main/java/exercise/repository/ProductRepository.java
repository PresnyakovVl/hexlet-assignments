package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import exercise.model.Product;

import org.springframework.data.domain.Sort;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // BEGIN
    List<Product> findAllByPriceOrderByPrice(Integer price);
    List<Product> findAllByPriceLessThanOrderByPrice(Integer price);
    List<Product> findAllByPriceLessThanEqualOrderByPrice(Integer price);
    List<Product> findAllByPriceGreaterThanOrderByPrice(Integer price);
    List<Product> findAllByPriceGreaterThanEqualOrderByPrice(Integer price);

    List<Product> findAllByPriceBetweenOrderByPrice(Integer startPrice, Integer endPrice);
    // END
}
