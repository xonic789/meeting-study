package study.devmeetingstudy.service.interfaces;

import study.devmeetingstudy.domain.Address;
import study.devmeetingstudy.domain.study.Offline;
import study.devmeetingstudy.domain.study.Study;

public interface OfflineService {

    Offline saveOffline(Address address, Study study);

    Offline getOfflineById(Long offlineId);

    Offline getOfflineByStudyId(Long studyId);

    void deleteOffline(Offline offline);

    Offline replaceOffline(Address address, Offline foundOffline);
}
