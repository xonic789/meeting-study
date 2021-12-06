package study.devmeetingstudy.service.interfaces;

import org.springframework.transaction.annotation.Transactional;
import study.devmeetingstudy.common.exception.global.error.exception.notfound.MessageNotFoundException;
import study.devmeetingstudy.domain.enums.DeletionStatus;
import study.devmeetingstudy.domain.member.Member;
import study.devmeetingstudy.domain.message.Message;
import study.devmeetingstudy.domain.message.enums.MessageReadStatus;
import study.devmeetingstudy.vo.MessageVO;

import java.util.List;

public interface MessageService {

    Message sendMessage(MessageVO messageVO);

    Message getMessage(Long id);

    Message readMessage(Message message);

    List<Message> getMessages(Member member);

    void deleteMessage(Message message);

}
