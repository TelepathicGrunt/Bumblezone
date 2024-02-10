package com.telepathicgrunt.the_bumblezone.utils;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

@Plugin(name = "LogSpamFiltering", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE)
public class LogFilters extends AbstractFilter {
    public static boolean filterActive = true;

    private static final String spamToSilence;
    static {
        // Don't ask. The drama is stupid and serves no purpose but clogs up user's logs and confuse them.
        // Let's just quiet the spam, so we can all go back to normal.
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("brick ");
        stringBuilder.append("about ");
        stringBuilder.append("to ");
        stringBuilder.append("fall ");
        stringBuilder.append("through ");
        stringBuilder.append("your ");
        stringBuilder.append("roof");
        spamToSilence = stringBuilder.toString();
    }

    @Override
    public Result filter(LogEvent event) {
        if (filterActive) {
            Message message = event.getMessage();
            if (message != null) {
                if (message.getFormattedMessage().contains(spamToSilence)) {
                    return Result.DENY;
                }
            }
        }

        return Result.NEUTRAL;
    }
}
