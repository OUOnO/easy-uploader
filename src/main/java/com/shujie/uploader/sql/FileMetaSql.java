package com.shujie.uploader.sql;

/**
 * <p> description: TODO
 * <p> 2019/01/19
 *
 * @author ssj
 * @version 1.0.0
 */
public class FileMetaSql {
    public static final String SELECT_ALL = "SELECT ID, PATH, CODE, FILE_NAME, DOWNLOAD_URL, CREATE_DATE FROM FILE_META ORDER BY CREATE_DATE DESC";
    public static final String INSERT_ALL = "INSERT INTO FILE_META (ID, PATH, CODE, FILE_NAME, DOWNLOAD_URL, CREATE_DATE) VALUES (?,?,?,?,?,?)";
    public static final String SELECT_BY_ID = "SELECT ID, PATH, CODE, FILE_NAME, DOWNLOAD_URL, CREATE_DATE FROM FILE_META WHERE ID = ?";
}
