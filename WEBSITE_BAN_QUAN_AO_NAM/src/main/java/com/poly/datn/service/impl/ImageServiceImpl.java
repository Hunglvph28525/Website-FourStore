package com.poly.datn.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.poly.datn.entity.Image;
import com.poly.datn.entity.Product;
import com.poly.datn.repository.ImageRepository;
import com.poly.datn.repository.ProductRepository;
import com.poly.datn.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository repository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ProductRepository productRepository;


    @Override
    public void deleteImage(Long id) {
        Image image = repository.getReferenceById(id);

        try {
            cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());
            repository.delete(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Long sp, MultipartFile[] files) {
        String url = "";
        String publicId = "";
        Product product = productRepository.getReferenceById(sp);
        try {
            for (MultipartFile file : files) {
                Map<String, Object> params = ObjectUtils.asMap("folder", "sanpham"); // Thay your_folder_name bằng tên folder bạn muốn lưu ảnh
                Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
                url = uploadResult.get("url").toString();
                publicId = uploadResult.get("public_id").toString();
                System.out.println(url);
//            Lưu src vào db

                repository.save(new Image(product, url, publicId));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
