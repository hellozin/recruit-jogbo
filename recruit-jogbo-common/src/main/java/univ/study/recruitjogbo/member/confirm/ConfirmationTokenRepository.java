package univ.study.recruitjogbo.member.confirm;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByUserEmail(String email);

    Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);

}
