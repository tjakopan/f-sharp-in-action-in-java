package common;

import java.util.Optional;

public final class Option {
  private Option() {
  }

  private static final class OptionException extends RuntimeException {
  }

  public static final class OptionScope {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public <T> T bind(final Optional<? extends T> optional) {
      return optional.orElseThrow(OptionException::new);
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