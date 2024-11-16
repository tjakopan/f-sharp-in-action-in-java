package chapter15;

import common.Pair;
import common.Result;

import java.time.LocalDate;
import java.util.List;

import static common.Result.ok;
import static common.Result.result;

final class _8_Order_DependencyRejection {
  private _8_Order_DependencyRejection() {
  }

  private record AuthToken(String value) {
  }

  private record Order(int customerId) {
  }

  private record ValidatedOrder(Order order) {
  }

  private record DispatchedOrder(LocalDate dispatchDate, String status) {
  }

  private record SqlConnectionString(String value) {
  }

  private record Customer(int customerId, String name) {
  }

  @FunctionalInterface
  private interface LogMsg {
    void log(String msg);
  }

  private static final class IO {
    private IO() {
    }

    static <E> Result<DispatchedOrder, E> dispatchOrder(final AuthToken authToken, final ValidatedOrder order) {
      return ok(new DispatchedOrder(LocalDate.now().plusDays(2), "Dispatched"));
    }

    static <E> Result<Customer, E> loadCustomer(final SqlConnectionString connectionString, final int customerId) {
      return ok(new Customer(customerId, "John Doe"));
    }
  }

  private static final class BusinessLogic {
    private BusinessLogic() {
    }

    static <E> Pair<List<String>, Result<ValidatedOrder, E>> validateOrder(final Order order, final Customer customer) {
      return new Pair<>(List.of("Checking customer balance...", "Validated order!"), ok(new ValidatedOrder(order)));
    }

    static <E> Result<DispatchedOrder, E> processOrder(final Order order) {
      return result(scope -> {
        final var authToken = new AuthToken(System.getenv("AUTH_TOKEN"));
        final var connectionString = new SqlConnectionString(System.getenv("CONNECTION_STRING"));
        final LogMsg logger = System.out::println;

        logger.log("Loading customer from DB...");
        final var customer = scope.bind(IO.loadCustomer(connectionString, order.customerId));
        final Pair<List<String>, Result<ValidatedOrder, E>> messagesAndValidationResult =
            validateOrder(order, customer);

        for (final String message : messagesAndValidationResult.first()) {
          logger.log(message);
        }

        final ValidatedOrder validatedOrder = scope.bind(messagesAndValidationResult.second());
        return scope.bind(IO.dispatchOrder(authToken, validatedOrder));
      });
    }
  }
}
