package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.effects.BzEffectsInit;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemEntity.class)
public class HoneyPickupMixin
{
    //bees attack player that picks up honey blocks
    @Inject(method = "onPlayerCollision",
            at = @At(value="INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onItemPickup(PlayerEntity player, CallbackInfo ci, ItemStack itemStack, Item item, int i) {

        //Bumblezone.LOGGER.log(Level.INFO, "started");
        World world = player.world;

        //Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
        //Also checks to make sure we are in dimension and that player isn't in creative or spectator
        if ((player.dimension == BzDimensionType.BUMBLEZONE_TYPE ) && //|| BzConfig.allowWrathOfTheHiveOutsideBumblezone) &&
            !player.isCreative() &&
            !player.isSpectator())
        {
            //if player picks up a honey block, bees gets very mad...
            if(item == Items.HONEY_BLOCK)// && BzConfig.aggressiveBees)
            {
                //Bumblezone.LOGGER.log(Level.INFO, "ANGRY BEES");
                player.addStatusEffect(new StatusEffectInstance(BzEffectsInit.WRATH_OF_THE_HIVE, 350/*BzConfig.howLongWrathOfTheHiveLasts*/, 2, false, true/*BzConfig.showWrathOfTheHiveParticles*/, true));
            }
        }
    }

}