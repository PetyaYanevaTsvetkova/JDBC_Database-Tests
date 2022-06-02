package entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Class of type POJO using Annotations.
 * 'Column' annotations are with 'name' value same as that of SQL column.
 * The fields are with same type as that of SQL column.
 * The annotation 'Entity' is required to mark the class capable of hold database values.
 * The annotation 'Build' is required to use Builder Design Pattern for creating a instance of type Product.
 */
@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    @Id
    @Column(name = "product_id")
    private Long product_id;

    @Column(name = "product_name")
    @NonNull
    private String product_name;

    @Column(name = "available_quantity")
    @NonNull
    private Integer available_quantity;

    @Column(name = "product_type")
    @NonNull
    private String product_type;

    @Column(name = "price_without_VAT")
    @NonNull
    private Double price_without_VAT;

    @Column(name = "price_with_VAT")
    @NonNull
    private Double price_with_VAT;

    @Column(name = "is_product_in_stock")
    @NonNull
    private boolean is_product_in_stock;

    @Column(name = "warehouse")
    @NonNull
    private String warehouse;
}
