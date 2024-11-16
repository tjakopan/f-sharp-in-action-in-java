package chapter02;

sealed interface CarriageKind {
  record Passenger(CarriageClass carriageClass) implements CarriageKind {
  }

  record Buffet(boolean coldFood, boolean hotFood) implements CarriageKind {
  }
}
