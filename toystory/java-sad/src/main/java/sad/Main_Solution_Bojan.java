package sad;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main_Solution_Bojan {
    public static void main(final String[] args) {
        final var consumer1 = new PredicatedToyConsumer(toy -> true);
        final var consumer2 = new PredicatedToyConsumer(toy -> toy.color().equals(Toy.Color.BLUE));
        final var consumer3 = new PredicatedToyConsumer(toy -> toy.broken() && toy.price() > 10);
        final var consumer4 = new ColorGrouper();
        final var consumers = List.of( //
            consumer1, //
            consumer2, //
            consumer3, //
            consumer4);

        for (final var toy : Data.toys()) {
            for (final var consumer : consumers) {
                consumer.accept(toy);
            }
        }

        final var toysStats = new ToysStats( //
            consumer1.total, //
            consumer2.total, //
            consumer3.total, //
            consumer4.map);
        System.out.println(toysStats);

        // ToysStats[total=5, blue=2, expensiveAndBroken=1, colors={RED=1, BLUE=2, BLACK=1, GREEN=1}]
    }

    public static class PredicatedToyConsumer implements Consumer<Toy> {
        private final Predicate<Toy> predicate;
        private long total = 0;

        public PredicatedToyConsumer(final Predicate<Toy> predicate) {
            this.predicate = predicate;
        }

        @Override
        public void accept(final Toy toy) {
            if (predicate.test(toy)) {
                total++;
            }
        }
    }

    public static class ColorGrouper implements Consumer<Toy> {
        private final Map<Toy.Color, Integer> map = new EnumMap<>(Toy.Color.class);

        @Override
        public void accept(final Toy toy) {
            final var color = toy.color();
            map.putIfAbsent(color, 0);
            map.computeIfPresent(color, (c, i) -> i + 1);
        }
    }
}
