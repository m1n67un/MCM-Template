package com.mg.api.file.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class FileUploadVO {
    private String fileName;
    private String fileOriginalName;
    private String fileDownloadUri;
    private String fileExt;
    private long size;

    public FileUploadVO(String fileName, String fileOriginalName, String fileExt, String fileDownloadUri) {
        this.fileName = fileName;
        this.fileOriginalName = fileOriginalName;
        this.fileExt = fileExt;
        this.fileDownloadUri = fileDownloadUri;
    }
}
