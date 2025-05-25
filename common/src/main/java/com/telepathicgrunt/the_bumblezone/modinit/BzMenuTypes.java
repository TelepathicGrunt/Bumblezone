package com.telepathicgrunt.the_bumblezone.modinit;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.menus.BuzzingBriefcaseMenu;
import com.telepathicgrunt.the_bumblezone.menus.CrystallineFlowerMenu;
import com.telepathicgrunt.the_bumblezone.menus.StrictChestMenu;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.apache.commons.lang3.NotImplementedException;

public class BzMenuTypes {
    public static final ResourcefulRegistry<MenuType<?>> MENUS = ResourcefulRegistries.create(BuiltInRegistries.MENU, Bumblezone.MODID);

    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x1 = MENUS.register("strict_9x1", () -> PlatformService.INSTANCE.create(StrictChestMenu::oneRow));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x2 = MENUS.register("strict_9x2", () -> PlatformService.INSTANCE.create(StrictChestMenu::twoRows));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x3 = MENUS.register("strict_9x3", () -> PlatformService.INSTANCE.create(StrictChestMenu::threeRows));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x4 = MENUS.register("strict_9x4", () -> PlatformService.INSTANCE.create(StrictChestMenu::fourRows));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x5 = MENUS.register("strict_9x5", () -> PlatformService.INSTANCE.create(StrictChestMenu::fiveRows));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x6 = MENUS.register("strict_9x6", () -> PlatformService.INSTANCE.create(StrictChestMenu::sixRows));
    public static final RegistryEntry<MenuType<CrystallineFlowerMenu>> CRYSTALLINE_FLOWER = MENUS.register("crystalline_flower", () -> PlatformService.INSTANCE.create(CrystallineFlowerMenu::new));
    public static final RegistryEntry<MenuType<BuzzingBriefcaseMenu>> BUZZING_BRIEFCASE = MENUS.register("buzzing_briefcase", () -> PlatformService.INSTANCE.create(BuzzingBriefcaseMenu::new));

    @FunctionalInterface
    public interface MenuCreator<T extends AbstractContainerMenu> {
        T create(int i, Inventory inventory);
    }
}
