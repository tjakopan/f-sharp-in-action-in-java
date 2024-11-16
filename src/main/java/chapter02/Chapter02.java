package chapter02;

import java.util.function.Function;
import java.util.stream.IntStream;

class Chapter02 {
  private Chapter02() {
  }

  IntStream getEvenNumbers(final IntStream numbers) {
    return numbers.filter(number -> number % 2 == 0);
  }

  IntStream squareNumbers(final IntStream numbers) {
    return numbers.map(x -> x * x);
  }

  IntStream getEvenNumbersThenSquare(final IntStream numbers) {
    return squareNumbers(getEvenNumbers(numbers));
  }

  static final Function<IntStream, IntStream> GET_EVEN_NUMBERS = numbers -> numbers.filter(number -> number % 2 == 0);
  static final Function<IntStream, IntStream> SQUARE_NUMBERS = numbers -> numbers.map(number -> number * number);
  static final Function<IntStream, IntStream> GET_EVEN_NUMBERS_THEN_SQUARE = GET_EVEN_NUMBERS.andThen(SQUARE_NUMBERS);
}
