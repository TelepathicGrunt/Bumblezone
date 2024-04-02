package com.telepathicgrunt.the_bumblezone.mixin.util;

import net.minecraft.FileUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = FileUtil.class, priority = 700)
public abstract class FileUtilMixin {

    @ModifyArg(method = "<clinit>()V", at = @At(value = "INVOKE", target = "Ljava/util/regex/Pattern;compile(Ljava/lang/String;I)Ljava/util/regex/Pattern;"))
    private static String bumblezone$fixMC268617(String regex) {
        return ".*\\.|(?:CON|PRN|AUX|NUL|CLOCK$|CONIN$|CONOUT$|(?:COM|LPT)[¹²³0-9])(?:\\..*)?";
    }
}
