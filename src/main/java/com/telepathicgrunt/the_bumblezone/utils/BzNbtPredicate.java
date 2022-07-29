package com.telepathicgrunt.the_bumblezone.utils;


import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

// Source: https://github.com/Resourceful-Bees/ResourcefulLib/blob/master/common/src/main/java/com/teamresourceful/resourcefullib/common/codecs/predicates/NbtPredicate.java
public record BzNbtPredicate(CompoundTag tag) {

    public static final BzNbtPredicate ANY = new BzNbtPredicate(null);
    public static final Codec<BzNbtPredicate> CODEC = CompoundTag.CODEC.xmap(BzNbtPredicate::new, BzNbtPredicate::tag);

    public boolean matches(ItemStack pStack) {
        return this == ANY || this.matches(pStack.getTag());
    }

    public boolean matches(Entity pEntity) {
        return this == ANY || this.matches(getEntityTagToCompare(pEntity));
    }

    public boolean matches(Tag tag) {
        return tag == null ? this == ANY : this.tag == null || NbtUtils.compareNbt(this.tag, tag, true);
    }

    public static CompoundTag getEntityTagToCompare(Entity entity) {
        CompoundTag compoundtag = entity.saveWithoutId(new CompoundTag());
        if (entity instanceof Player player) {
            ItemStack itemstack = player.getInventory().getSelected();
            if (!itemstack.isEmpty()) {
                compoundtag.put("SelectedItem", itemstack.save(new CompoundTag()));
            }
        }

        return compoundtag;
    }

}