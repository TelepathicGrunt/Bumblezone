package com.telepathicgrunt.the_bumblezone.utils;


import com.mojang.serialization.Codec;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

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
        return tag == null ? this == ANY : this.tag == null || compareNbt(this.tag, tag, true);
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

    public static boolean compareNbt(@Nullable Tag tag, @Nullable Tag tag2, boolean bl) {
        if (tag == tag2) {
            return true;
        }
        if (tag == null) {
            return true;
        }
        if (tag2 == null) {
            return false;
        }
        if (tag instanceof NumericTag numericTag && tag2 instanceof NumericTag numericTag2) {
            return numericTag.getAsDouble() == numericTag2.getAsDouble();
        }
        if (!tag.getClass().equals(tag2.getClass())) {
            return false;
        }
        if (tag instanceof CompoundTag compoundTag) {
            CompoundTag compoundTag2 = (CompoundTag)tag2;
            for (String string : compoundTag.getAllKeys()) {
                Tag tag3 = compoundTag.get(string);
                if (compareNbt(tag3, compoundTag2.get(string), bl)) continue;
                return false;
            }
            return true;
        }
        if (tag instanceof ListTag listTag && bl) {
            ListTag listTag2 = (ListTag)tag2;
            if (listTag.isEmpty()) {
                return listTag2.isEmpty();
            }
            for (Tag tag4 : listTag) {
                boolean bl2 = false;
                for (Tag value : listTag2) {
                    if (!compareNbt(tag4, value, true)) continue;
                    bl2 = true;
                    break;
                }
                if (bl2) continue;
                return false;
            }
            return true;
        }
        return tag.equals(tag2);
    }
}