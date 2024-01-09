package com.poly.datn.repository;

import com.poly.datn.dto.SPSapHet;
import com.poly.datn.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long> {
    @Query("select c from ProductDetail c where c.product.id = :id")
    List<ProductDetail> getDetailProduct(Long id);

    ProductDetail getById(Long id);

    boolean existsById(Long id);

    @Query("select c from ProductDetail c where c.product.id = :productId and c.color.id =:colorId and c.size.id =:sizeId")
    ProductDetail getProduct(Long productId, Long colorId, Long sizeId);

    //tổng sản phẩm
    @Query(value = "select sum(quantity) from productDetails",nativeQuery = true)
    Integer getTongSoSP();


    //số sản phẩm sắp hết hàng
    @Query(value = "select count(id) from productDetails where quantity < 5",nativeQuery = true)
    Integer getSPSapHet();

    //sản phẩm sắp hết hàng
    @Query(value = "select product_name 'productName',url, color_name 'colorName', Size.name,quantity from productDetails\n" +
            "join Products on  Products.id = ProductDetails.id_product\n" +
            "join Colors on Colors.id = ProductDetails.id_color\n" +
            "join Size on Size.id = ProductDetails.id_size\n" +
            "join Images on Products.id = Images.id_product\n" +
            "where quantity < 5 \n" +
            "ORDER BY quantity asc",nativeQuery = true)
    List<SPSapHet>  sapHet();

    @Query("select c from ProductDetail c where c.quantity < 10")
    List<ProductDetail> getSpSapHet();
}
