package com.models.cloud.common.dict.entity;

import java.util.Date;

public class DimImage {
    private String imageId;

    private Short imageTypeId;

    private String fileType;

    private String origFileName;

    private Long fileSize;

    private Integer hPixel;

    private Integer vPixel;

    private Short validFlag;

    private Date updTime;

    private Date crtTime;

    private String remark;

    private String imagePath;

    private String thumbPath;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId == null ? null : imageId.trim();
    }

    public Short getImageTypeId() {
        return imageTypeId;
    }

    public void setImageTypeId(Short imageTypeId) {
        this.imageTypeId = imageTypeId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getOrigFileName() {
        return origFileName;
    }

    public void setOrigFileName(String origFileName) {
        this.origFileName = origFileName == null ? null : origFileName.trim();
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer gethPixel() {
        return hPixel;
    }

    public void sethPixel(Integer hPixel) {
        this.hPixel = hPixel;
    }

    public Integer getvPixel() {
        return vPixel;
    }

    public void setvPixel(Integer vPixel) {
        this.vPixel = vPixel;
    }

    public Short getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(Short validFlag) {
        this.validFlag = validFlag;
    }

    public Date getUpdTime() {
        return updTime;
    }

    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    public Date getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath == null ? null : imagePath.trim();
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath == null ? null : thumbPath.trim();
    }
}