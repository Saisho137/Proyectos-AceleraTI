package simpleexample;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class SimpleFunctionsTest {
    static SimpleFunctions simpleFunctions;

    @BeforeAll
    static void setup() {
        simpleFunctions = new SimpleFunctions();
    }

    @ParameterizedTest
    @Tag("Tag-SimpleFunctions")
    @DisplayName("")
    @CsvSource({
            "Carlos",
            "Ana",
            "Luis"
    })
    void testHelloHuman(String name) {
        String result = simpleFunctions.helloHuman(name);
        assertThat(result)
                .startsWith("Hola")
                .endsWith("!");
    }

    @ParameterizedTest
    @Tag("Tag-SimpleFunctions")
    @DisplayName("")
    @CsvSource({
            "2, true",
            "3, false",
            "0, true",
            "-4, true",
            "-5, false"
    })
    void testCheckEvenNumber(int number, boolean expected) {
        boolean result = simpleFunctions.checkEvenNumber(number);
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @Tag("Tag-SimpleFunctions")
    @DisplayName("")
    @CsvSource({
            "2020, true",
            "1900, false",
            "2000, true",
            "2021, false",
            "-4, false",
            "0, false"
    })
    void testIsLeapYear(int year, boolean expected) {
        boolean result = simpleFunctions.isLeapYear(year);
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @Tag("Tag-SimpleFunctions")
    @DisplayName("")
    @CsvSource(value = {
            "'hello', 'OLLEH'",
            "'World', 'DLROW'",
            "'', ''",
            "'Java', 'AVAJ'",
            "null, null"
    }, nullValues = {"null"})
    void testReverseAndUpper(String input, String expected) {
        String result = simpleFunctions.reverseAndUpper(input);
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @Tag("Tag-SimpleFunctions")
    @DisplayName("")
    @CsvSource({
            "50, 200, 25.0",
            "30, 150, 20.0",
            "0, 100, 0.0",
            "75, 300, 25.0"
    })
    void testCalculatePercentage_Success(double part, double total, double expected) {
        assertThat(simpleFunctions.calculatePercentage(part, total))
                .isCloseTo(Double.valueOf(expected), Offset.offset(0.001));
    }

    @ParameterizedTest
    @Tag("Tag-SimpleFunctions")
    @DisplayName("")
    @CsvSource({
            "50",
            "0"
    })
    void testCalculatePercentage_DivideByZero(int part) {
        int total = 0;
        assertThatThrownBy(() -> simpleFunctions.calculatePercentage(part, total))
                .isInstanceOf(ArithmeticException.class)
                .hasMessage("Total cannot be zero");
    }

    @ParameterizedTest
    @Tag("Tag-SimpleFunctions")
    @DisplayName("")
    @CsvSource({
            "20, *, 100, 2000",
            "15, +, 50, 65",
            "30, -, 10, 20",
            "10, /, 2, 5",
            "6, %, 2, 0"
    })
    void testCalculator_Success(double a, String operation, double b, double expected) {
        if (operation.equals("*") || operation.equals("/")) {
            double result = simpleFunctions.calculator(a, operation, b);
            assertThat(result).isCloseTo(Double.valueOf(expected), Offset.offset(0.001));
        } else {
            double result = simpleFunctions.calculator(a, operation, b);
            assertThat(result).isEqualTo(expected);
        }

    }

    @ParameterizedTest
    @Tag("Tag-SimpleFunctions")
    @DisplayName("")
    @CsvSource({
            "20, %",
            "15, /",
            "30, %",
            "10, /",
    })
    void testCalculator_DivideByZero(double a, String operation) {
        double denominator = 0;
        assertThatThrownBy(() -> simpleFunctions.calculator(a, operation, denominator))
                .isInstanceOf(ArithmeticException.class)
                .hasMessage("Denominator cannot be Zero");

    }

    @ParameterizedTest
    @Tag("Tag-SimpleFunctions")
    @DisplayName("Testing calculator without supported operations")
    @CsvSource({
            "20, (, 100",
            "15, x, 50",
            "30, t, 10",
            "10, y, 2",
            "6, &, 2"
    })
    void testCalculator_WrongOperations(double a, String operation, double b) {
        assertThatThrownBy(() -> simpleFunctions.calculator(a, operation, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Operation " + operation + " not supported");
    }

    @ParameterizedTest
    @Tag("Tag-SimpleFunctions")
    @DisplayName("Finding max value in an array")
    @MethodSource("provideArraysForFindMax")
    void testFindMax_Success(int[] numbers, int expectedMax) {
        int result = simpleFunctions.findMax(numbers);
        assertThat(result).isEqualTo(expectedMax);
    }

    private static Stream<Arguments> provideArraysForFindMax() {
        return Stream.of(
                Arguments.of(new int[]{1, 2, 3, 4, 5}, 5),
                Arguments.of(new int[]{-1, -2, -3, -4, -5}, -1),
                Arguments.of(new int[]{0, 0, 0, 0}, 0),
                Arguments.of(new int[]{10, 20, 30, 40, 50}, 50),
                Arguments.of(new int[]{5}, 5)
        );
    }

    @Test
    @Tag("Tag-SimpleFunctions")
    @DisplayName("Finding max value in an empty array should throw exception")
    void testFindMax_EmptyArray() {
        int[] emptyArray = new int[]{};
        assertThatThrownBy(() -> simpleFunctions.findMax(emptyArray))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Array must not be empty");

        int[] nullArray = null;
        assertThatThrownBy(() -> simpleFunctions.findMax(nullArray))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Array must not be empty");
    }

    @ParameterizedTest
    @Tag("Tag-SimpleFunctions")
    @DisplayName("Validating email formats")
    @CsvSource(value = {
            "santiago@hotmail.com, true",
            "a.a, false",
            /*"ana@gmail, false", !Regex incompleto */
            "a@a.a, true",
            "null, false"
    }, nullValues = {"null"})
    void testIsValidEmail(String email, boolean expected) {
        boolean result = simpleFunctions.isValidEmail(email);
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @Tag("Tag-SimpleFunctions")
    @DisplayName("Applying discounts based on quantity")
    @CsvSource({
            "100.0, 30, 2850.0",
            "100.0, 50, 4500.0",
            "100.0, 100, 8000.0",
            "200.0, 75, 13500.0",
            "150.0, 120, 14400.0",
            "200, 10, 1900.0",
            "-50.0, 10, 0.0",
            "100.0, -5, 0.0"
    })
    void testApplyDiscount(double price, int quantity, double expectedTotal) {
        double result = simpleFunctions.applyDiscount(price, quantity);
        assertThat(result).isCloseTo(Double.valueOf(expectedTotal), Offset.offset(0.001));
    }

    @ParameterizedTest
    @Tag("Tag-SimpleFunctions")
    @DisplayName("Filtering strings by a specific letter")
    @MethodSource("provideArraysForFilter")
    void testFilterVowels(List<String> input, char letter, List<String> expected) {
        List<String> result = simpleFunctions.filterByLetter(input, letter);
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> provideArraysForFilter() {
        return Stream.of(
                Arguments.of(List.of("Ana", "Alberto", "Beatriz", "Carlos"), 'A',
                        List.of("Ana", "Alberto")),
                Arguments.of(List.of("Maria", "Miguel", "Jose", "Juana"), 'M',
                        List.of("Maria", "Miguel")),
                Arguments.of(null, 'A',
                        List.of()),
                Arguments.of(List.of(), 'B',
                        List.of())
        );
    }
}
