package study.devmeetingstudy.repository.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.devmeetingstudy.domain.Member;
import study.devmeetingstudy.domain.Message;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Message> findMessagesDesc(Member member) {
        return em.createQuery("select ms from Message ms where ms.member.id = :memberId order by ms.id desc", Message.class)
                .setParameter("memberId",member.getId())
                .getResultList();
    }
}