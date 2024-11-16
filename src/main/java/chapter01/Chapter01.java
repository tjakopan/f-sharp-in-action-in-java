package chapter01;

import java.time.LocalDateTime;

class Chapter01 {
  private Chapter01() {
  }

  AnswerAndDate addTenThenDouble(final int theNumber) {
    final var answer = (theNumber + 10) * 2;
    return new AnswerAndDate(answer, LocalDateTime.now());
  }
}
