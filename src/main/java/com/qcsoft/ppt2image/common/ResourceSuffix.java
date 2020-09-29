package com.qcsoft.ppt2image.common;

public enum ResourceSuffix {
    PNG("png"), PDF("pdf");

    private String value;

    ResourceSuffix(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
