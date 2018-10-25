package microservices.book.multiplication.service;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by e068805 on 10/25/2018.
 */
@Service
public class RandomGeneratorServiceImpl implements RandomGeneratorService {

    static final int MINIMUM_FACTOR = 11;
    static final int MAXIMUM_FACTOR = 99;

    @Override
    public int generateRandomFactor() {
        return new Random().nextInt( (MAXIMUM_FACTOR - MINIMUM_FACTOR) + 1) + MINIMUM_FACTOR ;
    }
}
