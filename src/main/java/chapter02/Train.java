package chapter02;

import java.util.List;
import java.util.Optional;

record Train(TrainId id, List<Carriage> carriages, Stop origin, List<Stop> stops, Stop destination,
             Optional<Station> driverChange) {
}
