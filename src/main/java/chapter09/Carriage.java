package chapter09;

import java.util.Set;

record Carriage(CarriageId id, CarriageType type, int numberOfSeats, Set<CarriageFeature> features) {
}
