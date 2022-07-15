package com.telepathicgrunt.the_bumblezone.capabilities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.mixin.PlayerAdvancementsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.Map;


public class EntityMisc implements INBTSerializable<CompoundTag> {

	private boolean isBeeEssenced = false;
	private int craftedBeehives = 0;

	public void setIsBeeEssenced(boolean isBeeEssenced) {
		this.isBeeEssenced = isBeeEssenced;
	}

	public boolean getIsBeeEssenced() {
		return isBeeEssenced;
	}

	public void setCraftedBeehives(int craftedBeehives) {
		this.craftedBeehives = craftedBeehives;
	}

	public int getCraftedBeehives() {
		return craftedBeehives;
	}

	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("is_bee_essenced", this.getIsBeeEssenced());
		nbt.putInt("crafted_beehives", this.getCraftedBeehives());
		return nbt;
	}

	public void deserializeNBT(CompoundTag nbtTag) {
		this.setIsBeeEssenced(nbtTag.getBoolean("is_bee_essenced"));
		this.setCraftedBeehives(nbtTag.getInt("crafted_beehives"));
	}

	public static void resetValueOnRespawn(PlayerEvent.Clone event) {
		if (BzGeneralConfigs.keepBeeEssenceOnRespawning.get() && event.isWasDeath()) {
			if (event.getPlayer() instanceof ServerPlayer serverPlayerNew && event.getOriginal() instanceof ServerPlayer serverPlayerOld) {
				serverPlayerOld.reviveCaps();
				EssenceOfTheBees.setEssence(serverPlayerNew, EssenceOfTheBees.hasEssence(serverPlayerOld));
				serverPlayerOld.invalidateCaps();
			}
		}
	}

	private static final ResourceLocation QUEENS_DESIRE_ROOT_ADVANCEMENT = new ResourceLocation(Bumblezone.MODID, "the_bumblezone/the_queens_desire/the_beginning");
	public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		ItemStack createdItem = event.getCrafting();
		if (event.getPlayer() instanceof ServerPlayer serverPlayer &&
			createdItem.getItem() instanceof BlockItem blockItem &&
			blockItem.getBlock() instanceof BeehiveBlock)
		{
			Advancement advancement = serverPlayer.createCommandSourceStack().getAdvancement(QUEENS_DESIRE_ROOT_ADVANCEMENT);
			Map<Advancement, AdvancementProgress> advancementsProgressMap = ((PlayerAdvancementsAccessor)serverPlayer.getAdvancements()).getAdvancements();
			if (advancement != null &&
				advancementsProgressMap.containsKey(advancement) &&
				advancementsProgressMap.get(advancement).isDone())
			{
				EntityMisc capability = serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).orElseThrow(RuntimeException::new);
				int currentlyCraftedCount = capability.getCraftedBeehives() + 1;
				BzCriterias.BEEHIVE_CRAFTED_TRIGGER.trigger(serverPlayer, currentlyCraftedCount);
			}
		}
	}
}