package com.sandaliaball.kingdoms.util;

import net.minecraft.util.math.BlockPos;

import java.util.Random;
import java.util.function.Consumer;

public class MathUtils {
    public static final Random RANDOM = new Random();

    public static void iterateSurroundings(BlockPos pos, double chance, Consumer<BlockPos> consumer) {
        consumer.accept(pos);
        for (BlockPos target : BlockPos.iterate(pos.add(-1, 0, -1), pos.add(1, 0, 1)))
            if (!target.equals(pos) && RANDOM.nextDouble() < chance) consumer.accept(target);
    }
}
