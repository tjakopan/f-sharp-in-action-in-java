package chapter10._2_serialization;

import java.util.List;

record Electric(List<Pickup> pickups) implements Kind {
  Electric {
    pickups = List.copyOf(pickups);
  }
}
