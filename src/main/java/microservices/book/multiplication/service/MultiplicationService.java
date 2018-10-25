package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;

/**
 * Created by e068805 on 10/25/2018.
 */
public interface MultiplicationService {
    /**
     * Create a {@link Multiplication} object with two randomly-generated factors
     * beetween 11 and 99.
     *
     * @return a {@link Multiplication} object with random factors
     */
    Multiplication createRandomMultiplication();
}
