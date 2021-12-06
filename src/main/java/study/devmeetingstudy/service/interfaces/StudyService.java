package study.devmeetingstudy.service.interfaces;

import org.springframework.transaction.annotation.Transactional;
import study.devmeetingstudy.common.exception.global.error.exception.notfound.StudyNotFoundException;
import study.devmeetingstudy.domain.enums.DeletionStatus;
import study.devmeetingstudy.domain.study.Study;
import study.devmeetingstudy.dto.study.request.StudySearchCondition;
import study.devmeetingstudy.vo.StudyReplaceVO;
import study.devmeetingstudy.vo.StudySaveVO;

import java.util.List;

public interface StudyService {

    public Study saveStudy(StudySaveVO studySaveVO);

    public List<Study> getStudiesByStudySearchCondition(StudySearchCondition studySearchCondition);

    public Study getStudyFetchJoinById(Long studyId);

    public Study getStudyById(Long studyId);

    public boolean existsStudyByStudyId(Long studyId);

    public Study replaceStudy(StudyReplaceVO studyReplaceVO, Study foundStudy);

    public void deleteStudyById(Study foundStudy);
}
