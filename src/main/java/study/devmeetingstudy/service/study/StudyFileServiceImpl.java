package study.devmeetingstudy.service.study;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;
import study.devmeetingstudy.common.exception.global.error.exception.notfound.StudyFileNotFoundException;
import study.devmeetingstudy.common.uploader.Uploader;
import study.devmeetingstudy.domain.study.Study;
import study.devmeetingstudy.domain.study.StudyFile;
import study.devmeetingstudy.repository.StudyFileRepository;
import study.devmeetingstudy.service.interfaces.StudyFileService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StudyFileServiceImpl implements StudyFileService {

    private final StudyFileRepository studyFileRepository;


    @Transactional
    public StudyFile saveStudyFile(Study study, Map<String, String> uploadFileInfo) {
        StudyFile studyFile = StudyFile.create(study, uploadFileInfo);
        return studyFileRepository.save(studyFile);
    }

    public List<StudyFile> getStudyFileByStudyId(Long studyId) {
        return studyFileRepository.findFirstByStudy_Id(studyId);
    }

    public StudyFile getStudyFileById(Long studyFileId) {
        return studyFileRepository.findById(studyFileId).orElseThrow(() -> new StudyFileNotFoundException("해당 id로 스터디 파일을 찾을 수 없습니다."));
    }

    @Transactional
    public StudyFile replaceStudyFile(Map<String, String> upload, StudyFile studyFile) {
        return StudyFile.replace(studyFile, upload);
    }

    public Map<String, String> getDefaultFile(String subjectName) {
        String encodedSubjectName = UriUtils.encode(subjectName, "UTF-8");
        log.info("encodedSubjectName = {}", encodedSubjectName);
        log.info("path : {}", getClass().getResource("/static/"));
        String name = encodedSubjectName.toLowerCase();
        Map<String, String> fileInfo = new ConcurrentHashMap<>();
        fileInfo.put(Uploader.FILE_NAME, name + ".png");
        fileInfo.put(Uploader.UPLOAD_URL, getClass().getResource("/static/") + name + ".png");

        return fileInfo;
    }
}
