package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by e068805 on 10/25/2018.
 */
public class MultiplicationServiceImplTest {
    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks( this );
        multiplicationServiceImpl = new MultiplicationServiceImpl( randomGeneratorService );
    }

    @Test
    public void createRandomMultiplicationTest() {
        //given
        given( randomGeneratorService.generateRandomFactor() ).willReturn( 50, 30 );

        Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        assertThat( multiplication.getFactorA() ).isEqualTo( 50 );
        assertThat( multiplication.getFactorB() ).isEqualTo( 30 );
        assertThat( multiplication.getResult() ).isEqualTo( 1500 );
    }
}
