package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.menus.BuzzingBriefcaseMenu;
import com.telepathicgrunt.the_bumblezone.menus.CrystallineFlowerMenu;
import com.telepathicgrunt.the_bumblezone.menus.StrictChestMenu;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import com.telepathicgrunt.the_bumblezone.services.RegistrationService;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.apache.commons.lang3.NotImplementedException;

public class BzMenuTypes {
    public static final ResourcefulRegistry<MenuType<?>> MENUS = RegistrationService.INSTANCE.create(BuiltInRegistries.MENU, Bumblezone.MODID);

    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x1 = MENUS.register("strict_9x1", () -> RegistrationService.INSTANCE.create(StrictChestMenu::oneRow));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x2 = MENUS.register("strict_9x2", () -> RegistrationService.INSTANCE.create(StrictChestMenu::twoRows));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x3 = MENUS.register("strict_9x3", () -> RegistrationService.INSTANCE.create(StrictChestMenu::threeRows));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x4 = MENUS.register("strict_9x4", () -> RegistrationService.INSTANCE.create(StrictChestMenu::fourRows));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x5 = MENUS.register("strict_9x5", () -> RegistrationService.INSTANCE.create(StrictChestMenu::fiveRows));
    public static final RegistryEntry<MenuType<StrictChestMenu>> STRICT_9x6 = MENUS.register("strict_9x6", () -> RegistrationService.INSTANCE.create(StrictChestMenu::sixRows));
    public static final RegistryEntry<MenuType<CrystallineFlowerMenu>> CRYSTALLINE_FLOWER = MENUS.register("crystalline_flower", () -> RegistrationService.INSTANCE.create(CrystallineFlowerMenu::new));
    public static final RegistryEntry<MenuType<BuzzingBriefcaseMenu>> BUZZING_BRIEFCASE = MENUS.register("buzzing_briefcase", () -> RegistrationService.INSTANCE.create(BuzzingBriefcaseMenu::new));

    @FunctionalInterface
    public interface MenuCreator<T extends AbstractContainerMenu> {
        T create(int i, Inventory inventory);
    }
}
