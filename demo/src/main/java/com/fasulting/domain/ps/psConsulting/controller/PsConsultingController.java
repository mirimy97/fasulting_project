package com.fasulting.domain.ps.psConsulting.controller;

import com.fasulting.domain.ps.psConsulting.service.PsConsultingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Slf4j
@RequestMapping("/ps-consulting")
@CrossOrigin("*")
public class PsConsultingController {

    private PsConsultingService psConsultingService;
    
    public PsConsultingController(PsConsultingService psConsultingService) {
        this.psConsultingService = psConsultingService;
    }

    @GetMapping("/download/{reservationSeq}")
    public ResponseEntity<?> getBeforeImg(@PathVariable Long reservationSeq) {

        String path = psConsultingService.getBeforeImg(reservationSeq);
//        String path = FileManage.beforeImgDirPath;

        try {
            Path filePath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

            File file = new File(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더

            return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
        }
    }



    // 1. 상담 입장
    // 2. 상담 사전 진행
    // 3. 상담 진행
    // 4. 상담 결과 작성

}
