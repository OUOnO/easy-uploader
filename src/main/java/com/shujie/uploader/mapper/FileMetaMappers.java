package com.shujie.uploader.mapper;

import com.shujie.uploader.entity.FileMeta;
import org.springframework.jdbc.core.RowMapper;


/**
 * <p> description: TODO
 * <p> 2019/01/19
 *
 * @author ssj
 * @version 1.0.0
 */
public class FileMetaMappers {
    public static final RowMapper<FileMeta> TOTAL = (resultSet, rowNum) -> {
        FileMeta fileMeta = new FileMeta();
        fileMeta.setId(resultSet.getLong("ID"));
        fileMeta.setPath(resultSet.getString("PATH"));
        fileMeta.setCode(resultSet.getString("CODE"));
        fileMeta.setFileName(resultSet.getString("FILE_NAME"));
        fileMeta.setDownloadUrl(resultSet.getString("DOWNLOAD_URL"));
        fileMeta.setCreateDate(resultSet.getDate("CREATE_DATE"));
        return fileMeta;
    };
}
