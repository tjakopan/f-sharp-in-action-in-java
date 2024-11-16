package chapter05;

import common.Pair;

import java.math.BigDecimal;
import java.time.LocalDate;

final class _2_Records {
  private _2_Records() {
  }

  record Person(String firstName, String lastName, int age) {
  }

  record Address(String line1, String line2, String town, String postcode, String country) {
  }

  record PersonSimple(Pair<String, String> name, Address address) {
  }

  record PersonWithDescription(String name, int age, String description) {
  }

  static PersonWithDescription buildPerson(final String forename, final String surname, final int age) {
    return new PersonWithDescription(forename + " " + surname, 42, age < 18 ? "child" : "adult");
  }

  static PersonSimple generatePerson(final Address address) {
    if ("UK".equals(address.country)) {
      return new PersonSimple(new Pair<>("Isaac", "Abraham"), address);
    } else {
      return new PersonSimple(new Pair<>("John", "Doe"), address);
    }
  }

  record Name(String forename, String surname) {
  }

  record CreditRating(int value) {
  }

  record Customer(Name name, Address address, CreditRating creditRating) {
  }

  record Balance(BigDecimal value) {
  }

  record Supplier(Name name, Address address, Balance outstandingBalance, LocalDate nextPaymentDate) {
  }

  public static void main(String[] args) {
    final var isaac = new Person("Isaac", "Abraham", 42);
    final var fullName = isaac.firstName + " " + isaac.lastName;

    final var theAddress = new Address("1 Main Street", "", "London", "SW1 1AA", "UK");
    final var isaacAddr = new PersonSimple(new Pair<>("Isaac", "Abraham"), theAddress);
    final var theAddressInDE = new Address(theAddress.line1, theAddress.line2, "Berlin", theAddress.postcode(), "DE");

    final var isaacOne = new PersonSimple(new Pair<>("Isaac", "Abraham"), theAddress);
    final var isaacTwo = new PersonSimple(new Pair<>("Isaac", "Abraham"), theAddress);
    final var areTheyTheSame = isaacOne.equals(isaacTwo);
    System.out.println(areTheyTheSame);
  }
}
