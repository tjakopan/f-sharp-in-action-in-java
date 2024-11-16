package chapter10._2_serialization;

import common.Result;

import java.util.ArrayList;
import java.util.List;

import static common.Result.*;

record RawGuitar(String brand, String strings, List<String> pickups) {
  Guitar asFullGuitar() throws Exception {
    final Brand brand = asFullBrand(brand());
    final Strings strings = asFullStrings(strings());
    final List<Pickup> pickups = asFullPickups(pickups());

    return new Guitar(brand, strings, pickups.isEmpty() ? Acoustic.INSTANCE : new Electric(pickups));
  }

  private static Brand asFullBrand(final String rawBrand) throws Exception {
    if (rawBrand == null || rawBrand.isBlank()) {
      throw new Exception("Brand is mandatory");
    }
    return new Brand(rawBrand);
  }

  private static Strings asFullStrings(final String rawStrings) throws Exception {
    return switch (rawStrings) {
      case "6" -> Strings.SIX;
      case "7" -> Strings.SEVEN;
      case "8" -> Strings.EIGHT;
      case "12" -> Strings.TWELVE;
      default -> throw new Exception("Invalid value '%s'".formatted(rawStrings));
    };
  }

  private static List<Pickup> asFullPickups(final List<String> rawPickups) throws Exception {
    final List<Pickup> pickups = new ArrayList<>();
    for (final String rawPickup : rawPickups) {
      if ("S".equals(rawPickup)) {
        pickups.add(Pickup.SINGLE);
      } else if ("H".equals(rawPickup)) {
        pickups.add(Pickup.HUMBUCKER);
      } else {
        throw new Exception("Invalid value '%s'".formatted(rawPickup));
      }
    }
    return List.copyOf(pickups);
  }

  Result<Guitar, String> asFullGuitarR() {
    return result(scope -> {
      final Brand brand = scope.bind(asFullBrandR(brand()));
      final Strings strings = scope.bind(asFullStringsR(strings()));
      final List<Pickup> pickups = scope.bind(asFullPickupsR(pickups()));
      return new Guitar(brand, strings, pickups.isEmpty() ? Acoustic.INSTANCE : new Electric(pickups));
    });
  }

  private static Result<Brand, String> asFullBrandR(final String rawBrand) {
    if (rawBrand == null || rawBrand.isBlank()) {
      return error("Brand is mandatory");
    }
    return ok(new Brand(rawBrand));
  }

  private static Result<Strings, String> asFullStringsR(final String rawStrings) {
    return switch (rawStrings) {
      case "6" -> ok(Strings.SIX);
      case "7" -> ok(Strings.SEVEN);
      case "8" -> ok(Strings.EIGHT);
      case "12" -> ok(Strings.TWELVE);
      default -> error("Invalid value '%s'".formatted(rawStrings));
    };
  }

  private static Result<List<Pickup>, String> asFullPickupsR(final List<String> rawPickups) {
    final List<Pickup> pickups = new ArrayList<>();
    for (final String rawPickup : rawPickups) {
      if ("S".equals(rawPickup)) {
        pickups.add(Pickup.SINGLE);
      } else if ("H".equals(rawPickup)) {
        pickups.add(Pickup.HUMBUCKER);
      } else {
        return error("Invalid value '%s'".formatted(rawPickup));
      }
    }
    return ok(pickups);
  }
}
