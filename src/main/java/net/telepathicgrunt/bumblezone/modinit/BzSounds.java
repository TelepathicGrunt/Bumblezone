package net.telepathicgrunt.bumblezone.modinit;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.enchantments.CombCutterEnchantment;

public class BzSounds {
    public final static SoundEvent ANGERED_BEES = new SoundEvent(new Identifier(Bumblezone.MODID, "angered_bees"));

    public static void registerSounds() {
        Registry.register(Registry.SOUND_EVENT, new Identifier(Bumblezone.MODID, "angered_bees"), ANGERED_BEES);
    }
}
