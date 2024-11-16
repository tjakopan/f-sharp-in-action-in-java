package chapter04;

final class _2_Immutability {
  private _2_Immutability() {
  }

  static double drive(final double gas, final String distance) {
    return "far".equals(distance)
        ? gas / 2.0
        : "medium".equals(distance)
        ? gas - 10.0
        : gas - 1.0;
  }

  static double drive43(final double gas, final int distance) {
    return distance > 50
        ? gas / 2.0
        : distance > 25
        ? gas - 10.0
        : distance > 0
        ? gas - 1.0
        : gas;
  }

  public static void main(final String[] args) {
    final var gas = 100.0;
    final var firstState = drive(gas, "far");
    final var secondState = drive(firstState, "medium");
    final var thirdState = drive(secondState, "near");
    System.out.println(thirdState);
  }
}
