package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

/**
 * Created by Marcelo Carvalho on 10/25/2018.
 */
public class MultiplicationServiceImplTest {
    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks( this );
        multiplicationServiceImpl = new MultiplicationServiceImpl( randomGeneratorService, attemptRepository, userRepository );
    }

    @Test
    public void createRandomMultiplicationTest() {
        //given
        given( randomGeneratorService.generateRandomFactor() ).willReturn( 50, 30 );

        Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        assertThat( multiplication.getFactorA() ).isEqualTo( 50 );
        assertThat( multiplication.getFactorB() ).isEqualTo( 30 );
    }

    @Test
    public void checkCorrectAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication( 50, 60 );
        User user = new User( "john_doe" );
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt( user, multiplication, 3000, false );
        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user, multiplication, 3000, true);

        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt( attempt );

        //then
        assertThat( attemptResult ).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
    }

    @Test
    public void checkWrongAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication( 50, 60 );
        User user = new User( "john_doe" );
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt( user, multiplication, 3010, false );

        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt( attempt );

        //then
        assertThat( attemptResult ).isFalse();
        then(attemptRepository).should().save(attempt);
    }
}
