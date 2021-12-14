package study.devmeetingstudy.common.uploader;


import org.springframework.web.multipart.MultipartFile;
import study.devmeetingstudy.domain.enums.DomainType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Uploader {

    String FILE_NAME = "fileName";
    String UPLOAD_URL = "uploadUrl";
    String LOCAL_IMAGE_URL = "http://api.dev-meeting-study.site/api/files/";
    String UPLOAD_PATH = "images/";


    Map<String, String> upload(MultipartFile multipartFile, DomainType domainType) throws IOException;
    List<Map<String, String>> uploadFiles(List<MultipartFile> multipartFiles, DomainType domainType) throws IOException;
}
