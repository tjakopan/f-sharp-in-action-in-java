package chapter15;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

final class _4_ATM_DependencyRejection {
  private _4_ATM_DependencyRejection() {
  }

  private record Customer(String name, BigDecimal balance) {
  }

  private static String describeCustomer(final Customer customer) {
    final var output = new StringBuilder();
    output.append("Welcome, %s!%n".formatted(customer.name()));
    if (customer.balance().compareTo(BigDecimal.ZERO) < 0) {
      output.append("You owe $%s.%n".formatted(customer.balance().abs()));
    } else {
      output.append("You have a positive balance of $%s.%n".formatted(customer.balance()));
    }
    return output.toString();
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

  private static void describeToConsole(final Customer customer) {
    console(describeCustomer(customer));
  }

  private static void describeToFile(final String name, final Customer customer) {
    file(name, describeCustomer(customer));
  }

  public static void main(String[] args) {
    final var customer = new Customer("John Doe", new BigDecimal("100.00"));
    describeToConsole(customer);
    describeToFile("file.txt", customer);
  }
}
