package study.devmeetingstudy.common.uploader;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import study.devmeetingstudy.domain.enums.DomainType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
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
    return getFileInfo(uploadFile.getName(), uploadFile.getAbsolutePath());
  }

  private Optional<File> convert(MultipartFile multipartFile) throws IOException {
    File convertFile = new File(System.getProperty("user.dir") + "/" + UUID.randomUUID() + multipartFile.getOriginalFilename());
    if (convertFile.createNewFile()){
      try (FileOutputStream fos = new FileOutputStream(convertFile)) {
        fos.write(multipartFile.getBytes());
      }
      return Optional.of(convertFile);
    }
    return Optional.empty();
  }


  public Map<String, String> getFileInfo(String originalFileName, String uploadImageUrl) {
    Map<String, String> fileInfo = new ConcurrentHashMap<>();
    fileInfo.put(FILE_NAME, originalFileName);
    fileInfo.put(UPLOAD_URL, uploadImageUrl);
    return fileInfo;
  }
}
