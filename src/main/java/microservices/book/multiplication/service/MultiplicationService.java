package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

/**
 * Created by Marcelo Carvalho on 10/25/2018.
 */
public interface MultiplicationService {
    /**
     * Create a {@link Multiplication} object with two randomly-generated factors
     * beetween 11 and 99.
     *
     * @return a {@link Multiplication} object with random factors
     */
    Multiplication createRandomMultiplication();

    /**
     * @return true if the attempt matches the result of the multiplication, false otherwise
     */
    boolean checkAttempt( final MultiplicationResultAttempt resultAttempt);
}
