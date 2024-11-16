package chapter03;

import java.util.function.BinaryOperator;

import static common.Run.run;

final class Chapter03 {
  private Chapter03() {
  }

  String exercise31(final int a, final int b, final int c) {
    final var inProgress = a + b;
    final var answer = inProgress * c;
    return "The answer is " + answer;
  }

  String greetingTextScoped() {
    final var fullName = run(() -> {
      final var fname = "Frank";
      final var sname = "Shmidt";
      return fname + " " + sname;
    });
    return "Greetings, " + fullName;
  }

  String greetingTextWithFunction(final String person) {
    final BinaryOperator<String> makeFullName = (fname, sname) -> fname + " " + sname;
    final String fullName = makeFullName.apply("Frank", "Schmidt");
    return "Greetings " + fullName + " from " + person;
  }

  String greetingTextWithFunctionScoped() {
    final var city = "London";
    final BinaryOperator<String> makeFullName = (fname, sname) -> fname + " " + sname + " from " + city;
    final var fullName = makeFullName.apply("Frank", "Schmidt");
    //final var surnameCity = sname + " from " + city; // won't compile
    return "Greeting, " + fullName;
  }
}
