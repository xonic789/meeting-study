package study.devmeetingstudy.common.uploader;

//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Slf4j
public class S3Uploader implements Uploader{

//    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

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
        return upload(uploadFile, domainType);
    }

    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());
        if (convertFile.createNewFile()){
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    private Map<String, String> upload(File uploadFile, DomainType domainType){
        String fileName = domainType.value() + "/" + UUID.randomUUID() + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);

        return getFileInfo(uploadFile.getName(), uploadImageUrl);
    }

    private Map<String, String> getFileInfo(String originalFileName, String uploadImageUrl) {
        Map<String, String> fileInfo = new ConcurrentHashMap<>();
        fileInfo.put(FILE_NAME, originalFileName);
        fileInfo.put(UPLOAD_URL, uploadImageUrl);
        return fileInfo;
    }

    private String putS3(File uploadFile, String fileName) {
//        amazonS3Client.putObject(
//                new PutObjectRequest(bucket, fileName, uploadFile)
//                        .withCannedAcl(CannedAccessControlList.PublicRead)
//        );
//        return amazonS3Client.getUrl(bucket, fileName).toString();
        return null;
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()){
            log.info("File delete success");
            return;
        }
        log.info("File delete failed");
    }

}