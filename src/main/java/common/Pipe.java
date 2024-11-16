package common;

import java.util.function.Consumer;
import java.util.function.Function;

public final class Pipe<T> {
  private final T value;

  private Pipe(final T value) {
    this.value = value;
  }

  public static <T> Pipe<T> of(final T value) {
    return new Pipe<>(value);
  }

  public <R> Pipe<R> pipe(final Function<? super T, ? extends R> mapper) {
    return new Pipe<>(mapper.apply(value));
  }

  public T get() {
    return value;
  }

  public T get(final Consumer<? super T> consumer) {
    consumer.accept(value);
    return value;
  }
}
