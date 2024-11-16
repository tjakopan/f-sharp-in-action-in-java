package chapter15;

import java.math.BigDecimal;

final class _1_ATM_Initial {
  private _1_ATM_Initial() {
  }

  private record Customer(String name, BigDecimal balance) {
  }

  private static void describeCustomer(final Customer customer) {
    System.out.printf("Welcome, %s!%n", customer.name());
    if (customer.balance().compareTo(BigDecimal.ZERO) < 0) {
      System.out.printf("You owe $%s.%n", customer.balance().abs());
    } else {
      System.out.printf("You have a positive balance of $%s.%n", customer.balance());
    }
  }
}
