package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.thrown.ThrownEnderpearlEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEnderpearlEntity.class)
public class EnderpearlImpactMixin {


    // Teleports player to Bumblezone when pearl hits bee nest
    @Inject(method = "onCollision",
            at = @At(value = "TAIL"),
            cancellable = true)
    private void onPearlHit(HitResult hitResult, CallbackInfo ci) {

        ThrownEnderpearlEntity pearlEntity = ((ThrownEnderpearlEntity) (Object) this);

        World world = pearlEntity.world; // world we threw in

        //Make sure we are on server by checking if thrower is ServerPlayerEntity
        if (!world.isClient && pearlEntity.method_24921() instanceof ServerPlayerEntity) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) pearlEntity.method_24921(); // the thrower
            Vec3d hitBlockPos = hitResult.getPos(); //position of the collision
            BlockPos hivePos = new BlockPos(0,0,0);
            boolean hitHive = false;

            //check with offset in all direction as the position of exact hit point could barely be outside the hive block
            //even through the pearl hit the block directly.
            for(double offset = -0.1D; offset <= 0.1D; offset += 0.1D) {
                BlockState block = world.getBlockState(new BlockPos(hitBlockPos.add(offset, 0, 0)));
                if(world.getBlockState(new BlockPos(hitBlockPos.add(-0.1D, 0, 0))).getBlock() == Blocks.BEE_NEST) {
                    hitHive = true;
                    hivePos = new BlockPos(hitBlockPos.add(offset, 0, 0));
                    break;
                }

                block = world.getBlockState(new BlockPos(hitBlockPos.add(0, offset, 0)));
                if(world.getBlockState(new BlockPos(hitBlockPos.add(-0.1D, 0, 0))).getBlock() == Blocks.BEE_NEST) {
                    hitHive = true;
                    hivePos = new BlockPos(hitBlockPos.add(0, offset, 0));
                    break;
                }

                block = world.getBlockState(new BlockPos(hitBlockPos.add(0, 0, offset)));
                if(world.getBlockState(new BlockPos(hitBlockPos.add(-0.1D, 0, 0))).getBlock() == Blocks.BEE_NEST) {
                    hitHive = true;
                    hivePos = new BlockPos(hitBlockPos.add(0, 0, offset));
                    break;
                }
            }

            //checks if block under hive is correct if config needs one
            boolean validBelowBlock = false;
            String requiredBlockString = Bumblezone.BZ_CONFIG.requiredBlockUnderHive;
            if(!requiredBlockString.trim().isEmpty())
            {
                if(requiredBlockString.matches("[a-z0-9/._-]+:[a-z0-9/._-]+") && Registry.BLOCK.containsId(new Identifier(requiredBlockString)))
                {
                    Block requiredBlock = Registry.BLOCK.get(new Identifier(requiredBlockString));
                    if(requiredBlock == world.getBlockState(hivePos.down()).getBlock())
                    {
                        validBelowBlock = true;
                    }
                    else if(Bumblezone.BZ_CONFIG.warnPlayersOfWrongBlockUnderHive && world.isClient)
                    {
                        //failed. Block below isn't the required block
                        String beeBlock = world.getBlockState(hivePos).getBlock().getName().getString();
                        Bumblezone.LOGGER.log(Level.INFO, "Bumblezone: The block under the "+beeBlock+" is not the correct block to teleport to Bumblezone. The config enter says it needs "+requiredBlockString+" under "+beeBlock+".");
                        Text message = new LiteralText("§eBumblezone:§f The block under the §6"+beeBlock+"§f is not the correct block to teleport to Bumblezone. The config enter says it needs §6"+requiredBlockString+"§f under §6"+beeBlock+"§f.");
                        playerEntity.sendMessage(message);
                        return;
                    }
                }
                else
                {
                    //failed. the required block config entry is broken
                    Bumblezone.LOGGER.log(Level.INFO, "Bumblezone: The required block under beenest config is broken. Please specify a resourcelocation to a real block or leave it blank so that players can teleport to Bumblezone dimension. Currently, the broken config has this in it: "+requiredBlockString);
                    Text message = new LiteralText("§eBumblezone:§f The required block under beenest config is broken. Please specify a resourcelocation to a real block or leave it blank so that players can teleport to Bumblezone dimension. Currently, the broken config has this in it: §c"+requiredBlockString);
                    playerEntity.sendMessage(message);
                    return;
                }
            }
            else {
                validBelowBlock = true;
            }


            //if the pearl hit a beehive and is not in our bee dimension, begin the teleportation.
            if (hitHive && validBelowBlock && playerEntity.dimension != BzDimensionType.BUMBLEZONE_TYPE) {
                Bumblezone.PLAYER_COMPONENT.get(playerEntity).setIsTeleporting(true);
                ci.cancel(); // cancel rest of the enderpearl hit stuff
            }
        }
    }

}