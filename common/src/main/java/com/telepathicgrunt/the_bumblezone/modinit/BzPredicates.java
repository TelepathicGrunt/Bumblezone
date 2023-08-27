package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.world.predicates.BlocksNotMatchRuleTest;
import com.telepathicgrunt.the_bumblezone.world.predicates.PieceOriginAxisAlignedLinearPosRuleTest;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public class BzPredicates {
    public static final ResourcefulRegistry<PosRuleTestType<?>> POS_RULE_TEST = ResourcefulRegistries.create(BuiltInRegistries.POS_RULE_TEST, Bumblezone.MODID);
    public static final ResourcefulRegistry<RuleTestType<?>> RULE_TEST = ResourcefulRegistries.create(BuiltInRegistries.RULE_TEST, Bumblezone.MODID);

    public static final RegistryEntry<PosRuleTestType<PieceOriginAxisAlignedLinearPosRuleTest>> PIECE_ORIGIN_AXIS_ALIGNED_LINEAR_POS_RULE_TEST = POS_RULE_TEST.register("piece_origin_axis_aligned_linear_pos_rule_test", () -> () -> PieceOriginAxisAlignedLinearPosRuleTest.CODEC);
    public static final RegistryEntry<RuleTestType<BlocksNotMatchRuleTest>> BLOCKS_NOT_MATCH_RULE_TEST = RULE_TEST.register("blocks_not_match_rule_test", () -> () -> BlocksNotMatchRuleTest.CODEC);
}
