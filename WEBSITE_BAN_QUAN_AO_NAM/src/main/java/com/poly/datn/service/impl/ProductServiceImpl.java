package com.poly.datn.service.impl;

import com.poly.datn.dto.ColorSizeVertion;
import com.poly.datn.dto.ProductDto;
import com.poly.datn.dto.ProductRequest;
import com.poly.datn.entity.Color;
import com.poly.datn.entity.Image;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.ProductDetail;
import com.poly.datn.entity.Size;
import com.poly.datn.repository.ImageRepository;
import com.poly.datn.repository.ProductDetailRepository;
import com.poly.datn.repository.ProductRepository;
import com.poly.datn.service.ImageService;
import com.poly.datn.service.ProductService;
import com.poly.datn.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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


    @Override
    public List<ProductDto> getAll(String status, Long category, Long type, Long brand) {
        if (status.equals("0") && category == 0 && type == 0 && brand == 0) {
            return productRepository.getAll();
        } else {
            return productRepository.locProduct(status, category, type, brand);
        }
    }


    @Override
    public MessageUtil update(ProductRequest request, Long id) {
        Product product = request.product();
        product.setId(id);
        product = productRepository.save(product);
        String urlImage = "";
        String publicId = "";
        //upload ảnh lên cloudinary
        Map<?, ?> uploadResult = new HashMap<>();
        List<MultipartFile> files = request.getFiles();
        if (files.isEmpty())
            return MessageUtil.builder().status(1).message("Thành công !").type("bg-success").build();
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
        return MessageUtil.builder().status(1).message("Thành công !").type("bg-success").build();
    }

    @Override
    public MessageUtil save(Long id) {
        Product product = productRepository.getReferenceById(id);
        if (product.getStatus().equals("on")) {
            product.setStatus("off");
        } else {
            product.setStatus("on");
        }
        productRepository.save(product);
        return MessageUtil.builder().status(1).message("Thành công !").type("bg-success").build();
    }

    @Override
    public ProductDto getDetailtoCart(Long id) {
        Product product = productRepository.getReferenceById(id);
        ProductDto productDto = new ProductDto(product);
        List<ColorSizeVertion> vertions = new ArrayList<>();
        product.getProductDetails().stream().forEach(x ->
                vertions.add(new ColorSizeVertion(x)));
        Set<Color> uniqueColors = new HashSet<>();
        Set<Size> uniqueSizes = new HashSet<>();

        for (ColorSizeVertion vertion : vertions) {
            Color color = vertion.getColor();
            if (color != null) {
                uniqueColors.add(color);
            }
            Size size = vertion.getSize();
            if (size != null) {
                uniqueSizes.add(size);
            }
        }
        List<Color> colors = new ArrayList<>(uniqueColors);
        List<Size> sizes = new ArrayList<>(uniqueSizes);
        productDto.setColors(colors);
        productDto.setSizes(sizes);
        productDto.setVertions(vertions);
        return productDto;
    }

    @Override
    public ProductRequest getProduct(Long id) {
        return new ProductRequest(productRepository.getReferenceById(id));
    }

    @Override
    public MessageUtil add(ProductRequest req) {
        if (productRepository.existsByProductName(req.getName())) {
            return MessageUtil.builder().status(0).message("Thêm thất bại vì đã có sản phẩm này !").type("bg-danger").object(req).build();
        } else if (productRepository.existsByMaSp(req.getMaSp())) {
            return MessageUtil.builder().status(0).message("Thêm thất bại vì đã có mã sản phẩm này !").type("bg-danger").object(req).build();
        } else {
            Product product = req.product();
            product.setCreateDate(Date.valueOf(LocalDate.now()));
            product.setStatus("on");
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
                    detailRepository.save(new ProductDetail(product, color, size, 1, BigDecimal.valueOf(10), req.getStatus()));
                }
            }
            return MessageUtil.builder().status(1).message("Thêm thành công !").type("bg-success").object(product).build();
        }
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
