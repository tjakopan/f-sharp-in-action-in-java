package chapter10._2_serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Result;

import java.util.List;

import static common.Result.result;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

final class MainR {
  public static void main(String[] args) throws JsonProcessingException {
    final var ibanezElectric = """
        {
          "brand": "Ibanez",
          "strings": "6",
          "pickups": ["H", "S", "H"]
        }
        """;

    final var objectMapper = new ObjectMapper();

    final var ibanezGuitar = objectMapper.readValue(ibanezElectric, RawGuitar.class).asFullGuitarR();
    System.out.println(ibanezGuitar);

    final var yamahaAcoustic = objectMapper.readValue("""
            {
              "brand": "Yamaha",
              "strings": "6",
              "pickups": []
            }
            """, RawGuitar.class)
        .asFullGuitarR();
    System.out.println(yamahaAcoustic);

    final Result<List<Guitar>, String> guitars = result(scope -> {
      final var ig = scope.bind(ibanezGuitar);
      final var ya = scope.bind(yamahaAcoustic);
      return List.of(ig, ig, ig, ig, ig, ya);
    });
    switch (guitars) {
      case Result.Ok<List<Guitar>, String>(var g) -> {
        final var report = createReport(g);
        System.out.println(report);
      }
      case Result.Error<List<Guitar>, String>(var e) -> System.out.println(e);
    }
  }

  private record ReportItem(String brand, long guitars) {
  }

  static String createReport(final List<Guitar> guitars) throws JsonProcessingException {
    final var report = guitars.stream()
        .collect(groupingBy(Guitar::brand, counting()))
        .entrySet()
        .stream()
        .map(entry -> new ReportItem(entry.getKey().value(), entry.getValue()))
        .toList();
    final var objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(report);
  }
}
