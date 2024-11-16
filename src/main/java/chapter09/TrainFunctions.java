package chapter09;

import chapter09.CarriageType.Buffet;
import chapter09.CarriageType.Passenger;
import chapter09.TrainFunctions.TimeToTravelException.InvalidOrderOfStations;
import chapter09.TrainFunctions.TimeToTravelException.NoSuchStation;
import chapter09.TrainFunctions.TimeToTravelException.StartAndEndStationAreTheSame;
import common.Result;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static chapter09.CarriageClass.FIRST;
import static chapter09.CarriageClass.SECOND;
import static chapter09.CarriageFeature.*;
import static common.OptionalMatch.match;

final class TrainFunctions {
  private TrainFunctions() {
  }

  static int numberOfSeats(final Train train) {
    return train.carriages()
        .stream()
        .mapToInt(Carriage::numberOfSeats)
        .sum();
  }

  sealed static abstract class TimeToTravelException extends Exception {
    static final class StartAndEndStationAreTheSame extends TimeToTravelException {
      private final Station station;

      StartAndEndStationAreTheSame(final Station station) {
        this.station = station;
      }

      public Station getStation() {
        return station;
      }
    }

    static final class InvalidOrderOfStations extends TimeToTravelException {
      private final Station start;
      private final Station end;

      InvalidOrderOfStations(final Station start, final Station end) {
        this.start = start;
        this.end = end;
      }

      public Station getStart() {
        return start;
      }

      public Station getEnd() {
        return end;
      }
    }

    static final class NoSuchStation extends TimeToTravelException {
      private final Station station;

      NoSuchStation(final Station station) {
        this.station = station;
      }

      public Station getStation() {
        return station;
      }
    }
  }

  static Result<Duration, TimeToTravelException> timeToTravelBetweenStations(final Station startStation,
                                                                             final Station endStation,
                                                                             final Train train) {
    if (startStation.equals(endStation)) {
      return new Result.Error<>(new StartAndEndStationAreTheSame(startStation));
    }

    final var allStops = allStops(train);
    final var startStop = allStops.stream()
        .filter(stop -> stop.station().equals(startStation))
        .findFirst();
    final var endStop = allStops.stream()
        .filter(stop -> stop.station().equals(endStation))
        .findFirst();
    return match(startStop,
        ss -> match(endStop,
            es -> {
              final var startTime = ss.arrival();
              final var endTime = es.arrival();
              if (startTime.isAfter(endTime)) {
                return new Result.Error<>(new InvalidOrderOfStations(startStation, endStation));
              }
              return new Result.Ok<>(Duration.between(startTime, endTime));
            },
            () -> new Result.Error<>(new NoSuchStation(endStation))
        ),
        () -> new Result.Error<>(new NoSuchStation(startStation)));
  }

  static Duration timeToTravelBetweenStationsEx(final Station startStation, final Station endStation, final Train train)
      throws TimeToTravelException {
    if (startStation.equals(endStation)) {
      throw new StartAndEndStationAreTheSame(startStation);
    }

    final var allStops = allStops(train);
    final var startStop = allStops.stream()
        .filter(stop -> stop.station().equals(startStation))
        .findFirst();
    final var endStop = allStops.stream()
        .filter(stop -> stop.station().equals(endStation))
        .findFirst();
    if (startStop.isPresent() && endStop.isPresent()) {
      final var startTime = startStop.get().arrival();
      final var endTime = endStop.get().arrival();
      if (startTime.isAfter(endTime)) {
        throw new InvalidOrderOfStations(startStation, endStation);
      }
      return Duration.between(startTime, endTime);
    } else if (startStop.isEmpty()) {
      throw new NoSuchStation(startStation);
    } else {
      throw new NoSuchStation(endStation);
    }
  }

  private static List<Stop> allStops(final Train train) {
    final var allStops = new ArrayList<Stop>();
    allStops.add(train.origin());
    allStops.addAll(train.stops());
    allStops.add(train.destination());
    return allStops;
  }

  static List<Carriage> carriagesWithFeature(final CarriageFeature feature, final Train train) {
    return train.carriages()
        .stream()
        .filter(carriage -> carriage.features().contains(feature))
        .toList();
  }

  public static void main(String[] args) {
    final var exampleTrain = new Train(
        new TrainId("ABC123"),
        new Stop(new Station("London St Pancras"), LocalTime.of(9, 0)),
        List.of(
            new Stop(new Station("Ashford"), LocalTime.of(10, 0)),
            new Stop(new Station("Lille"), LocalTime.of(12, 0))
        ),
        new Stop(new Station("Paris Nord"), LocalTime.of(13, 15)),
        Optional.of(new Station("Ashford")),
        List.of(
            new Carriage(new CarriageId(1), new Passenger(FIRST), 45, Set.of(WIFI, QUIET, WASHROOM)),
            new Carriage(new CarriageId(2), new Passenger(SECOND), 65, Set.of(WASHROOM)),
            new Carriage(new CarriageId(3), Buffet.builder().hotFood(true).coldFood(true).build(), 12, Set.of(WIFI))
        )
    );

    final var exampleTrainSeats = numberOfSeats(exampleTrain);
    System.out.println(exampleTrainSeats);

    final var wifiCarriages = carriagesWithFeature(WIFI, exampleTrain);
    System.out.println(wifiCarriages);

    try {
      final var londonParisNord =
          timeToTravelBetweenStationsEx(new Station("London St Pancras"), new Station("Paris Nord"), exampleTrain);
      System.out.println(londonParisNord);
    } catch (TimeToTravelException e) {
      e.printStackTrace();
    }

    try {
      final var londonParis =
          timeToTravelBetweenStationsEx(new Station("London St Pancras"), new Station("Paris"), exampleTrain);
      System.out.println(londonParis);
    } catch (TimeToTravelException e) {
      e.printStackTrace();
    }

    try {
      final var parisLondon =
          timeToTravelBetweenStationsEx(new Station("Paris Nord"), new Station("London St Pancras"), exampleTrain);
      System.out.println(parisLondon);
    } catch (TimeToTravelException e) {
      e.printStackTrace();
    }

    try {
      final var londonLondon =
          timeToTravelBetweenStationsEx(new Station("London St Pancras"), new Station("London St Pancras"),
              exampleTrain);
      System.out.println(londonLondon);
    } catch (TimeToTravelException e) {
      e.printStackTrace();
    }
  }
}
