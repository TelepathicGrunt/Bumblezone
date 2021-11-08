package com.telepathicgrunt.the_bumblezone.advancements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RecipeDiscoveredTrigger extends AbstractCriterionTrigger<RecipeDiscoveredTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "recipe_discovered");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public RecipeDiscoveredTrigger.Instance createInstance(JsonObject jsonObject, EntityPredicate.AndPredicate predicate, ConditionArrayParser conditionArrayParser) {
        return new RecipeDiscoveredTrigger.Instance(predicate, fromJson(jsonObject));
    }

    public static ResourceLocation[] fromJson(@Nullable JsonElement jsonElement) {
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

    public void trigger(ServerPlayerEntity serverPlayerEntity, ResourceLocation recipeRL) {
        super.trigger(serverPlayerEntity, (currentItemStack) -> currentItemStack.matches(recipeRL));
    }

    public static class Instance extends CriterionInstance {
        private final Set<ResourceLocation> resourceLocations;

        public Instance(EntityPredicate.AndPredicate predicate, ResourceLocation[] resourceLocations) {
            super(RecipeDiscoveredTrigger.ID, predicate);
            this.resourceLocations = new HashSet<>((Arrays.asList(resourceLocations)));
        }

        public boolean matches(ResourceLocation recipeRL) {
            return this.resourceLocations.contains(recipeRL);
        }

        @Override
        public JsonObject serializeToJson(ConditionArraySerializer conditionArraySerializer) {
            JsonObject jsonobject = super.serializeToJson(conditionArraySerializer);
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
