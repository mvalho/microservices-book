package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

/**
 * Created by Marcelo Carvalho on 10/25/2018.
 */
public class MultiplicationServiceImplTest {
    private static final String JOHN_DOE = "john_doe";
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
        User user = new User(JOHN_DOE);

        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt( user, multiplication, 3000 );
        attempt.setCorrect(false);

        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user, multiplication, 3000);
        verifiedAttempt.setCorrect(true);

        given(userRepository.findByAlias(JOHN_DOE)).willReturn(Optional.empty());

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
        User user = new User(JOHN_DOE);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt( user, multiplication, 3010 );
        attempt.setCorrect(false);

        given(userRepository.findByAlias(JOHN_DOE)).willReturn(Optional.empty());

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt( attempt );

        //then
        assertThat( attemptResult ).isFalse();
        then(attemptRepository).should().save(attempt);
    }

    @Test
    public void checkMultiplicationAttemptAlreadyExistTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 30 );
        User user = new User(JOHN_DOE);

        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt( user, multiplication, 3010 );
        attempt.setCorrect(false);

        MultiplicationResultAttempt existedAttempt = new MultiplicationResultAttempt( user, multiplication, 3010 );
        existedAttempt.setCorrect(true);

        given(userRepository.findByAlias(JOHN_DOE)).willReturn(Optional.empty());
        given(attemptRepository.findByUser_AliasAndMultiplication_FactorAAndMultiplication_FactorB(JOHN_DOE, 50, 30)).willReturn(Optional.of(existedAttempt));

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        //then
        assertThat( attemptResult ).isFalse();
        then(attemptRepository).should().findByUser_AliasAndMultiplication_FactorAAndMultiplication_FactorB(JOHN_DOE, 50, 30);
        then(attemptRepository).should().save(attempt);
    }

    @Test
    public void retrieveStatsTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User(JOHN_DOE);
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user, multiplication, 3010);
        attempt1.setCorrect(false);

        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user, multiplication, 3051);
        attempt1.setCorrect(false);

        List<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);

        given(userRepository.findByAlias(JOHN_DOE)).willReturn(Optional.empty());
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc(JOHN_DOE)).willReturn(latestAttempts);

        //when
        List<MultiplicationResultAttempt> latestAttemptsResult = multiplicationServiceImpl.getStatsForUser(JOHN_DOE);

        //then
        assertThat(latestAttemptsResult).isEqualTo(latestAttempts);
    }
}
