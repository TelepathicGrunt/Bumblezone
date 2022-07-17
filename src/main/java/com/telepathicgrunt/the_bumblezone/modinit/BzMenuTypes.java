package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.screens.StrictChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Bumblezone.MODID);

    public static final RegistryObject<MenuType<StrictChestMenu>> STRICT_9x1 = MENUS.register("strict_9x1", () -> new MenuType<>(StrictChestMenu::oneRow));
    public static final RegistryObject<MenuType<StrictChestMenu>> STRICT_9x2 = MENUS.register("strict_9x2", () -> new MenuType<>(StrictChestMenu::twoRows));
    public static final RegistryObject<MenuType<StrictChestMenu>> STRICT_9x3 = MENUS.register("strict_9x3", () -> new MenuType<>(StrictChestMenu::threeRows));
    public static final RegistryObject<MenuType<StrictChestMenu>> STRICT_9x4 = MENUS.register("strict_9x4", () -> new MenuType<>(StrictChestMenu::fourRows));
    public static final RegistryObject<MenuType<StrictChestMenu>> STRICT_9x5 = MENUS.register("strict_9x5", () -> new MenuType<>(StrictChestMenu::fiveRows));
    public static final RegistryObject<MenuType<StrictChestMenu>> STRICT_9x6 = MENUS.register("strict_9x6", () -> new MenuType<>(StrictChestMenu::sixRows));
}
