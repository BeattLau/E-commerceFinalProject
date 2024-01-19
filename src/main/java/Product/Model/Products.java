package Product.Model;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "Products")
@RequiredArgsConstructor
public class Products{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private double price;
    private String description;
}