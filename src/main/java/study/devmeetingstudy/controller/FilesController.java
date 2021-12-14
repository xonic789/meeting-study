package study.devmeetingstudy.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(tags = "파일")
@RestController
@RequestMapping("/api/files")
@Slf4j
@EnableCaching
public class FilesController {

  @GetMapping("/{fileName}")
  @ApiOperation(value = "파일 가져오기", notes = "파일을 요청합니다.")
  @Cacheable(value = "images", key = "#fileName")
  public ResponseEntity<Resource> getFile(@PathVariable String fileName) throws IOException {
    log.info("fineName = {}", fileName);
    Resource resource = new FileSystemResource("images/" + fileName);
    log.info("file path = {}", resource.getURL());
    log.info("file path = {}", resource.getURI());
    return ResponseEntity.ok(resource);
  }
}
