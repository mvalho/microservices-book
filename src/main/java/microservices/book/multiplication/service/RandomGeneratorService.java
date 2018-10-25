package microservices.book.multiplication.service;

/**
 * Created by e068805 on 10/25/2018.
 */
public interface RandomGeneratorService {
    /**
     * @return a randomly-generated factor. It's always a number between 11 and 99.
     */
    int generateRandomFactor();
}
