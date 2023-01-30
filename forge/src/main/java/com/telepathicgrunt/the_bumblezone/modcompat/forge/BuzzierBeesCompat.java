package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.events.AddFeaturesEvent;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DispenserBlockInvoker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class BuzzierBeesCompat implements ModCompat {
    private static final ResourceLocation BEE_BOTTLE_RL = new ResourceLocation("buzzier_bees", "bee_bottle");

    private static final List<ResourceLocation> FEATURES = List.of(
            new ResourceLocation(Bumblezone.MODID, "buzzier_bees_crystallized_honey_pf"),
            new ResourceLocation(Bumblezone.MODID, "buzzier_bees_honeycomb_tiles_pf")
    );

    public BuzzierBeesCompat() {
        Optional<Item> bottledBee = BuiltInRegistries.ITEM.getOptional(BEE_BOTTLE_RL);

        if (bottledBee.isPresent() && BzModCompatibilityConfigs.allowBeeBottleRevivingEmptyBroodBlock) {
            BuzzierBeesBottledBeeDispenseBehavior.DEFAULT_BOTTLED_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(bottledBee.get()));
            DispenserBlock.registerBehavior(bottledBee.get(), new BuzzierBeesBottledBeeDispenseBehavior()); // adds compatibility with bottled bee in dispensers
        }

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.buzzierBeesPresent = true;
        AddFeaturesEvent.EVENT.addListener(BuzzierBeesCompat::addHoneycombVeins);
    }

    private static void addHoneycombVeins(AddFeaturesEvent event) {
        for (ResourceLocation feature : FEATURES) {
            event.addFeature(
                    biome -> biome.is(BzTags.THE_BUMBLEZONE),
                    GenerationStep.Decoration.UNDERGROUND_ORES,
                    feature
            );
        }
    }

    @Override
    public EnumSet<Type> compatTypes() {
        return EnumSet.of(Type.EMPTY_BROOD);
    }

    @Override
    public InteractionResult onEmptyBroodInteract(ItemStack itemstack, Player playerEntity, InteractionHand playerHand) {
        if (!BzModCompatibilityConfigs.allowBeeBottleRevivingEmptyBroodBlock) return InteractionResult.PASS;
        if (BuiltInRegistries.ITEM.getKey(itemstack.getItem()).equals(BEE_BOTTLE_RL)) {
            if (!playerEntity.isCrouching()) {
                if (!playerEntity.isCreative()) {
                    playerEntity.setItemInHand(playerHand, new ItemStack(Items.GLASS_BOTTLE)); //replaced bottled bee with glass bottle
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
