package com.poly.datn.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.poly.datn.dto.ProductDto;
import com.poly.datn.entity.Color;
import com.poly.datn.entity.Image;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.ProductDetail;
import com.poly.datn.entity.Size;
import com.poly.datn.repository.ImageRepository;
import com.poly.datn.repository.ProductDetailRepository;
import com.poly.datn.repository.ProductRepository;
import com.poly.datn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ProductDetailRepository detailRepository;
    @Autowired
    private Cloudinary cloudinary;


    @Override
    public List<ProductDto> getAll() {
        List<ProductDto> list = new ArrayList<>();
        productRepository.findAll().stream().forEach(product -> list.add(new ProductDto(product)));
        return list;
    }

    @Override
    public void updateProduct(ProductDto dto, Long sp) {
        Product product = dto.product();
        product.setId(sp);
        productRepository.save(product);
    }

    @Override
    public ProductDto getProductById(Long id) {
        return productRepository.getProduct(id);
    }

    @Override
    public Product add(ProductDto dto) {
        Product product = dto.product();
        product.setQrCode(UUID.randomUUID().toString());
        //thêm sản phẩm mơi
        product = productRepository.save(product);
        MultipartFile[] files = dto.getFiles();
        String urlImage = "/assets/images/users/avatar-2.jpg";
        String publicId = "";
        //upload ảnh lên cloudinary
        Map<String, Object> params = ObjectUtils.asMap("folder", "sanpham");
        Map<?, ?> uploadResult;
        try {
            for (MultipartFile file : files) {
                uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
                urlImage = uploadResult.get("url").toString();
                publicId = uploadResult.get("public_id").toString();
                System.out.println(urlImage);
                imageRepository.save(new Image(product, urlImage, publicId));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        for (Color color : dto.getColor()) {
            for (Size size : dto.getSize()) {
                detailRepository.save(new ProductDetail(product, color, size, 0));
            }
        }

        return product;
    }

    @Override
    public Product detail(Long id) {
        return productRepository.getReferenceById(id);
    }
}
