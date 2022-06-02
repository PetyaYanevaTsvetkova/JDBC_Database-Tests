package entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

/**
 * Class of type POJO using Annotations.
 * 'Column' annotations are with 'name' value same as that of SQL column.
 * The fields are with same type as that of SQL column.
 * The annotation 'Entity' is required to mark the class capable of hold database values.
 * The annotation 'Build' is required to use Builder Design Pattern for creating a instance of type Order.
 */
@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {
    @Id
    @Column(name = "order_id")
    private Long order_id;

    @Column(name = "customer_id")
    @NonNull
    private Long customer_id;

    @Column(name = "is_order_completed")
    @NonNull
    private boolean is_order_completed;

    @Column(name = "is_order_payed")
    @NonNull
    private boolean is_order_payed;

    @Column(name = "date_of_order")
    @NonNull
    private Date date_of_order;

    @Column(name = "date_order_completed")
    private Date date_order_completed;
}
