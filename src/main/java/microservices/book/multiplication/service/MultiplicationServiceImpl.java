package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created by Marcelo Carvalho on 10/25/2018.
 */
@Service
public class MultiplicationServiceImpl implements MultiplicationService {
    private RandomGeneratorService randomGeneratorService;
    private MultiplicationResultAttemptRepository attemptRepository;
    private UserRepository userRepository;

    @Autowired
    public MultiplicationServiceImpl( final RandomGeneratorService randomGeneratorService, final MultiplicationResultAttemptRepository attemptRepository, final  UserRepository userRepository ) {
        this.randomGeneratorService = randomGeneratorService;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = this.randomGeneratorService.generateRandomFactor();
        int factorB = this.randomGeneratorService.generateRandomFactor();

        return new Multiplication( factorA, factorB );
    }

    @Transactional
    @Override
    public boolean checkAttempt( final MultiplicationResultAttempt resultAttempt ) {
        Optional<User> user = userRepository.findByAlias(resultAttempt.getUser().getAlias());
        Assert.isTrue(!resultAttempt.isCorrect(), "You can't send an attempt marked as correct!!");

        boolean isCorrect = resultAttempt.getResultAttempt() == resultAttempt.getMultiplication().getFactorA() * resultAttempt.getMultiplication().getFactorB();

        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(user.orElse(resultAttempt.getUser()), resultAttempt.getMultiplication(), resultAttempt.getResultAttempt(), isCorrect);

        attemptRepository.save(checkedAttempt);

        return isCorrect;
    }
}
