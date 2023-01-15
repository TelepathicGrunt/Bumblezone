package com.telepathicgrunt.the_bumblezone.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;


public class BlockStateSpecificTrigger extends SimpleCriterionTrigger<BlockStateSpecificTrigger.Instance> {
    private final ResourceLocation id;

    public BlockStateSpecificTrigger(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public Instance createInstance(JsonObject jsonObject, EntityPredicate.Composite predicate, DeserializationContext deserializationContext) {
        return new Instance(predicate, BlockPredicate.fromJson(jsonObject.get("block")));
    }

    public void trigger(ServerPlayer serverPlayer, BlockPos pos) {
        super.trigger(serverPlayer, (instance) -> instance.matches(serverPlayer.getLevel(), pos));
    }

    public class Instance extends AbstractCriterionTriggerInstance {
        private final BlockPredicate blockPredicate;

        public Instance(EntityPredicate.Composite predicate, BlockPredicate blockPredicate) {
            super(id, predicate);
            this.blockPredicate = blockPredicate;
        }

        public boolean matches(ServerLevel level, BlockPos pos) {
            return this.blockPredicate.matches(level, pos);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext) {
            JsonObject jsonobject = super.serializeToJson(serializationContext);
            jsonobject.add("blockstate", this.blockPredicate.serializeToJson());
            return jsonobject;
        }
    }
}
