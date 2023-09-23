package com.telepathicgrunt.the_bumblezone.client.blocks;

import com.google.gson.JsonObject;
import earth.terrarium.athena.api.client.utils.CtmUtils;
import net.minecraft.client.resources.model.Material;
import net.minecraft.util.GsonHelper;

import java.util.EnumMap;
import java.util.List;

public enum ConnectedTexture {
    NONE(true, "base"),
    FRONT(false, "front"),
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    ;

    private static final List<ConnectedTexture> VALUES = List.of(values());

    private final boolean required;
    private final String id;

    ConnectedTexture() {
        this.required = true;
        this.id = this.name().toLowerCase();
    }

    ConnectedTexture(boolean required, String id) {
        this.required = required;
        this.id = id;
    }

    public void read(EnumMap<ConnectedTexture, Material> materials, JsonObject object) {
        if (this.required || object.has(this.id)) {
            materials.put(this, CtmUtils.blockMat(GsonHelper.getAsString(object, this.id)));
        }
    }

    public static List<ConnectedTexture> list() {
        return VALUES;
    }
}
