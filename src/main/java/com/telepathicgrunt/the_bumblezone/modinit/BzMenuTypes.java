package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerMenu;
import com.telepathicgrunt.the_bumblezone.screens.StrictChestMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

public class BzMenuTypes {
    public static MenuType<StrictChestMenu> STRICT_9x1 = new MenuType<>(StrictChestMenu::oneRow);
    public static MenuType<StrictChestMenu> STRICT_9x2 = new MenuType<>(StrictChestMenu::twoRows);
    public static MenuType<StrictChestMenu> STRICT_9x3 = new MenuType<>(StrictChestMenu::threeRows);
    public static MenuType<StrictChestMenu> STRICT_9x4 = new MenuType<>(StrictChestMenu::fourRows);
    public static MenuType<StrictChestMenu> STRICT_9x5 = new MenuType<>(StrictChestMenu::fiveRows);
    public static MenuType<StrictChestMenu> STRICT_9x6 = new MenuType<>(StrictChestMenu::sixRows);
    public static MenuType<CrystallineFlowerMenu> CRYSTALLINE_FLOWER = new MenuType<>(CrystallineFlowerMenu::new);

    public static void registerMenu() {
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Bumblezone.MODID, "strict_9x1"), STRICT_9x1);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Bumblezone.MODID, "strict_9x2"), STRICT_9x2);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Bumblezone.MODID, "strict_9x3"), STRICT_9x3);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Bumblezone.MODID, "strict_9x4"), STRICT_9x4);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Bumblezone.MODID, "strict_9x5"), STRICT_9x5);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Bumblezone.MODID, "strict_9x6"), STRICT_9x6);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Bumblezone.MODID, "crystalline_flower"), CRYSTALLINE_FLOWER);
    }
}
