package common;

import java.util.Optional;

public final class Option {
  private Option() {
  }

  private static final class OptionException extends RuntimeException {
    private static final OptionException INSTANCE = new OptionException();

    private OptionException() {
      super(null, null, false, false);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
      return this;
    }
  }

  public static final class OptionScope {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public <T> T bind(final Optional<? extends T> optional) {
      return optional.orElseThrow(() -> OptionException.INSTANCE);
    }
  }

  @FunctionalInterface
  public interface OptionComputation<T> {
    T run(final OptionScope scope);
  }

  public static <T> Optional<T> option(final OptionComputation<T> computation) {
    final OptionScope scope = new OptionScope();
    try {
      final T result = computation.run(scope);
      return Optional.ofNullable(result);
    } catch (final OptionException e) {
      return Optional.empty();
    }
  }
}
