package com.shujie.uploader.controller;

import com.shujie.uploader.entity.FileMeta;
import com.shujie.uploader.mapper.FileMetaMappers;
import com.shujie.uploader.service.DesService;
import com.shujie.uploader.sql.FileMetaSql;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p> description: TODO
 * <p> 2019/01/19
 *
 * @author ssj
 * @version 1.0.0
 */
@Controller
public class UploadController {

    @Value("${uploader.savepath}")
    private String savePath;

    @Value("${uploader.url}")
    private String url;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DesService desService;

    @RequestMapping("/")
    public String toIndex() {
        return "index";
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<FileMeta> toList() {
        String sql = FileMetaSql.SELECT_ALL;
        return jdbcTemplate.query(sql, FileMetaMappers.TOTAL)
            .stream()
            .map(e -> {
                String downloadUrl = e.getDownloadUrl();
                FileMeta fileMeta = new FileMeta();
                BeanUtils.copyProperties(e, fileMeta);
                fileMeta.setDownloadUrl(url + downloadUrl);
                return fileMeta;
            })
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map upload(MultipartFile file_data) {
        String fileName = file_data.getOriginalFilename();
        String type = getType(fileName);

        String basePath = savePath + File.separator + type;
        String finalPath = getfinalPath(basePath);
        Random r = new Random();
        String saveFileName = String.valueOf(System.currentTimeMillis()) + (r.nextInt(900) + 100);

        File savePath = new File(finalPath);
        if (!savePath.exists()) {
            boolean b = savePath.mkdirs();
        }

        String finalFilePath = finalPath + File.separator + saveFileName;
        File saveFile = new File(finalFilePath);
        try {
            file_data.transferTo(saveFile);
            String downloadUrl = "/download?downloadUrl=" + URLEncoder.encode(desService.encode(saveFileName), "utf-8");
            String sql = FileMetaSql.INSERT_ALL;
            String code = UUID.randomUUID().toString().substring(0, 4);
            jdbcTemplate.update(sql, Long.valueOf(saveFileName), finalFilePath, code, fileName, downloadUrl, new Date());

            Map<String, Object> res = new HashMap<>();
            res.put("code", 200);
            res.put("download_url", url + downloadUrl);
            res.put("download_code", code);
            return res;
        } catch (IllegalBlockSizeException | NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IOException e) {
            e.printStackTrace();
        }

        Map<String, Object> res = new HashMap<>();
        res.put("code", 400);
        return res;

    }

    private String getfinalPath(String basePath) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        return basePath + File.separator +
            year + File.separator +
            month + File.separator +
            day;
    }

    private String getType(String fileName) {
        int indexofspliter = -1;
        if (!StringUtils.isEmpty(fileName)) {
            indexofspliter = fileName.lastIndexOf('.');
        }
        String type = "default";
        if (indexofspliter != -1 && indexofspliter != (fileName.length() - 1)) {
            type = fileName.substring(indexofspliter + 1);
        }
        return type;
    }

    @RequestMapping("/download")
    public String toDownload(String downloadUrl, Model model) {
        model.addAttribute("downloadUrl", downloadUrl);
        return "download";
    }

    @RequestMapping("/download/{pathId}/{code}")
    public void download(@PathVariable("pathId") String pathId, @PathVariable("code") String code, HttpServletResponse response) throws UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        FileMeta fileMeta = getFileMeta(pathId);

        if (fileMeta == null) {
            response.reset();
            try (PrintWriter writer = response.getWriter()) {
                writer.println("file not found!");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }

        if (!code.equals(fileMeta.getCode())) {
            response.reset();
            try (PrintWriter writer = response.getWriter()) {
                writer.println("illegal code!");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }

        String path = fileMeta.getPath();
        File file = new File(path);
        String filename = URLEncoder.encode(fileMeta.getFileName(), "utf-8");

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");
        response.setCharacterEncoding("utf-8");

        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[512];
            int len = inputStream.read(buffer);
            while (len != -1) {
                outputStream.write(buffer, 0, len);
                len = inputStream.read(buffer);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            response.reset();

            try (PrintWriter writer = response.getWriter()) {
                writer.println("File not found!");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private FileMeta getFileMeta(String pathId) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String sql = FileMetaSql.SELECT_BY_ID;
        String decode = desService.decode(pathId);
        try {
            Long id = Long.valueOf(decode);
            Object[] object = {id};
            return jdbcTemplate.queryForObject(sql, object, FileMetaMappers.TOTAL);
        } catch (Exception e) {
            return null;
        }

    }

}
