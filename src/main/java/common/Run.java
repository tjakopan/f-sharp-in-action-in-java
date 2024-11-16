package common;

import java.util.function.Supplier;

public final class Run {
  private Run() {
  }

  public static <R> R run(final Supplier<? extends R> block) {
    return block.get();
  }
}
