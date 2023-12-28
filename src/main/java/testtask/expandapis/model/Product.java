package testtask.expandapis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "entry_date")
    private LocalDate entryDate;
    @Column(nullable = false, name = "code")
    private String itemCode;
    @Column(nullable = false, name = "name")
    private String itemName;
    @Column(nullable = false, name = "quantity")
    private Integer itemQuantity;
    @Column(nullable = false)
    private String status;
}
