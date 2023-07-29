package com.telepathicgrunt.the_bumblezone.utils;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class GeneralUtilsClient {

    public static List<MutableComponent> autoWrappedTooltip(String lengthText, String wrappingText) {
        List<MutableComponent> list = new ArrayList<>();

        String translatedLengthString = Language.getInstance().getOrDefault(lengthText);

        if (translatedLengthString.length() > 10) {
            String translatedWrapString = Language.getInstance().getOrDefault(wrappingText);
            if (translatedWrapString.length() > translatedLengthString.length()) {
                list.add(Component.literal(translatedWrapString.substring(0, translatedLengthString.length())));
                list.add(Component.literal(translatedWrapString.substring(translatedLengthString.length())));
            }
            else {
                list.add(Component.translatable(wrappingText));
            }
        }
        else {
            list.add(Component.translatable(wrappingText));
        }

        return list;
    }
}
