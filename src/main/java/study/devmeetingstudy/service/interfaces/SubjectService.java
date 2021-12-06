package study.devmeetingstudy.service.interfaces;

import study.devmeetingstudy.common.exception.global.error.exception.notfound.SubjectNotFoundException;
import study.devmeetingstudy.domain.Subject;
import study.devmeetingstudy.dto.subject.SubjectReqDto;

import java.util.List;

public interface SubjectService {

    Subject saveSubject(SubjectReqDto subjectReqDto);

    List<Subject> getSubjects();

    Subject getSubjectById(Long subjectId);
}
