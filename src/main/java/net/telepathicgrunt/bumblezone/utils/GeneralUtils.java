package net.telepathicgrunt.bumblezone.utils;

import com.mojang.datafixers.util.Pair;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class GeneralUtils {

    // Weighted Random from: https://stackoverflow.com/a/6737362
    public static <T> T getRandomEntry(List<Pair<T, Integer>> rlList, Random random) {
        double totalWeight = 0.0;

        // Compute the total weight of all items together.
        for (Pair<T, Integer> pair : rlList) {
            totalWeight += pair.getSecond();
        }

        // Now choose a random item.
        int index = 0;
        for (double randomWeightPicked = random.nextFloat() * totalWeight; index < rlList.size() - 1; ++index) {
            randomWeightPicked -= rlList.get(index).getSecond();
            if (randomWeightPicked <= 0.0) break;
        }

        return rlList.get(index).getFirst();
    }

    ////////////////

    // Source: https://dzone.com/articles/be-lazy-with-java-8
    public static final class Lazy<T> {

        private volatile T value;

        public T getOrCompute(Supplier<T> supplier) {
            final T result = value; // Just one volatile read
            return result == null ? maybeCompute(supplier) : result;
        }

        private synchronized T maybeCompute(Supplier<T> supplier) {
            if (value == null) {
                value = requireNonNull(supplier.get());
            }
            return value;
        }
    }
}
