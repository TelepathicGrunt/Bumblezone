package com.telepathicgrunt.the_bumblezone.components;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.mixin.entities.PlayerAdvancementsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import dev.architectury.event.events.common.PlayerEvent;
import dev.onyxstudios.cca.api.v3.component.Component;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BeehiveBlock;

import java.util.Map;

public class EssenceComponent implements Component {

    public boolean isBeeEssenced = false;

    @Override
    public void writeToNbt(CompoundTag nbt) {
        nbt.putBoolean("is_bee_essenced", this.isBeeEssenced);
    }

    @Override
    public void readFromNbt(CompoundTag nbtTag) {
        this.isBeeEssenced = nbtTag.getBoolean("is_bee_essenced");
    }
}