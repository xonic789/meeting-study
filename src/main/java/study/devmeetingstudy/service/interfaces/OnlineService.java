package study.devmeetingstudy.service.interfaces;

import org.springframework.transaction.annotation.Transactional;
import study.devmeetingstudy.common.exception.global.error.exception.notfound.OnlineNotFoundException;
import study.devmeetingstudy.domain.study.Online;
import study.devmeetingstudy.domain.study.Study;
import study.devmeetingstudy.dto.study.request.StudyPutReqDto;
import study.devmeetingstudy.dto.study.request.StudySaveReqDto;

public interface OnlineService {

    Online saveOnline(StudySaveReqDto studySaveReqDto, Study study);

    Online getOnlineById(Long onlineId);

    Online getOnlineByStudyId(Long studyId);

    void deleteOnline(Online online);

    Online replaceOnline(StudyPutReqDto studyPutReqDto, Online foundOnline);
}
