package study.devmeetingstudy.repository.message;

import org.springframework.data.jpa.repository.JpaRepository;
import study.devmeetingstudy.domain.message.Message;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {


}
