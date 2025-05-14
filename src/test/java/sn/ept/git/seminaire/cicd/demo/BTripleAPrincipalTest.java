package sn.ept.git.seminaire.cicd.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class BTripleAPrincipalTest {

    private static ICalculator calculator;
    double a,b;

    @BeforeAll
    static void beforeAll(){
        calculator = new Calculator();
    }


    @BeforeEach
     void beforeEach(){
         a=100;
         b=200;
         log.info(" one");
    }

    @Test
    void shouldRespectTripleAPrinciple() {

        //arrange; given
        a = 22;
        b = 44;
        double expected=a+b;

        //act; when
        double result = calculator.add(a,b);

        //assert; then
        assertThat(result).isEqualTo(expected);
    }

}