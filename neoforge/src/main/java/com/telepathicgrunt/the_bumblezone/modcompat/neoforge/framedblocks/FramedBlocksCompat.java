package com.telepathicgrunt.the_bumblezone.modcompat.neoforge.framedblocks;

import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;

public final class FramedBlocksCompat implements ModCompat {

    // TODO: fix when framed blocks updates
//    private static final DeferredRegister<CamoContainerFactory<?>> CAMO_FACTORIES = DeferredRegister.create(
//            FramedConstants.CAMO_CONTAINER_FACTORY_REGISTRY_NAME,
//            Bumblezone.MODID
//    );
//    static final DeferredHolder<CamoContainerFactory<?>, CarvableWaxBlockCamoContainerFactory> WAX_BLOCK_CAMO_FACTORY =
//            CAMO_FACTORIES.register("carvable_wax", CarvableWaxBlockCamoContainerFactory::new);
//
//    public FramedBlocksCompat(IEventBus modEventBus) {
//        CAMO_FACTORIES.register(modEventBus);
//        NeoForge.EVENT_BUS.addListener(FramedBlocksCompat::onItemUsedOnBlock);
//
//        ModChecker.framedBlocksPresent = true;
//    }
//
//    /**
//     * Event handler for managing custom interactions with carvable wax, ancient wax and luminescent wax when applied
//     * as a camo to a framed block. To do so, the camo is retrieved from the framed block via the player's interaction
//     * point on the block, the stored state is modified as necessary and then a new camo container with the modified
//     * state is written back to the framed block
//     */
//    private static void onItemUsedOnBlock(UseItemOnBlockEvent event) {
//        Level level = event.getLevel();
//        Player player = event.getPlayer();
//        BlockPos pos = event.getPos();
//        if (player == null || !(level.getBlockEntity(pos) instanceof FramedBlockEntity be)) {
//            return;
//        }
//
//        BlockHitResult hit = ((UseOnContextAccessor) event.getUseOnContext()).bumblezone$getHitResult();
//        CamoContainer<?, ?> camo = be.getCamo(hit, player);
//        if (camo instanceof CarvableWaxBlockCamoContainer waxCamo && waxCamo.getState().getBlock() instanceof CarvableWax wax) {
//            BlockState carvedState = wax.tryCarve(event.getItemStack(), waxCamo.getState(), level, pos, player, event.getHand());
//            if (carvedState != null) {
//                if (!level.isClientSide()) {
//                    be.setCamo(waxCamo.copyWithState(carvedState), hit, player);
//                }
//                event.cancelWithResult(InteractionResult.SUCCESS);
//            }
//        }
//        else if (camo instanceof AbstractBlockCamoContainer<?> blockCamo) {
//            if (blockCamo.getState().getBlock() instanceof LuminescentWaxBase lumiWax) {
//                BlockState rotatedState = lumiWax.tryRotate(event.getItemStack(), blockCamo.getState(), level, pos, player, event.getHand());
//                if (rotatedState != null) {
//                    if (!level.isClientSide()) {
//                        be.setCamo(blockCamo.copyWithState(rotatedState), hit, player);
//                    }
//                    event.cancelWithResult(InteractionResult.SUCCESS);
//                }
//            }
//            else if (blockCamo.getState().getBlock() instanceof AncientWax ancientWax) {
//                BlockState swappedState = ancientWax.trySwap(event.getItemStack(), blockCamo.getState(), level, pos, player, event.getHand());
//                if (swappedState != null) {
//                    if (!level.isClientSide()) {
//                        be.setCamo(blockCamo.copyWithState(swappedState), hit, player);
//                    }
//                    event.cancelWithResult(InteractionResult.SUCCESS);
//                }
//            }
//        }
//    }
}
