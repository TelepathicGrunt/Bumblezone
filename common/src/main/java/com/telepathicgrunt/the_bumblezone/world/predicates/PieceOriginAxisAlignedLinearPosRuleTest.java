package com.telepathicgrunt.the_bumblezone.world.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzPredicates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;

public class PieceOriginAxisAlignedLinearPosRuleTest extends PosRuleTest {
    public static final Codec<PieceOriginAxisAlignedLinearPosRuleTest> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.FLOAT.fieldOf("min_chance").orElse(0.0F).forGetter((ruleTest) -> ruleTest.minChance),
            Codec.FLOAT.fieldOf("max_chance").orElse(0.0F).forGetter((ruleTest) -> ruleTest.maxChance),
            Codec.INT.fieldOf("min_dist").orElse(0).forGetter((ruleTest) -> ruleTest.minDistance),
            Codec.INT.fieldOf("max_dist").orElse(0).forGetter((ruleTest) -> ruleTest.maxDistance),
            Direction.Axis.CODEC.fieldOf("axis").orElse(Direction.Axis.Y).forGetter((ruleTest) -> ruleTest.axis)
    ).apply(instance, PieceOriginAxisAlignedLinearPosRuleTest::new));

    private final float minChance;
    private final float maxChance;
    private final int minDistance;
    private final int maxDistance;
    private final Direction.Axis axis;

    public PieceOriginAxisAlignedLinearPosRuleTest(float minChance, float maxChance, int minDistance, int maxDistance, Direction.Axis axis) {
        if (minDistance >= maxDistance) {
            throw new IllegalArgumentException("Invalid range: [" + minDistance + "," + maxDistance + "]");
        }
        else {
            this.minChance = minChance;
            this.maxChance = maxChance;
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.axis = axis;
        }
    }

    public boolean test(BlockPos blockPos, BlockPos blockPos2, BlockPos blockPos3, RandomSource random) {
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, this.axis);
        float xDist = (float)Math.abs((blockPos.getX()) * direction.getStepX());
        float yDist = (float)Math.abs((blockPos.getY()) * direction.getStepY());
        float zDist = (float)Math.abs((blockPos.getZ()) * direction.getStepZ());
        int distanceFromOrigin = (int)(xDist + yDist + zDist);
        float randomChance = random.nextFloat();
        return (double)randomChance <= Mth.clampedLerp(this.minChance, this.maxChance, Mth.inverseLerp(distanceFromOrigin, this.minDistance, this.maxDistance));
    }

    protected PosRuleTestType<?> getType() {
        return BzPredicates.PIECE_ORIGIN_AXIS_ALIGNED_LINEAR_POS_RULE_TEST.get();
    }
}
