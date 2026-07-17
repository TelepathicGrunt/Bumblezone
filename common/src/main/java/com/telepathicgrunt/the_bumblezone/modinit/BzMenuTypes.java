package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.menus.BuzzingBriefcaseMenu;
import com.telepathicgrunt.the_bumblezone.menus.CrystallineFlowerMenu;
import com.telepathicgrunt.the_bumblezone.menus.StrictChestMenu;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistriesService;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class BzMenuTypes {
    public static final ResourcefulRegistry<MenuType<?>> MENUS = ResourcefulRegistriesService.INSTANCE.create(BuiltInRegistries.MENU, Bumblezone.MODID);

    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x1 = MENUS.register("strict_9x1", () -> create(StrictChestMenu::oneRow));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x2 = MENUS.register("strict_9x2", () -> create(StrictChestMenu::twoRows));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x3 = MENUS.register("strict_9x3", () -> create(StrictChestMenu::threeRows));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x4 = MENUS.register("strict_9x4", () -> create(StrictChestMenu::fourRows));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x5 = MENUS.register("strict_9x5", () -> create(StrictChestMenu::fiveRows));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x6 = MENUS.register("strict_9x6", () -> create(StrictChestMenu::sixRows));
    public static final RegistryEntry<MenuType<CrystallineFlowerMenu>> CRYSTALLINE_FLOWER = MENUS.register("crystalline_flower", () -> create(CrystallineFlowerMenu::new));
    public static final RegistryEntry<MenuType<BuzzingBriefcaseMenu>> BUZZING_BRIEFCASE = MENUS.register("buzzing_briefcase", () -> create(BuzzingBriefcaseMenu::new));

    public static <T extends AbstractContainerMenu> MenuType<T> create(MenuCreator<T> creator) {
        return new MenuType<>(creator::create, FeatureFlags.DEFAULT_FLAGS);
    }

    @FunctionalInterface
    public interface MenuCreator<T extends AbstractContainerMenu> {
        T create(int i, Inventory inventory);
    }
}
