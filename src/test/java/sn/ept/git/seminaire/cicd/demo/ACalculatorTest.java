package sn.ept.git.seminaire.cicd.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sn.ept.git.seminaire.cicd.demo.exception.DivisionByZeroException;
import java.util.Random;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ACalculatorTest {

    private static ICalculator calculator;
    private  double a,b,result, expected;
    private static  Random r;


    @BeforeAll
    static void beforeAll(){
        calculator = new Calculator();
        r = new Random();
    }

    @BeforeEach
      void beforeEach(){
        a = 33;
        b = 44;
    }

    @Test
    void addShouldReturnCorrectResult() {
        //Arrange
        expected = a+b;

        //Act
        result = calculator.add(a,b);

        //Assert
        assertThat(result)
                .isPositive()
                .isEqualTo(expected);
    }

    @Test
    void substractShouldReturnCorrectResult() {
        //Arrange
        expected =a-b;
        result = calculator.subtract(a,b);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void multiplyShouldReturnCorrectResult() {
        expected = a*b;
        result = calculator.multiply(a,b);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void divideShouldReturnCorrectResult() throws DivisionByZeroException {
        expected =a/b;
        result = calculator.divide(a,b);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void divisionByZeroShouldThrowError() {
        b=0;
        Assertions.assertThrows(
                DivisionByZeroException.class,
                () -> calculator.divide(a, b)
        );
    }



    @RepeatedTest(value = 30)
    void testAddRepeated() {
        expected =a*2;
        result = calculator.add(a,a);
        assertThat(result).isEqualTo(expected);
    }



    @DisplayName("ICalculator: parameterized test for add method")

    @ParameterizedTest
    @MethodSource("addTestData")
    void addWithRandomInputsShouldReturnCorrectValue(double a, double b) {
        //arrange
        expected = a+b;
        System.out.println(a+","+b);


        //act
        double result = calculator.add(a,b);

        //assert
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> addTestData() {
       /* return IntStream
                .range(1,10)
                .mapToObj(item->Arguments.of(r.nextDouble(), r.nextDouble()));*/
        return   Stream.of(
                    Arguments.of(1, 10),
                    Arguments.of(28, 2),
                    Arguments.of(37, 3),
                    Arguments.of(40, 4),
                    Arguments.of(55, 5),
                    Arguments.of(6, 61)
        );
    }


    /**
     * Won't be run
     */
/*    @Disabled
    @Test
    void testDisabled() {
        assertThat(Boolean.TRUE).isTrue();
    }*/

}