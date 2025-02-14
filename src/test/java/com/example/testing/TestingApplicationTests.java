package com.example.testing;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Slf4j
class TestingApplicationTests {


    @BeforeEach // execute before every test.
    void startTest(){
        log.info("Starting Tests.");
    }

    @AfterEach
    void endTest(){ // execute after every test.
        log.info("End Tests.");
    }

    @BeforeAll // execute before other tests and only once
    static void setUpStart(){ // static bcz it is a part of class (inside class test check and execute)
        log.info("setup start.");
    }

    @AfterAll // execute after other tests and only once
    static void setUpEnd(){
        log.info("setup end.");
    }

    @Test
//    @Disabled // not execute anymore
    void testNumberOne() {
        log.info("Test one is execute.");
    }

    @Test
//    @DisplayName("DisplayTwo") // display what you write inside DisplayName show in tests.
    void testNumberTwo() {
        log.info("Test two is execute.");
    }


//    create a test case for addTwoNumber method.
    @Test
    void checkOutput(){
        int a = 5;
        int b = 3;
        int res = addTwoNumber(a,b);
//        Assertions.assertEquals(8,res); // Assertions are use for to ensure that expected
        // output is same as we got


//       1> It is similar to JUnit, but it allows chaining of multiple methods seamlessly.
//       2> It is more intuitive than JUnit because, in AssertJ, methods are
//       context-aware and suggest type-specific methods right from the beginning, unlike JUnit.
        Assertions.assertThat(res)
                .isEqualTo(8)
                .isGreaterThan(7)
                .isCloseTo(6, Offset.offset(2));

        assertThat("Apple")
                .isEqualTo("Apple")
                .startsWith("App")
                .endsWith("le")
                .hasSize(5);

    }

    @Test
    void dividingByZero_whenDenominatorIsZero_trySomethingElse(){
        int a = 10;
        int b = 0;

        assertThatThrownBy(()->divide(a,b))
                .isInstanceOf(ArithmeticException.class)
                .hasMessage("tried to divided by zero");
    }

    int addTwoNumber(int a, int b){
        return a+b;
    }

    int divide(int a,int b){
        try{
            return a/b;
        }catch (ArithmeticException e){
            log.info("ArithmeticException occur..");
            throw new ArithmeticException("tried to divided by zero");
        }
    }

}
