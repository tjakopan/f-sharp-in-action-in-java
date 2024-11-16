package chapter06;

import common.Pipe;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;

final class _1_Functions {
  private _1_Functions() {
  }

  static void foo(final int x, final int y) {
  }

  static int add(final int firstNumber, final int secondNumber) {
    return firstNumber + secondNumber;
  }

  static int addFive(final int number) {
    return add(5, number);
  }

  static int addTen(final int number) {
    return add(10, number);
  }

  static int multiply(final int firstNumber, final int secondNumber) {
    return firstNumber * secondNumber;
  }

  static int timesTwo(final int number) {
    return multiply(2, number);
  }

  static int addTenAndDouble(final int number) {
    return timesTwo(addTen(number));
  }

  static final Function<Integer, Function<Integer, Integer>> ADD = x -> y -> x + y;
  static final Function<Integer, Integer> ADD_FIVE = ADD.apply(5);
  static final Function<Integer, Integer> ADD_TEN = ADD.apply(10);

  static final Function<Integer, Function<Integer, Integer>> MULTIPLY = x -> y -> x * y;
  static final Function<Integer, Integer> TIMES_TWO = MULTIPLY.apply(2);
  static final Function<Integer, Integer> ADD_TEN_AND_DOUBLE = ADD_TEN.andThen(TIMES_TWO);

  enum LetterType {WELCOME, PAYMENT_OVERDUE}

  static <A, B> void sendEmail(final A customerAddress, final B letterType) {
  }

  static void sendEmailToFred(final LetterType letterType) {
    sendEmail("fred@email.com", letterType);
  }

  static final Function<String, Consumer<LetterType>> SEND_EMAIL = customerAddress -> letterType -> {
  };

  static final Consumer<LetterType> SEND_EMAIL_TO_FRED = SEND_EMAIL.apply("fred@email.com");

  static <A, B, C, D> void sendEmailManyArgs(final A sender, final B letterType, final C emailAddress,
                                             final D postDate) {
  }

  static void sendOfficeWelcome(final String emailAddress, final LocalDateTime postDate) {
    sendEmailManyArgs("office@email.com", LetterType.WELCOME, emailAddress, postDate);
  }

  static final Function<String, Function<LetterType, Function<String, Consumer<LocalDateTime>>>> SEND_EMAIL_MANY_ARGS =
      sender -> letterType -> emailAddress -> postDate -> {
      };

  static final Function<String, Consumer<LocalDateTime>> SEND_OFFICE_WELCOME =
      emailAddress -> postDate -> SEND_EMAIL_MANY_ARGS.apply("office@email.com").apply(LetterType.WELCOME);

  record CustomerId() {
  }

  record Customer() {
  }

  record Email() {
  }

  static Customer loadFromDb(final CustomerId id) {
    return new Customer();
  }

  static Customer reviewCustomer(final Customer customer) {
    return customer;
  }

  enum EmailTemplate {OVERDUE, WELCOME}

  static Email crateEmail(final EmailTemplate template, final Customer customer) {
    return new Email();
  }

  static void send(final Email email) {
  }

  static double drive(final int distance, final double gas) {
    return distance > 50
        ? gas / 2.0
        : distance > 25
        ? gas - 10.0
        : distance > 0
        ? gas - 1.0
        : gas;
  }

  static <T> Function<Void, T> functionOf(final T value) {
    return v -> value;
  }

  public static void main(String[] args) {
    final var fifteen = addFive(10);
    final var fifteen2 = ADD_FIVE.apply(10);

    sendEmailToFred(LetterType.WELCOME);
    sendEmailToFred(LetterType.PAYMENT_OVERDUE);

    SEND_EMAIL_TO_FRED.accept(LetterType.WELCOME);
    SEND_EMAIL_TO_FRED.accept(LetterType.PAYMENT_OVERDUE);

    sendOfficeWelcome("fred@email.com", LocalDateTime.now());
    sendOfficeWelcome("joanne@email.com", LocalDateTime.now().plusDays(1));

    SEND_OFFICE_WELCOME.apply("fred@email.com").accept(LocalDateTime.now());
    SEND_OFFICE_WELCOME.apply("joanne@email.com").accept(LocalDateTime.now().plusDays(1));

    final var firstValue = add(5, 10);
    final var secondValue = add(firstValue, 7);
    final var finalValue = multiply(secondValue, 2);
    final var finalValueChained = multiply(add(add(5, 10), 7), 2);
    final var pipelineCalc = Pipe.of(10)
        .pipe(i -> add(i, 5))
        .pipe(i -> add(i, 7))
        .pipe(i -> multiply(i, 2))
        .get();
    System.out.println(pipelineCalc);

    final var firstValueF = ADD.apply(5).apply(10);
    final var secondValueF = ADD.apply(firstValueF).apply(7);
    final var finalValueF = MULTIPLY.apply(secondValueF).apply(2);
    final var finalValueChainedF = MULTIPLY.apply(ADD.apply(ADD.apply(5).apply(10)).apply(7)).apply(2);
    final var pipelineCalcF = Pipe.of(10)
        .pipe(i -> ADD.apply(i).apply(5))
        .pipe(i -> ADD.apply(i).apply(7))
        .pipe(i -> MULTIPLY.apply(i).apply(2))
        .get();
    System.out.println(pipelineCalcF);

    final var customerId = new CustomerId();
    Pipe.of(customerId)
        .pipe(_1_Functions::loadFromDb)
        .pipe(_1_Functions::reviewCustomer)
        .pipe(customer -> crateEmail(EmailTemplate.OVERDUE, customer))
        .get(_1_Functions::send);

    final var finalState = Pipe.of(100.0)
        .pipe(gas -> drive(55, gas))
        .pipe(gas -> drive(26, gas))
        .pipe(gas -> drive(1, gas))
        .get();
  }
}
