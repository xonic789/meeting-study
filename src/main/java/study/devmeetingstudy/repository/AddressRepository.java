package study.devmeetingstudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.devmeetingstudy.domain.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
