package microservices.book.multiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservices.book.multiplication.controller.MultiplicationResultAttemptController.ResultResponse;
import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.service.MultiplicationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by Marcelo Carvalho on 10/25/2018.
 */
@RunWith( SpringRunner.class )
@WebMvcTest( MultiplicationResultAttemptController.class )
public class MultiplicationResultAttemptControllerTest {

    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<MultiplicationResultAttempt> jsonResult;

    @Before
    public void setup() {
        JacksonTester.initFields( this, new ObjectMapper() );
    }

    @Test
    public void postResultReturnCorrect() throws Exception {
        genericParameterizeTest( true );
    }

    private void genericParameterizeTest( final boolean correct ) throws Exception {
        //Given
        given( multiplicationService.checkAttempt( any( MultiplicationResultAttempt.class ) ) ).willReturn( correct );
        User user = new User( "John" );
        Multiplication multiplication = new Multiplication( 50, 70 );
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt( user, multiplication, 3500, correct );

        //When
        MockHttpServletResponse response = mvc.perform( post( "/results" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( jsonResult.write( attempt )
                        .getJson() ) )
                .andReturn()
                .getResponse();

        //then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.OK.value() );
        assertThat( response.getContentAsString() ).isEqualTo( jsonResult.write( new MultiplicationResultAttempt(attempt.getUser(), attempt.getMultiplication(), attempt.getResultAttempt(), correct) ).getJson() );
    }

    @Test
    public void postResultReturnNotCorrect() throws Exception {
        genericParameterizeTest( false );
    }
}
