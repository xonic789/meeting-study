package study.devmeetingstudy.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.devmeetingstudy.domain.study.Study;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class StudyFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_file_id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "study_id", columnDefinition = "comment '다대일 관계의 study foreign key'")
//    private Study study;


}