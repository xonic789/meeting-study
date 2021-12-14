package study.devmeetingstudy.common.uploader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import study.devmeetingstudy.domain.enums.DomainType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class LocalUploader implements Uploader{

  @Override
  public List<Map<String, String>> uploadFiles(List<MultipartFile> multipartFiles, DomainType domainType) throws IOException {
    List<Map<String, String>> uploadImageUrls = new ArrayList<>();
    for (MultipartFile multipartFile : multipartFiles) {
      uploadImageUrls.add(upload(multipartFile, domainType));
    }
    return uploadImageUrls;
  }

  @Override
  public Map<String, String> upload(MultipartFile multipartFile, DomainType domainType) throws IOException {
    File uploadFile = convert(multipartFile)
            .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert error"));
    return getFileInfo(uploadFile);
  }

  private Optional<File> convert(MultipartFile multipartFile) throws IOException {
    File convertFile = new File(UPLOAD_PATH + UUID.randomUUID() + multipartFile.getOriginalFilename());
    log.info("convert File Path = {}", convertFile.getAbsolutePath());
    if (convertFile.createNewFile()){
      try (FileOutputStream fos = new FileOutputStream(convertFile)) {
        fos.write(multipartFile.getBytes());
      }
      return Optional.of(convertFile);
    }
    return Optional.empty();
  }


  private Map<String, String> getFileInfo(File file) {
    Map<String, String> fileInfo = new ConcurrentHashMap<>();
    fileInfo.put(FILE_NAME, file.getName());
    fileInfo.put(UPLOAD_URL, LOCAL_IMAGE_URL + file.getName());
    return fileInfo;
  }

  public void removeFile(File file) {
    if (file.delete()) {
      log.info("File delete success");
      return;
    }
    log.info("File delete failed");
  }

}
