package microservices.book.multiplication.repository;

import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MultiplicationResultAttemptRepository extends CrudRepository<MultiplicationResultAttempt, Long> {
    List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias);
    Optional<MultiplicationResultAttempt> findByUser_AliasAndMultiplication_FactorAAndMultiplication_FactorB(String userAlias, int factorA, int factorB);
}
