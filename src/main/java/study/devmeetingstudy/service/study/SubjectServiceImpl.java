package study.devmeetingstudy.service.study;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.devmeetingstudy.common.exception.global.error.exception.notfound.SubjectNotFoundException;
import study.devmeetingstudy.domain.Subject;
import study.devmeetingstudy.dto.subject.SubjectReqDto;
import study.devmeetingstudy.repository.SubjectRepository;
import study.devmeetingstudy.service.interfaces.SubjectService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    @Transactional
    public Subject saveSubject(SubjectReqDto subjectReqDto) {
        Subject subject = Subject.create(subjectReqDto);
        return subjectRepository.save(subject);
    }

    public List<Subject> getSubjects() {
        return subjectRepository.findAll();
    }


    public Subject getSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("해당 id로 스터디 주제를 찾을 수 없습니다."));
    }
}
