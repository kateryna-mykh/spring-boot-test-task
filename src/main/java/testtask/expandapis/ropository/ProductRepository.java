package testtask.expandapis.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import testtask.expandapis.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
}
