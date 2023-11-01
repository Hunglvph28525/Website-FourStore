package com.poly.datn.service.impl;

import com.poly.datn.dto.ProductDto;
import com.poly.datn.dto.ProductRequest;
import com.poly.datn.entity.Category;
import com.poly.datn.entity.Color;
import com.poly.datn.entity.Image;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.ProductDetail;
import com.poly.datn.entity.Size;
import com.poly.datn.entity.TypeProduct;
import com.poly.datn.repository.ImageRepository;
import com.poly.datn.repository.ProductDetailRepository;
import com.poly.datn.repository.ProductRepository;
import com.poly.datn.repository.PromotionRepository;
import com.poly.datn.service.ImageService;
import com.poly.datn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ProductDetailRepository detailRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private PromotionRepository promotionRepository;


    @Override
    public List<ProductDto> getAll(String status, Long category, Long type, Long brand) {
        if (status.equals("0") && category == 0 && type == 0 && brand == 0) {
            return productRepository.getAll();
        } else {
            return productRepository.locProduct(status, category, type, brand);
        }
    }

    @Override
    public List<Product> getProductNoPromotion() {
        return productRepository.getproductNoPromotion();
    }


    @Override
    public void update(ProductRequest request, Long id) {
        Product product = request.product();
        product.setId(id);
        product = productRepository.save(product);
        String urlImage = "";
        String publicId = "";
        //upload ảnh lên cloudinary
        Map<?, ?> uploadResult = new HashMap<>();
        List<MultipartFile> files = request.getFiles();
        if (files.stream().count() == 0) return;
        try {
            for (MultipartFile file : request.getFiles()) {
                uploadResult = imageService.upload(file, "sanpham");
                urlImage = uploadResult.get("url").toString();
                publicId = uploadResult.get("public_id").toString();
                System.out.println(urlImage);
                imageRepository.save(new Image(product, urlImage, publicId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void save(Long id) {
        Product product = productRepository.getReferenceById(id);
        if (product.getStatus().equals("on")) {
            product.setStatus("off");
        } else {
            product.setStatus("on");
        }
        productRepository.save(product);
    }

    @Override
    public void updatePromotion(Long id, Category category, TypeProduct type, List<Long> products) {
        if (category != null) {
            List<Product> product = productRepository.getProductByCategory(category.getId());
            product.stream().forEach(x -> {
                x.setPromotion(promotionRepository.getReferenceById(id));
            });
            productRepository.saveAll(product);
            return;
        }
        if (type != null) {
            List<Product> product = productRepository.getProductByTypeProduct(type.getId());
            product.stream().forEach(x -> {
                x.setPromotion(promotionRepository.getReferenceById(id));
            });
            productRepository.saveAll(product);
            return;
        }
        if (!products.isEmpty()){
            List<Product> product = new ArrayList<>();
            products.stream().forEach(x -> {
                Product temp = productRepository.getReferenceById(x);
                temp.setPromotion(promotionRepository.getReferenceById(id));
                product.add(temp);
            });
            productRepository.saveAll(product);
            return;
        }
    }

    @Override
    public ProductRequest getProduct(Long id) {
        return new ProductRequest(productRepository.getReferenceById(id));
    }

    @Override
    public Product add(ProductRequest req) {
        Product product = req.product();
        product.setCreateDate(Date.valueOf(LocalDate.now()));
        //zen mã nếu không tạo mã
        if (req.getMaSp().equals(""))
            product.setMaSp(generateProductCode(req.getTypeProduct().getCategory().getName(), req.getName()));
        //thêm sản phẩm mơi
        product = productRepository.save(product);
        List<MultipartFile> files = req.getFiles();
        String urlImage = "";
        String publicId = "";
        //upload ảnh lên cloudinary
        Map<?, ?> uploadResult = new HashMap<>();
        try {
            if (files.stream().count() >= 1) {
                for (MultipartFile file : files) {
                    uploadResult = imageService.upload(file, "sanpham");
                    urlImage = uploadResult.get("url").toString();
                    publicId = uploadResult.get("public_id").toString();
                    System.out.println(urlImage);
                    imageRepository.save(new Image(product, urlImage, publicId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Color color : req.getColors()) {
            for (Size size : req.getSizes()) {
                detailRepository.save(new ProductDetail(product, color, size, 0, req.getPrice(), req.getStatus()));
            }
        }
        return product;
    }


    @Override
    public Product detail(Long id) {
        return productRepository.getReferenceById(id);
    }

    private String generateProductCode(String categoryName, String productName) {
        // Loại bỏ các ký tự không mong muốn và chuyển thành chữ thường
        categoryName = categoryName.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
        productName = productName.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
        // Tách các từ trong tên danh mục và tên sản phẩm
        String[] categoryWords = categoryName.split(" ");
        String[] productWords = productName.split(" ");
        StringBuilder code = new StringBuilder();
        // Lấy chữ cái đầu tiên của từng từ trong danh mục
        for (String word : categoryWords) {
            if (!word.isEmpty()) {
                code.append(word.charAt(0));
            }
        }
        // Lấy chữ cái đầu tiên của từng từ trong tên sản phẩm
        for (String word : productWords) {
            if (!word.isEmpty()) {
                code.append(word.charAt(0));
            }
        }
        // Thêm một chuỗi ngẫu nhiên để tránh trùng
        String randomString = generateRandomString(4);
        code.append(randomString);

        // Trả về mã sản phẩm
        return code.toString().toUpperCase();
    }

    // Hàm tạo chuỗi ngẫu nhiên
    private String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }
        return randomString.toString();
    }
}
