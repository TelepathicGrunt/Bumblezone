package com.telepathicgrunt.the_bumblezone.advancements;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;


public class BlockStateSpecificTrigger extends SimpleCriterionTrigger<BlockStateSpecificTrigger.Instance> {

    public BlockStateSpecificTrigger() {}

    @Override
    public Instance createInstance(JsonObject jsonObject, Optional<ContextAwarePredicate> predicate, DeserializationContext deserializationContext) {
        return new Instance(predicate, BlockPredicate.CODEC.decode(JsonOps.INSTANCE, jsonObject.get("block")).result().get().getFirst());
    }

    public void trigger(ServerPlayer serverPlayer, BlockPos pos) {
        super.trigger(serverPlayer, (instance) -> instance.matches((ServerLevel) serverPlayer.level(), pos));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final BlockPredicate blockPredicate;

        public Instance(Optional<ContextAwarePredicate> predicate, BlockPredicate blockPredicate) {
            super(predicate);
            this.blockPredicate = blockPredicate;
        }

        public boolean matches(ServerLevel level, BlockPos pos) {
            return this.blockPredicate.matches(level, pos);
        }

        @Override
        public JsonObject serializeToJson() {
            JsonObject jsonobject = super.serializeToJson();
            jsonobject.add("blockstate", BlockPredicate.CODEC.encodeStart(JsonOps.INSTANCE, this.blockPredicate).result().get());
            return jsonobject;
        }
    }
}
