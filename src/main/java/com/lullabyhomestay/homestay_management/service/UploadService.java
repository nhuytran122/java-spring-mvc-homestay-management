package com.lullabyhomestay.homestay_management.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

@Service
public class UploadService {
    private final ServletContext servletContext;

    public UploadService(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public String handleSaveUploadFile(MultipartFile file, String targetFolder) {
        if (file.isEmpty())
            return "";

        String rootPath = this.servletContext.getRealPath("/resources/images");
        String finalName = "";
        try {
            byte[] bytes = file.getBytes();

            File dir = new File(rootPath + File.separator + targetFolder);
            if (!dir.exists())
                dir.mkdirs();
            finalName = normalizeFileName(file.getOriginalFilename());
            finalName = System.currentTimeMillis() + "-" + finalName;
            File serverFile = new File(dir.getAbsolutePath() + File.separator + finalName);

            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalName;
    }

    private String normalizeFileName(String fileName) {
	    // Loại bỏ dấu tiếng Việt
	    String normalizedFileName = Normalizer.normalize(fileName, Normalizer.Form.NFD)
	    										.replaceAll("\\p{M}", "");
	    normalizedFileName = normalizedFileName.replaceAll("đ", "d").replaceAll("Đ", "D");
	    // Thay khoảng trắng -> dấu dấu gạch dưới
	    normalizedFileName = normalizedFileName.replaceAll("\\s+", "_");
	    return normalizedFileName;
	}
}
