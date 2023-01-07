package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.predicates.PieceOriginAxisAlignedLinearPosRuleTest;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;

public class BzPredicates {
    public static PosRuleTestType<PieceOriginAxisAlignedLinearPosRuleTest> PIECE_ORIGIN_AXIS_ALIGNED_LINEAR_POS_RULE_TEST = () -> PieceOriginAxisAlignedLinearPosRuleTest.CODEC;

    public static void registerPredicates() {
        Registry.register(Registry.POS_RULE_TEST, new ResourceLocation(Bumblezone.MODID, "piece_origin_axis_aligned_linear_pos_rule_test"), PIECE_ORIGIN_AXIS_ALIGNED_LINEAR_POS_RULE_TEST);
    }
}
