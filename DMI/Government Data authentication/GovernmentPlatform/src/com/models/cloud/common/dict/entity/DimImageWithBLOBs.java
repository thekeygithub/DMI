package com.models.cloud.common.dict.entity;

public class DimImageWithBLOBs extends DimImage {
    private byte[] content;

    private byte[] thumb;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getThumb() {
        return thumb;
    }

    public void setThumb(byte[] thumb) {
        this.thumb = thumb;
    }
}