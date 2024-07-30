package summer_projects.quickbitedelivery.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import summer_projects.quickbitedelivery.common.R;

@RestController
@RequestMapping
@Slf4j
/**
 * upload the files
 */
public class CommonController {

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info(file.toString());
        return null;
    }

}
