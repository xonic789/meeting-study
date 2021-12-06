package study.devmeetingstudy.repository.email;

import study.devmeetingstudy.domain.Email;

import java.util.Optional;

public interface EmailRepositoryCustom {

    Optional<Email> findByEmail(String email);
}
