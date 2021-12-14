package study.devmeetingstudy.controller;

import org.apache.tomcat.jni.Local;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;
import study.devmeetingstudy.annotation.handlerMethod.MemberDecodeResolver;
import study.devmeetingstudy.common.uploader.LocalUploader;
import study.devmeetingstudy.common.uploader.Uploader;
import study.devmeetingstudy.domain.enums.DomainType;
import study.devmeetingstudy.domain.member.Member;
import study.devmeetingstudy.dto.token.TokenDto;
import study.devmeetingstudy.jwt.TokenProvider;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;


@ExtendWith(MockitoExtension.class)
class FilesControllerTest {

  @InjectMocks
  private FilesController filesController;

  private MockMvc mockMvc;

  @BeforeEach
  void init(){
    mockMvc = MockMvcBuilders.standaloneSetup(filesController).build();
  }

  @DisplayName("getFile")
  @Test
  void getFile() throws Exception {
    //given
    MockMultipartFile mockFile = new MockMultipartFile("file", "image-1.png", "image/png", "<<png data>>".getBytes(StandardCharsets.UTF_8));
    LocalUploader uploader = new LocalUploader();
    Map<String, String> upload = uploader.upload(mockFile, DomainType.STUDY);

    //when
    final ResultActions resultActions = mockMvc.perform(get("/api/files/" + upload.get(Uploader.FILE_NAME)));
    uploader.removeFile(new File("./images/" + upload.get(Uploader.FILE_NAME)));

    String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
    //then
    assertEquals("<<png data>>", contentAsString);
  }
}