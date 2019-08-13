package com.zezaeoh.nasresourceserving.web;

import com.zezaeoh.nasresourceserving.annotation.WildcardParam;
import com.zezaeoh.nasresourceserving.service.NasResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ServingController {
    private final NasResourceService nrs;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping(value = {"/upload/**", "/mall/upload/**"})
    public ResponseEntity<InputStreamResource> getNasResource(@WildcardParam String restUri){
        return nrs.getNasResource(restUri);
    }

    @PostMapping(value = "/uploadToNas",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity uploadNasResource(@RequestParam("upload") MultipartFile uploadFile){
        var re = nrs.uploadNasResource(uploadFile);

        if(re == null)
            return ResponseEntity
                    .badRequest()
                    .build();

        var jsonObj = new HashMap<String, File>();
        jsonObj.put("uploadedFile", re);
        return ResponseEntity
                .ok()
                .body(jsonObj);
    }

    @PostMapping(value = "/uploadToNasDump",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity uploadNasResource(@RequestParam("upload") MultipartFile[] uploadFiles){
        File re;
        var uploadedFiles = new LinkedList<File>();

        for(var uploadFile : uploadFiles) {
            re = nrs.uploadNasResource(uploadFile);
            if(re == null) {
                nrs.deleteNasResource(uploadedFiles);
                return ResponseEntity
                        .badRequest()
                        .build();
            } else uploadedFiles.add(re);
        }

        var jsonObj = new HashMap<String, List>();
        jsonObj.put("uploadedFiles", uploadedFiles);

        return ResponseEntity
                .ok()
                .body(jsonObj);
    }
}
