package com.mg.api.file.controller;

import com.mg.api.file.service.FileService;
import com.mg.api.file.vo.FileUploadVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/file")

public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @PostMapping("/uploadFile")
    public FileUploadVO uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            // 파일 업로드가 안됐을 시 처리
            log.info("파일 업로드 실패! 비어있음");
            return null;
        }
        // 저장된 파일명
        String fileName = fileService.storeFile(file);
        // 파일의 확장자만 추출
        String fileExt = fileName.replaceAll("^.*\\.(.*)$", "$1");

        // 파일의 원래 이름
        String fileOriginalName = StringUtils.cleanPath(file.getOriginalFilename());

        // 다운로드시 사용할 URI
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/Downloads/")
                .path(fileName)
                .toUriString();

        return new FileUploadVO(fileName, fileOriginalName, fileExt, fileDownloadUri);
    }

    @PostMapping("/uploadmultiplefiles")
    public List<FileUploadVO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadfile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request)
            throws Exception {
        // 리소스에서 파일을 불러옴
        Resource resource = fileService.loadFileAsResource(fileName);

        // 파일 타입을 읽어와서 지정
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("파일 타입이 지정되지 않음");
        }

        // 유형을 결정할 수 없는 경우 기본 값으로 대체
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
