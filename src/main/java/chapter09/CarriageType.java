package chapter09;

import java.util.Objects;

sealed interface CarriageType {
  record Passenger(CarriageClass carriageClass) implements CarriageType {
  }

  final class Buffet implements CarriageType {
    private final boolean hotFood;
    private final boolean coldFood;

    private Buffet(boolean hotFood, boolean coldFood) {
      this.hotFood = hotFood;
      this.coldFood = coldFood;
    }

    static BuffetBuilder builder() {
      return new BuffetBuilder();
    }

    public boolean hotFood() {
      return hotFood;
    }

    public boolean coldFood() {
      return coldFood;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) return true;
      if (obj == null || obj.getClass() != this.getClass()) return false;
      var that = (Buffet) obj;
      return this.hotFood == that.hotFood &&
          this.coldFood == that.coldFood;
    }

    @Override
    public int hashCode() {
      return Objects.hash(hotFood, coldFood);
    }

    @Override
    public String toString() {
      return "Buffet[" +
          "hotFood=" + hotFood + ", " +
          "coldFood=" + coldFood + ']';
    }

    static class BuffetBuilder {
      private boolean hotFood = false;
      private boolean coldFood = false;

      BuffetBuilder hotFood(final boolean hotFood) {
        this.hotFood = hotFood;
        return this;
      }

      BuffetBuilder coldFood(final boolean coldFood) {
        this.coldFood = coldFood;
        return this;
      }

      Buffet build() {
        return new Buffet(hotFood, coldFood);
      }
    }
  }
}
