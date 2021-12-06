package study.devmeetingstudy.service.interfaces;

import study.devmeetingstudy.domain.study.Study;
import study.devmeetingstudy.domain.study.StudyFile;

import java.util.List;
import java.util.Map;

public interface StudyFileService {

    String DEFAULT_IMAGE_URL = "https://dev-meeting-study-bucket.s3.ap-northeast-2.amazonaws.com/subject/";

    StudyFile saveStudyFile(Study study, Map<String, String> uploadFileInfo);

    List<StudyFile> getStudyFileByStudyId(Long studyId);

    StudyFile getStudyFileById(Long studyFileId);

    StudyFile replaceStudyFile(Map<String, String> upload, StudyFile studyFile);

    Map<String, String> getDefaultFile(String subjectName);
}
