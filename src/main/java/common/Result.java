package common;

public sealed interface Result<T, E> {
  static <T, E> Result<T, E> ok(T value) {
    return new Ok<>(value);
  }

  static <T, E> Result<T, E> error(E error) {
    return new Error<>(error);
  }

  record Ok<T, E>(T value) implements Result<T, E> {
  }

  record Error<T, E>(E error) implements Result<T, E> {
  }

  final class ResultException extends RuntimeException {
    private final Object error;

    public ResultException(final Object error) {
      super(null, null, false, false);
      this.error = error;
    }

    @SuppressWarnings("unchecked")
    public <E> E getError() {
      return (E) error;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
      return this;
    }
  }

  final class ResultScope<E> {
    public <T> T bind(final Result<T, E> result) {
      return switch (result) {
        case Ok<T, E>(T value) -> value;
        case Error<T, E>(E error) -> throw new ResultException(error);
      };
    }
  }

  @FunctionalInterface
  interface ResultComputation<T, E> {
    T run(final ResultScope<E> scope);
  }

  static <T, E> Result<T, E> result(final ResultComputation<T, E> computation) {
    final ResultScope<E> scope = new ResultScope<>();
    try {
      final T result = computation.run(scope);
      return ok(result);
    } catch (final ResultException e) {
      return error(e.getError());
    }
  }
}
