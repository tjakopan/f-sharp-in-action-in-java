package chapter05;

import common.Pair;
import common.Quadruple;
import common.Triple;

final class _1_Tuples {
  private _1_Tuples() {
  }

  static <T, U> Pair<String, U> makeDoctor(final Pair<T, U> name) {
    final var sname = name.second();
    return new Pair<>("Dr", sname);
  }

  static Pair<Pair<String, String>, Pair<Integer, String>> buildPerson(final String forename, final String surname,
                                                                       final int age) {
    return new Pair<>(new Pair<>(forename, surname), new Pair<>(age, age < 18 ? "child" : "adult"));
  }

  public static void main(String[] args) {
    final var theAuthor = new Pair<>("isaac", "abraham");
    final var firstName = theAuthor.first();
    final var secondName = theAuthor.second();

    final var name = new Quadruple<>("isaac", "abraham", 42, "london");

    final var nameAndAge = new Triple<>("Jane", "Smith", 25);
    final var forename = nameAndAge.first();
    final var surname = nameAndAge.second();

    final var nameAndAgeNested = new Pair<>(new Pair<>("Joe", "Bloggs"), 28);
    final var nameNested = nameAndAgeNested.first();
    final var age = nameAndAgeNested.second();
  }
}
