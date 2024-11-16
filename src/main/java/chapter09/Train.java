package chapter09;

import java.util.List;
import java.util.Optional;

record Train(TrainId id, Stop origin, List<Stop> stops, Stop destination, Optional<Station> driverChange,
             List<Carriage> carriages) {
}
