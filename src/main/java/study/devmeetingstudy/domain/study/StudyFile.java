package study.devmeetingstudy.domain.study;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.devmeetingstudy.common.uploader.Uploader;
import study.devmeetingstudy.domain.study.Study;

import javax.persistence.*;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
public class StudyFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @Column(length = 100, name = "study_file_name")
    private String name;

    @Column(length = 300,name = "file_path")
    private String path;

    @Builder
    public StudyFile(Long id, Study study, String name, String path) {
        this.id = id;
        this.study = study;
        this.name = name;
        this.path = path;
    }

    public static StudyFile create(Study study, Map<String, String> uploadFileInfo){
        return StudyFile.builder()
                .study(study)
                .name(uploadFileInfo.get(Uploader.FILE_NAME))
                .path(uploadFileInfo.get(Uploader.UPLOAD_URL))
                .build();
    }

    public static StudyFile replace(StudyFile studyFile, Map<String, String> uploadFileInfo) {
        studyFile.name = uploadFileInfo.get(Uploader.FILE_NAME);
        studyFile.path = uploadFileInfo.get(Uploader.UPLOAD_URL);
        return studyFile;
    }
}
