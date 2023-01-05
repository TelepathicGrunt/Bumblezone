package com.telepathicgrunt.the_bumblezone.utils;

import com.google.common.collect.Lists;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Enchantment Utility class used by OpenMods.  Replicated here under the permissions of the MIT Licenses.
 * @author boq
 *
 */
public class EnchantmentUtils {

	/**
	 * Be warned, minecraft doesn't update experienceTotal properly, so we have
	 * to do this.
	 *
	 * @param player
	 * @return
	 */
	public static int getPlayerXP(Player player) {
		return (int) (EnchantmentUtils.getExperienceForLevel(player.experienceLevel) + player.experienceProgress * player.getXpNeededForNextLevel());
	}

	public static void addPlayerXP(Player player, int amount) {
		int experience = getPlayerXP(player) + amount;
		player.totalExperience = experience;
		player.experienceLevel = EnchantmentUtils.getLevelForExperience(experience);
		int expForLevel = EnchantmentUtils.getExperienceForLevel(player.experienceLevel);
		player.experienceProgress = (float) (experience - expForLevel) / (float) player.getXpNeededForNextLevel();
	}

	public static int xpBarCap(int level) {
		if (level >= 30) return 112 + (level - 30) * 9;

		if (level >= 15) return 37 + (level - 15) * 5;

		return 7 + level * 2;
	}

	private static int sum(int n, int a0, int d) {
		return n * (2 * a0 + (n - 1) * d) / 2;
	}

	public static int getExperienceForLevel(int level) {
		if (level == 0) return 0;
		if (level <= 15) return sum(level, 7, 2);
		if (level <= 30) return 315 + sum(level - 15, 37, 5);
		return 1395 + sum(level - 30, 112, 9);
	}

	public static int getXpToNextLevel(int level) {
		int levelXP = EnchantmentUtils.getLevelForExperience(level);
		int nextXP = EnchantmentUtils.getExperienceForLevel(level + 1);
		return nextXP - levelXP;
	}

	public static int getLevelForExperience(int targetXp) {
		int level = 0;
		while (true) {
			final int xpToNextLevel = xpBarCap(level);
			if (targetXp < xpToNextLevel) return level;
			level++;
			targetXp -= xpToNextLevel;
		}
	}

	public static float getPower(Level world, BlockPos position) {
		float power = 0;

		for (int deltaZ = -1; deltaZ <= 1; ++deltaZ) {
			for (int deltaX = -1; deltaX <= 1; ++deltaX) {
				if ((deltaZ != 0 || deltaX != 0) && world.isEmptyBlock(position.offset(deltaX, 0, deltaZ)) && world.isEmptyBlock(position.offset(deltaX, 1, deltaZ))) {
					power += getEnchantPower(world, position.offset(deltaX * 2, 0, deltaZ * 2));
					power += getEnchantPower(world, position.offset(deltaX * 2, 1, deltaZ * 2));
					if (deltaX != 0 && deltaZ != 0) {
						power += getEnchantPower(world, position.offset(deltaX * 2, 0, deltaZ));
						power += getEnchantPower(world, position.offset(deltaX * 2, 1, deltaZ));
						power += getEnchantPower(world, position.offset(deltaX, 0, deltaZ * 2));
						power += getEnchantPower(world, position.offset(deltaX, 1, deltaZ * 2));
					}
				}
			}
		}
		return power;
	}

	static float getEnchantPower(Level world, BlockPos pos) {
		return world.getBlockState(pos).getEnchantPowerBonus(world, pos);
	}

	public static void addAllBooks(Enchantment enchantment, List<ItemStack> items) {
		for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); i++)
			items.add(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i)));
	}

	public static List<EnchantmentInstance> allAllowedEnchantsWithoutMaxLimit(int level, ItemStack stack, int xpTier) {
		List<EnchantmentInstance> list = Lists.newArrayList();
		boolean bookFlag = stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK);
		boolean allowTreasure = xpTier == 7;
		Map<Enchantment, Integer> existingEnchantments = getEnchantmentsOnBook(stack);
		for(Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {

			boolean forceAllowed = Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.tags()).getTag(BzTags.FORCED_ALLOWED_CRYSTALLINE_FLOWER_ENCHANTMENTS).contains(enchantment);
			boolean disallowed = Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.tags()).getTag(BzTags.DISALLOWED_CRYSTALLINE_FLOWER_ENCHANTMENTS).contains(enchantment);
			if (!forceAllowed && disallowed) {
				continue;
			}

			int minLevelAllowed = enchantment.getMinLevel();
			if (existingEnchantments.containsKey(enchantment)) {
				minLevelAllowed = Math.max(minLevelAllowed, existingEnchantments.get(enchantment) + 1);
			}

			if ((!enchantment.isTreasureOnly() || allowTreasure) && (forceAllowed || enchantment.isDiscoverable()) && (enchantment.canApplyAtEnchantingTable(stack) || (bookFlag && enchantment.isAllowedOnBooks()))) {
				for(int i = enchantment.getMaxLevel(); i > minLevelAllowed - 1; --i) {
					if (forceAllowed || level >= enchantment.getMinCost(i)) {
						EnchantmentInstance enchantmentInstance = new EnchantmentInstance(enchantment, i);
						if (xpTier > EnchantmentUtils.getEnchantmentTierCost(enchantmentInstance)) {
							list.add(enchantmentInstance);
							break;
						}
					}
				}
			}
		}
		list.sort(EnchantmentUtils::compareEnchantments);
		return list;
	}

	public static Map<Enchantment, Integer> getEnchantmentsOnBook(ItemStack itemStack) {
		ListTag listtag = EnchantedBookItem.getEnchantments(itemStack);
		Map<Enchantment, Integer> existingEnchants = new Object2IntOpenHashMap<>();

		for(int i = 0; i < listtag.size(); ++i) {
			CompoundTag compoundtag = listtag.getCompound(i);
			ResourceLocation resourcelocation1 = EnchantmentHelper.getEnchantmentId(compoundtag);
			if (resourcelocation1 != null) {
				existingEnchants.put(
					Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.get(resourcelocation1)),
					EnchantmentHelper.getEnchantmentLevel(compoundtag)
				);
			}
		}

		return existingEnchants;
	}

	public static int getEnchantmentTierCost(EnchantmentInstance enchantmentInstance) {
		return getEnchantmentTierCost(
				enchantmentInstance.level,
				enchantmentInstance.enchantment.getMinCost(2),
				enchantmentInstance.enchantment.isTreasureOnly(),
				enchantmentInstance.enchantment.isCurse());
	}

	public static int getEnchantmentTierCost(int level, int minCost, boolean isTreasureOnly, boolean isCurse) {
		int cost = 0;

		cost += minCost / 10;
		cost += level / 1.5f;

		if (isTreasureOnly) {
			cost += 2;
		}
		if (isCurse) {
			cost -= 3;
		}

		cost += BzGeneralConfigs.crystallineFlowerExtraTierCost.get();

		return Math.max(1, Math.min(6, cost));
	}

	public static int compareEnchantments(EnchantmentInstance enchantment1, EnchantmentInstance enchantment2) {
		ResourceKey<Enchantment> resourceKey1 = BuiltInRegistries.ENCHANTMENT.getResourceKey(enchantment2.enchantment).get();
		ResourceKey<Enchantment> resourceKey2 = BuiltInRegistries.ENCHANTMENT.getResourceKey(enchantment1.enchantment).get();

		int ret = resourceKey2.location().getPath().compareTo(resourceKey1.location().getPath());
		if (ret == 0) ret = enchantment2.level - enchantment1.level;
		return ret;
	}
}