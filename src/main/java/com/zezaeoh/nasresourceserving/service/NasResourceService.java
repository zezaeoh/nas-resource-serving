package com.zezaeoh.nasresourceserving.service;

import com.zezaeoh.nasresourceserving.component.NasSettings;
import com.zezaeoh.nasresourceserving.tika.TikaAnalysis;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class NasResourceService {
    private NasSettings nasSettings;

    public ResponseEntity<InputStreamResource> getNasResource(String restUri){
        var resourceFile = new FileSystemResource(nasSettings.getBasePath() + restUri);
        log.info("restUri: " + restUri);
        try {
            var iStream =  resourceFile.getInputStream();
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(TikaAnalysis.detectDocTypeUsingFacade(iStream)))
                    .body(new InputStreamResource(resourceFile.getInputStream()));
        } catch (IOException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    public File uploadNasResource(MultipartFile uploadFile){
        OutputStream out = null;
        File file = null;

        try {
            var fileName = uploadFile.getOriginalFilename();
            var bytes = uploadFile.getBytes();
            var uploadPath = nasSettings.getBasePath() + fileName;

            file = new File(uploadPath);
            out = new FileOutputStream(file);

            out.write(bytes);
        } catch (IOException e) {
            log.error("File upload fail: " + file);
            return null;
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("File upload success: " + file);
        return file;
    }

    public void deleteNasResource(File file){
        Assert.notNull(file, "Nas Resource upload response must have a file object!");

        if(file.exists()) {
            if(file.delete()) log.info("File deleted: " + file);
            else log.warn("File not deleted!: " + file);
        } else log.warn("There is no file: " + file);
    }

    public void deleteNasResource(List<File> res){
        for(var re: res)
            deleteNasResource(re);
    }
}
