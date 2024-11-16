package chapter10._2_serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

final class Main {
  public static void main(String[] args) throws Exception {
    final var ibanezElectric = """
        {
          "brand": "Ibanez",
          "strings": "6",
          "pickups": ["H", "S", "H"]
        }
        """;

    final var objectMapper = new ObjectMapper();

    final var ibanezGuitar = objectMapper.readValue(ibanezElectric, RawGuitar.class).asFullGuitar();
    System.out.println(ibanezGuitar);

    final var yamahaAcoustic = objectMapper.readValue("""
            {
              "brand": "Yamaha",
              "strings": "6",
              "pickups": []
            }
            """, RawGuitar.class)
        .asFullGuitar();
    System.out.println(yamahaAcoustic);

    final var report = createReport(List.of(ibanezGuitar, ibanezGuitar, ibanezGuitar, ibanezGuitar, yamahaAcoustic));
    System.out.println(report);
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
