package chapter15;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

final class _2_ATM_OO {
  private _2_ATM_OO() {
  }

  private record Customer(String name, BigDecimal balance) {
  }

  private interface IPrinter {
    void print(String text);
  }

  private static final IPrinter TO_CONSOLE = text -> System.out.printf("%s", text);

  private static IPrinter toConsole() {
    return TO_CONSOLE;
  }

  private static IPrinter toFile(final String filename) {
    return text -> {
      try {
        Files.writeString(Path.of(filename), text);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }

  private record Atm(Customer customer) {
    void describeCustomer(final IPrinter printer) {
      printer.print("Welcome, %s!%n".formatted(customer.name));
      if (customer.balance().compareTo(BigDecimal.ZERO) < 0) {
        printer.print("You owe $%s.%n".formatted(customer.balance().abs()));
      } else {
        printer.print("You have a positive balance of %s.%n".formatted(customer.balance()));
      }
    }
  }
}
