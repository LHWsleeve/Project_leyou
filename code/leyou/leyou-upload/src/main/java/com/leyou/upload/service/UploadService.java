package com.leyou.upload.service;

import com.leyou.upload.controller.UpController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {
    private final static List<String> CONTENT_TYPE = Arrays.asList("image/gif", "image/jpeg");
    private static final Logger LOGGER = LoggerFactory.getLogger(UpController.class);//日志

    public String uploadImage(MultipartFile file){
        //校验
        //1.文件类型
        String originalFilename = file.getOriginalFilename();//获取文件全名，包括后缀.可以使用切割判断后缀
        String contentType = file.getContentType();
        if (!CONTENT_TYPE.contains(contentType)) {
            LOGGER.info("文件上传失败：{}，文件类型不合法", originalFilename);
            return null;
        }
        try {
            //2.校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage==null){
                LOGGER.info("文件上传失败：{}，文件内容不合法", originalFilename);
                return null;
            }
            //3.上传
            file.transferTo(new File("C:\\Users\\liuha\\Documents\\GitHub\\Project_leyou\\images\\"+originalFilename));
            //4.返回url
            return "http://image.leyou.com/"+originalFilename ;
        } catch (IOException e) {
            LOGGER.info("文件上传失败：{}，服务器异常", originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
