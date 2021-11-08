package com.telepathicgrunt.bumblezone.advancements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.telepathicgrunt.bumblezone.Bumblezone;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RecipeDiscoveredTrigger extends SimpleCriterionTrigger<RecipeDiscoveredTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "recipe_discovered");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject jsonObject, EntityPredicate.Composite predicate, DeserializationContext deserializationContext) {
        return new Instance(predicate, fromJson(jsonObject));
    }

    public static ResourceLocation[] fromJson(JsonElement jsonElement) {
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            JsonArray jsonarray = jsonElement.getAsJsonObject().getAsJsonArray("recipes");
            ResourceLocation[] resourceLocations = new ResourceLocation[jsonarray.size()];

            for(int i = 0; i < resourceLocations.length; ++i) {
                resourceLocations[i] = new ResourceLocation(jsonarray.get(i).getAsString());
            }

            return resourceLocations;
        }
        else {
            return new ResourceLocation[0];
        }
    }

    public void trigger(ServerPlayer serverPlayer, ResourceLocation recipeRL) {
        super.trigger(serverPlayer, (currentItemStack) -> currentItemStack.matches(recipeRL));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final Set<ResourceLocation> resourceLocations;

        public Instance(EntityPredicate.Composite predicate, ResourceLocation[] resourceLocations) {
            super(RecipeDiscoveredTrigger.ID, predicate);
            this.resourceLocations = new HashSet<>((Arrays.asList(resourceLocations)));
        }

        public boolean matches(ResourceLocation recipeRL) {
            return this.resourceLocations.contains(recipeRL);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext) {
            JsonObject jsonobject = super.serializeToJson(serializationContext);
            String[] recipes = new String[this.resourceLocations.size()];
            int i = 0;
            for(ResourceLocation rl : this.resourceLocations) {
                recipes[i] = rl.toString();
                i++;
            }
            jsonobject.addProperty("recipes", Arrays.toString(recipes));
            return jsonobject;
        }
    }
}
