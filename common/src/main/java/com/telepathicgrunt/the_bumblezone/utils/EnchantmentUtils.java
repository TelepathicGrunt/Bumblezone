package com.telepathicgrunt.the_bumblezone.utils;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Enchantment Utility class used by OpenMods.  Replicated here under the permissions of the MIT Licenses.
 * @author boq
 *
 */
public class EnchantmentUtils {

	/**
	 * Be warned, minecraft doesn't update experienceTotal properly, so we have to do this.
	 */
	public static long getPlayerXP(Player player) {
		return (long) (EnchantmentUtils.getExperienceForLevel(player.experienceLevel) + player.experienceProgress * player.getXpNeededForNextLevel());
	}

	private static long sum(int n, int a0, int d) {
		return n * (2L * a0 + (n - 1L) * d) / 2L;
	}

	public static long getExperienceForLevel(int level) {
		if (level == 0) return 0;
		if (level <= 15) return sum(level, 7, 2);
		if (level <= 30) return 315 + sum(level - 15, 37, 5);
		return 1395L + sum(level - 30, 112, 9);
	}

	public static Map<ResourceLocation, EnchantmentInstance> allAllowedEnchantsWithoutMaxLimit(int level, ItemStack itemStack, int xpTier) {
		Map<ResourceLocation, EnchantmentInstance> map = new HashMap<>();
		boolean bookFlag = itemStack.is(Items.BOOK) || itemStack.is(Items.ENCHANTED_BOOK);
		boolean allowTreasure = xpTier == 7;
		Map<Enchantment, Integer> existingEnchantments = getEnchantmentsOnBook(itemStack);
		for(Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {

			boolean forceAllowed = GeneralUtils.isInTag(BuiltInRegistries.ENCHANTMENT, BzTags.FORCED_ALLOWED_CRYSTALLINE_FLOWER_ENCHANTMENTS, enchantment);
			boolean disallowed = GeneralUtils.isInTag(BuiltInRegistries.ENCHANTMENT, BzTags.DISALLOWED_CRYSTALLINE_FLOWER_ENCHANTMENTS, enchantment);
			if (!forceAllowed && disallowed) {
				continue;
			}

			int minLevelAllowed = enchantment.getMinLevel();
			if (existingEnchantments.containsKey(enchantment)) {
				minLevelAllowed = Math.max(minLevelAllowed, existingEnchantments.get(enchantment) + 1);
			}

			if ((!enchantment.isTreasureOnly() || allowTreasure) && (forceAllowed || enchantment.isDiscoverable()) && (bookFlag || (enchantment.canEnchant(itemStack) && enchantment.isPrimaryItem(itemStack)))) {
				for(int i = enchantment.getMaxLevel(); i > minLevelAllowed - 1; --i) {
					if (forceAllowed || level >= enchantment.getMinCost(i)) {
						EnchantmentInstance enchantmentInstance = new EnchantmentInstance(enchantment, xpTier <= 2 ? 1 : i);
						if (xpTier > EnchantmentUtils.getEnchantmentTierCost(enchantmentInstance)) {
							map.put(BuiltInRegistries.ENCHANTMENT.getKey(enchantmentInstance.enchantment), enchantmentInstance);
							break;
						}
					}
				}
			}
		}
		return map;
	}

	public static Map<Enchantment, Integer> getEnchantmentsOnBook(ItemStack itemStack) {
		Set<Object2IntMap.Entry<Holder<Enchantment>>> enchantments = itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).entrySet();
		Map<Enchantment, Integer> existingEnchants = new Object2IntOpenHashMap<>();

		for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments) {
			existingEnchants.put(entry.getKey().value(), entry.getIntValue());
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

		cost += BzGeneralConfigs.crystallineFlowerExtraTierCost;

		return Math.max(1, Math.min(6, cost));
	}
}