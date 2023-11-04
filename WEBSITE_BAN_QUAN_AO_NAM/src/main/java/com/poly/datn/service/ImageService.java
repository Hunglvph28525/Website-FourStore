package com.poly.datn.service;

import com.poly.datn.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ImageService {
    Boolean deleteImage(Long id);

    Map<?, ?> upload(MultipartFile file, String foder) throws IOException;

    List<Image> getListanh(Long id);
}
