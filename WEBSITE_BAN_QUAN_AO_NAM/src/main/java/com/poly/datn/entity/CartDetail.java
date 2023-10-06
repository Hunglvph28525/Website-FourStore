package com.poly.datn.entity;
import com.poly.datn.entity.composite.CartId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "CartDetails")

public class CartDetail {
    @EmbeddedId
    private CartId cartId;

    @Min(1)
    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;
}
