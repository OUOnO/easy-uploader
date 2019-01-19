package com.shujie.uploader.entity;

import lombok.Data;

import java.util.Date;

/**
 * <p> description: TODO
 * <p> 2019/01/19
 *
 * @author ssj
 * @version 1.0.0
 */
@Data
public class FileMeta {
    private Long id;
    private String path;
    private String code;
    private String fileName;
    private String downloadUrl;
    private Date createDate;
}