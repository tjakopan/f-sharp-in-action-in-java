package chapter02;

import java.util.Set;

record Carriage(CarriageNumber number, CarriageKind kind, Set<Feature> features, int numberOfSeats) {
}
