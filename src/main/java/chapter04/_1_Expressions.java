package chapter04;

import java.time.LocalDateTime;

final class _1_Expressions {
  private _1_Expressions() {
  }

  static String ageDescription(final int age) {
    return age < 18
        ? "Child"
        : age < 65
        ? "Adult"
        : "OAP";
  }

  static String describeAge(final int age) {
    final String ageDescription = ageDescription(age);
    final var greeting = "Hello";
    return greeting + "! You are a '" + ageDescription + "'.";
  }

  static void printAddition(final int a, final int b) {
    final var answer = a + b;
    System.out.printf("%d plus %d equals %d%n", a, b, answer);
  }

  static LocalDateTime addDays(final int days) {
    final var newDays = LocalDateTime.now().plusDays(days);
    System.out.printf("You gave me %d days and I gave you %s", days, newDays);
    return newDays;
  }
}
