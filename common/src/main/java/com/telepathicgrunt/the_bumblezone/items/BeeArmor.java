package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.beearmor.BeeArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class BeeArmor extends ArmorItem {
    private final int variant;
    private final boolean transTexture;

    public BeeArmor(ArmorMaterial material, EquipmentSlot slot, Properties properties, int variant, boolean transTexture) {
        super(material, slot, properties);
        this.variant = variant;
        this.transTexture = transTexture;
    }

    public int getVariant() {
        return variant;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slot, isSelected);
        if (entity instanceof Player player) {
            Inventory inventory = player.getInventory();
            if (slot < inventory.items.size()) return;
            if (slot >= inventory.items.size() + inventory.armor.size()) return;
            armorTick(stack, level, player);
        }
    }

    abstract void armorTick(ItemStack stack, Level level, Player player);

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (transTexture) {
            return Bumblezone.MODID + ":textures/models/armor/trans_bee_material_layer_" + variant + ".png";
        }
        else {
            return Bumblezone.MODID + ":textures/models/armor/bee_material_layer_" + variant + ".png";
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BeeArmorModel model;
            private int variant;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                if (this.model == null || (itemStack.getItem() instanceof BeeArmor beeArmor && variant != beeArmor.getVariant())) {

                    ModelPart layer = null;
                    if(itemStack.getItem() instanceof BeeArmor beeArmor) {
                        int newVariant = beeArmor.getVariant();
                        if(newVariant == 1) {
                            layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_1_LAYER_LOCATION);
                        }
                        else if(newVariant == 2) {
                            layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_2_LAYER_LOCATION);
                        }
                        this.variant = newVariant;
                    }

                    if(layer == null) {
                        layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_1_LAYER_LOCATION);
                    }

                    this.model = new BeeArmorModel(layer, armorSlot, entityLiving);
                }
                model.entityLiving = entityLiving;
                return this.model;
            }
        });
    }
}