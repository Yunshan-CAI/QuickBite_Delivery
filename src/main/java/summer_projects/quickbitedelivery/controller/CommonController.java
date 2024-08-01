package summer_projects.quickbitedelivery.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import summer_projects.quickbitedelivery.common.R;

import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
/**
 * upload the files
 */
public class CommonController {

    @Value("${quickbite.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //file is temporary,it needs to be saved elsewhere or it'll be deleted
        log.info(file.toString());

        //regenerate filename by UUID
        String fileName = UUID.randomUUID().toString() + ".jpg";

        //create a directory object
        File dir = new File(basePath);

        //if the directory doesn't exist, create one
        if (!dir.exists()) {
            dir.mkdir();
        }

        //restore the file into the assigned directory
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            //read the file by input stream
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //read the file back to the browser by output stream and show the pic in the browser
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            //close the resources
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //8f78598a-c6f0-44e8-9487-b2f33e92656f.jpg
        //8f78598a-c6f0-44e8-9487-b2f33e92656f.jpg

    }
}
