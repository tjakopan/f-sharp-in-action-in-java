package common;

public final class Unit {
  public static final Unit INSTANCE = new Unit();

  private Unit() {
  }

  @Override
  public String toString() {
    return "Unit";
  }

  @Override
  public boolean equals(final Object obj) {
    return obj instanceof Unit;
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
