package com.telepathicgrunt.the_bumblezone.mixin.world;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.DynamicOps;
import com.telepathicgrunt.the_bumblezone.utils.WorldSeedHolder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.util.Optional;
import java.util.function.BiFunction;

@Mixin(LevelStorageSource.class)
public class LevelStorageSourceMixin {

	@Inject(method = "getDataPacks(Ljava/io/File;Lcom/mojang/datafixers/DataFixer;)Lnet/minecraft/world/level/DataPackConfig;",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/nbt/CompoundTag;getCompound(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;", ordinal = 0),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private static void worldblender_giveUsTrueWorldSeed(File file, DataFixer dataFixer,
												  CallbackInfoReturnable<DataPackConfig> cir,
												  CompoundTag fileCompoundTag, CompoundTag dataCompoundTag) {
		if(dataCompoundTag.contains("WorldGenSettings") && dataCompoundTag.getCompound("WorldGenSettings").contains("seed")) {
			WorldSeedHolder.setSeed(dataCompoundTag.getCompound("WorldGenSettings").getLong("seed"));
		}
	}
}