package com.mg.api.file.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Slf4j
@Service
public class FileService {
    private static String uploadPath;

    private Path fileStorageLocation;

    // 최초 생성자에서 디렉토리가 없는 경우 생성
    public FileService(@Value("${upload-path}") String value) throws Exception {
        uploadPath = value;
        this.fileStorageLocation = Paths.get(uploadPath)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            log.error("파일이 업로드 될 디렉토리를 생성에 실패했습니다.");
            throw ex;
        }
    }

    // 파일 저장
    public String storeFile(MultipartFile file) {
        // 밀리세컨드 단위까지 지정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS_");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String timeStamp = sdf.format(timestamp);

        // 새로운 파일 명
        String fileName = timeStamp + StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // 파일 명에 허용되지 않은 문자열이 있는지 체크
            if (fileName.contains("..")) {
                log.error("다음 파일 명에 허용되지 않은 문자열이 존재합니다! >>> {}", fileName);
            }

            // 복사하면서 파일명이 같은 경우 파일을 덮어쓰기
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 이미지 파일이라면 썸네일을 생성한다.
            String fileExt = fileName.replaceAll("^.*\\.(.*)$", "$1");
            if (fileExt.equalsIgnoreCase("jpg") || fileExt.equalsIgnoreCase("png") || fileExt.equalsIgnoreCase("gif")) {
                thumbnailFileMake(fileName, new File(targetLocation.toString()));
            }
        } catch (IOException ex) {
            log.error("다음 파일을 저장할 수 없습니다. >>> " + fileName + ". 에러 >>> !", ex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }

    // 파일을 다운로드 하기 위해서 리소스에서 읽어들이는 기능
    public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new Exception("다음 파일을 찾을 수 없습니다 >>> " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new Exception("다음 파일을 찾을 수 없습니다 >>> " + fileName, ex);
        }
    }

    // 이미지의 경우 로딩이 오래 걸릴수 있으니 업로드 할 때부터 크기를 줄인 파일을 생성
    public void thumbnailFileMake(String oriFileName, File oriFile) throws Exception {
        File thumbnailFile = new File(uploadPath, "s_" + oriFileName);

        // 파일을 읽어들여 n배로 축소
        BufferedImage bo_img = ImageIO.read(oriFile);
        double ratio = 3;
        int width = (int) (bo_img.getWidth() / ratio);
        int height = (int) (bo_img.getHeight() / ratio);

        Thumbnails.of(oriFile)
                .size(width, height)
                .toFile(thumbnailFile);
    }
}
