package common;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class OptionalMatch {
  private OptionalMatch() {
  }

  public static <T, R> R match(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") final Optional<? extends T> optional,
                               final Function<? super T, ? extends R> ifPresent,
                               final Supplier<? extends R> ifNotPresent) {
    if (optional.isPresent()) {
      return ifPresent.apply(optional.get());
    }
    return ifNotPresent.get();
  }
}
