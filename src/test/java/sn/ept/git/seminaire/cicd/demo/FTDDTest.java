package sn.ept.git.seminaire.cicd.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.of;

@Slf4j
class FTDDTest {

    static Stream<Arguments> testData() {
        return Stream.of(
                of( List.of(1,2,0,3), 4), //2,0
                of(List.of(1,2,7,3), 4),
                of(List.of(1,2,9,3),4),
                of(List.of(1,2,11,3),4),
                of(List.of(1,2), 4),
                of(List.of(2),4),
                of(List.of(),0),
                of(List.of(-1),0),
                of(List.of(1,2,4,3),20),
                of(List.of(1,2,4,6),56),
                of(List.of(0,2,4,8,1,3),84),
                of(List.of(-1,-2,-4,-3),20),
                of(List.of(-1,-2,-4,-6),56),
                of(List.of(-0,-2,-4,-8,-1,-3),84)
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    void shouldReturnSumOfSquaresOfEvenNumbers(
            List<Integer> values, Integer result
    ) {
        assertThat(Validator.sumOfSquaresOfEvenNumbers(values)).isEqualTo(result);
    }
    //red *
    //greed
    //refactoring



    static Stream<Arguments> testData2() {
        return Stream.of(
                of( List.of(1,2,0,3),6),
                of( List.of(-1,2,0,3),6),
                of( List.of(1,-2,0,3),6),
                of( List.of(1,-2,0,-3),6),
                of( List.of(-1,-2,0,-3),6),
                of( List.of(1,1,1,-1,-1,-1),6),
                of( List.of(10,10,10,-10,-10,-10),60),
                of( List.of(0,0,0),0),
                of( List.of(),0)
        );
    }

    @ParameterizedTest
    @MethodSource("testData2")
    void shouldReturnSumOfAbsoluteValues(
            List<Integer> values, Integer result
    ) {
        assertThat(Validator. sumOfAbsoluteValues(values)).isEqualTo(result);
    }



}