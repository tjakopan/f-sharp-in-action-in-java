package chapter15;

import common.Result;

import java.time.LocalDate;

import static common.Result.error;
import static common.Result.ok;

final class _6_Order_Initial {
  private _6_Order_Initial() {
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

    static <E> Result<ValidatedOrder, E> validateOrder(final LogMsg logMsg,
                                                       final SqlConnectionString connectionString, final Order order) {
      logMsg.log("Loading customer from DB...");
      final var customer = IO.loadCustomer(connectionString, order.customerId);
      logMsg.log("Validated order!");
      return ok(new ValidatedOrder(order));
    }

    static <E> Result<DispatchedOrder, E> processOrder(final LogMsg logMsg, final AuthToken authToken,
                                                       final SqlConnectionString connectionString, final Order order) {
      final Result<ValidatedOrder, E> validatedOrder = validateOrder(logMsg, connectionString, order);
      return switch (validatedOrder) {
        case Result.Ok(var vo) -> IO.dispatchOrder(authToken, vo);
        case Result.Error<ValidatedOrder, E>(var e) -> error(e);
      };
    }
  }
}
