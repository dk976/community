package com.kevin.community.controller;

import com.kevin.community.provider.TCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FileController {
    @Autowired
    private TCloudProvider ttCloudProvider;

    @RequestMapping("/file/upload")
    @ResponseBody

//    @RequestParam("file") MultipartFile file
    public Object upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        String name = null;
        try {
            name = ttCloudProvider.uploadFile2Cos(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String imgUrl = ttCloudProvider.getImgUrl(name);
        String[] split = imgUrl.split("\\.");
        return split[0];
    }
}
