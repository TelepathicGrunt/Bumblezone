package com.telepathicgrunt.the_bumblezone.utils;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

	public static int xpBarCap(int level) {
		if (level >= 30) return 112 + (level - 30) * 9;

		if (level >= 15) return 37 + (level - 15) * 5;

		return 7 + level * 2;
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

	public static Map<ResourceLocation, EnchantmentInstance> allAllowedEnchantsWithoutMaxLimit(int level, ItemStack stack, int xpTier) {
		Map<ResourceLocation, EnchantmentInstance> map = new HashMap<>();
		boolean bookFlag = stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK);
		boolean allowTreasure = xpTier == 7;
		Map<Enchantment, Integer> existingEnchantments = getEnchantmentsOnBook(stack);
		for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {

			boolean forceAllowed = GeneralUtils.isInTag(BuiltInRegistries.ENCHANTMENT, BzTags.FORCED_ALLOWED_CRYSTALLINE_FLOWER_ENCHANTMENTS, enchantment);
			boolean disallowed = GeneralUtils.isInTag(BuiltInRegistries.ENCHANTMENT, BzTags.DISALLOWED_CRYSTALLINE_FLOWER_ENCHANTMENTS, enchantment);
			if (!forceAllowed && disallowed) {
				continue;
			}

			int minLevelAllowed = enchantment.getMinLevel();
			if (existingEnchantments.containsKey(enchantment)) {
				minLevelAllowed = Math.max(minLevelAllowed, existingEnchantments.get(enchantment) + 1);
			}

			if (forceAllowed || (!(enchantment.isTreasureOnly() && !allowTreasure) && enchantment.isDiscoverable() && (canApplyAtEnchantingTable(enchantment, stack) || (bookFlag && isAllowedOnBooks(enchantment))))) {
				int maxLevelForEnchant = enchantment.getMaxLevel();
				for (ModCompat compat : ModChecker.ENCHANTMENT_MAX_LEVEL_COMPATS) {
					maxLevelForEnchant = compat.maxLevelForEnchantment(enchantment);
				}

				for(int i = maxLevelForEnchant; i > minLevelAllowed - 1; --i) {
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

	@ExpectPlatform
	@Contract(pure = true)
	public static boolean canApplyAtEnchantingTable(Enchantment enchantment, ItemStack stack) {
		throw new NotImplementedException();
	}

	@ExpectPlatform
	@Contract(pure = true)
	public static boolean isAllowedOnBooks(Enchantment enchantment) {
		throw new NotImplementedException();
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
				enchantmentInstance.enchantment.getMinCost(
						BuiltInRegistries.ENCHANTMENT.getKey(enchantmentInstance.enchantment).getNamespace().equals("minecraft") ?
						Math.max(enchantmentInstance.level, 2) : enchantmentInstance.level),
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