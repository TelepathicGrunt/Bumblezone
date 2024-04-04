package com.telepathicgrunt.the_bumblezone.mixin.util;

import net.minecraft.FileUtil;
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

    @Redirect(method = "isPathPortable(Ljava/nio/file/Path;)Z", at = @At(value = "INVOKE", target = "Ljava/util/regex/Pattern;matcher(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;", ordinal = 0))
    private static Matcher bumblezone$fixMC268617(Pattern instance, CharSequence input) {
        return RESERVED_WINDOWS_FILENAMES_BUMBLEZONE.matcher(input);
    }
}
