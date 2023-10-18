package com.poly.datn.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void deleteImage(Long id);

    void add(Long sp, MultipartFile[] files);
}
