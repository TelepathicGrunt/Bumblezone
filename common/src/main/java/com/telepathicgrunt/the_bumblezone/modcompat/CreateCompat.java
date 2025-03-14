package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.EnumSet;

public class CreateCompat implements ModCompat {
    private static Block LIMESTONE;

    public CreateCompat() {
        LIMESTONE = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("create", "limestone"));

       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.createPresent = true;
    }

    @Override
    public EnumSet<Type> compatTypes() {
        return EnumSet.of(Type.HONEY_FLUID_LAVA_INTERACTION);
    }

    @Override
    public BlockState honeyLavaResultBlock(FluidState honeyFluid) {
        return BzModCompatibilityConfigs.allowCreateLimestoneForHoneyLavaCompat ? LIMESTONE.defaultBlockState() : null;
    }
}
