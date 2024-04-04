package com.telepathicgrunt.the_bumblezone.mixin.util;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.FileUtil;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(value = FileUtil.class, priority = 700)
public abstract class FileUtilMixin {

    @Unique
    private static final Pattern RESERVED_WINDOWS_FILENAMES_BUMBLEZONE = Pattern.compile(".*\\.|(?:CON|PRN|AUX|NUL|CLOCK\\$|CONIN\\$|CONOUT\\$|(?:COM|LPT)[¹²³0-9])(?:\\..*)?", Pattern.CASE_INSENSITIVE);

    @ModifyExpressionValue(method = "isPathPortable(Ljava/nio/file/Path;)Z", at = @At(value = "FIELD", target = "net/minecraft/FileUtil.RESERVED_WINDOWS_FILENAMES:Ljava/util/regex/Pattern;", opcode = Opcodes.GETSTATIC, ordinal = 0))
    private static Pattern bumblezone$fixMC268617(Pattern original) {
        return RESERVED_WINDOWS_FILENAMES_BUMBLEZONE;
    }
}
