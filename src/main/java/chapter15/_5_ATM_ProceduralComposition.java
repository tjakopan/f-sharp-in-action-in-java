package chapter15;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

final class _5_ATM_ProceduralComposition {
  private _5_ATM_ProceduralComposition() {
  }

  private record Customer(String name, BigDecimal balance) {
    String greet() {
      return "Welcome, %s!%n".formatted(name);
    }

    String sendMessage() {
      return balance.compareTo(BigDecimal.ZERO) < 0
          ? "You owe %s.%n".formatted(balance.abs())
          : "You have a positive balance of $%s.%n".formatted(balance);
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

  private static void describeToConsole(final Customer customer) {
    console(customer.greet());
    console(customer.sendMessage());
  }

  private static void describeToFile(final String name, final Customer customer) {
    file(name, customer.greet());
    file(name, customer.sendMessage());
  }

  public static void main(String[] args) {
    final var customer = new Customer("John Doe", new BigDecimal("100.00"));
    describeToConsole(customer);
    describeToFile("file.txt", customer);
  }
}
