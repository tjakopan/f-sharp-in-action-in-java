package chapter10._2_serialization;

final class Acoustic implements Kind {
  static final Acoustic INSTANCE = new Acoustic();

  private Acoustic() {
  }

  @Override
  public String toString() {
    return "Acoustic";
  }
}
