package chapter15;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

final class _3_ATM_HOF {
  private _3_ATM_HOF() {
  }

  private record Customer(String name, BigDecimal balance) {
  }

  private static void describeCustomer(final Consumer<String> printer, final Customer customer) {
    printer.accept("Welcome, %s!%n".formatted(customer.name()));
    if (customer.balance().compareTo(BigDecimal.ZERO) < 0) {
      printer.accept("You owe $%s.%n".formatted(customer.balance().abs()));
    } else {
      printer.accept("You have a positive balance of $%s.%n".formatted(customer.balance()));
    }
  }

  private static void console(final String text) {
    System.out.print(text);
  }

  private static void file(final String name, final String text) {
    try {
      Files.writeString(Path.of(name), text);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    final var customer = new Customer("John Doe", new BigDecimal("100.00"));
    describeCustomer(_3_ATM_HOF::console, customer);
    describeCustomer(text -> file("file.txt", text), customer);
  }
}
