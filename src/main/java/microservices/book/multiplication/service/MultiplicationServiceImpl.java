package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Marcelo Carvalho on 10/25/2018.
 */
@Service
public class MultiplicationServiceImpl implements MultiplicationService {
    private RandomGeneratorService randomGeneratorService;

    @Autowired
    public MultiplicationServiceImpl( RandomGeneratorService randomGeneratorService ) {
        this.randomGeneratorService = randomGeneratorService;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = this.randomGeneratorService.generateRandomFactor();
        int factorB = this.randomGeneratorService.generateRandomFactor();

        return new Multiplication( factorA, factorB );
    }

    @Override
    public boolean checkAttempt( MultiplicationResultAttempt resultAttempt ) {
        return resultAttempt.getResultAttempt() == resultAttempt.getMultiplication().getFactorA() * resultAttempt.getMultiplication().getFactorB();
    }
}
