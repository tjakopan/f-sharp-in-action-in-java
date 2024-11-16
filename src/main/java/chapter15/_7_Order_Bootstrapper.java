package chapter15;

import common.Result;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.IntFunction;

import static common.Result.error;
import static common.Result.ok;

final class _7_Order_Bootstrapper {
  private _7_Order_Bootstrapper() {
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
                                                       final IntFunction<Result<Customer, E>> loadCustomer,
                                                       final Order order) {
      logMsg.log("Loading customer from DB...");
      final var customer = loadCustomer.apply(order.customerId);
      logMsg.log("Validated order!");
      return ok(new ValidatedOrder(order));
    }

    static <E> Result<DispatchedOrder, E> processOrder(final Function<Order, Result<ValidatedOrder, E>> validateOrder,
                                                       final Function<ValidatedOrder, Result<DispatchedOrder, E>> dispatchOrder,
                                                       final Order order) {
      final var validatedOrder = validateOrder.apply(order);
      return switch (validatedOrder) {
        case Result.Ok(var vo) -> dispatchOrder.apply(vo);
        case Result.Error<ValidatedOrder, E>(var e) -> error(e);
      };
    }
  }

  private static final class WireUp {
    private WireUp() {
    }

    static Result<DispatchedOrder, String> processOrder(final Order order) {
      final var authToken = new AuthToken(System.getenv("AUTH_TOKEN"));
      final var connectionString = new SqlConnectionString(System.getenv("CONNECTION_STRING"));
      final LogMsg logger = System.out::println;
      final Function<Order, Result<ValidatedOrder, String>> validateOrder = o -> {
        final IntFunction<Result<Customer, String>> loadCustomer =
            customerId -> IO.loadCustomer(connectionString, customerId);
        return BusinessLogic.validateOrder(logger, loadCustomer, o);
      };
      final Function<ValidatedOrder, Result<DispatchedOrder, String>> dispatchOrder =
          vo -> IO.dispatchOrder(authToken, vo);
      return BusinessLogic.processOrder(validateOrder, dispatchOrder, order);
    }
  }
}
