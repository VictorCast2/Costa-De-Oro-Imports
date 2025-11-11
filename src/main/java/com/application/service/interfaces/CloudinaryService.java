package com.application.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String subirImagen(MultipartFile imagen, String tipo);
    String getImagenUrl(String publicId);
    void deleteImagen(String publicId);
}
