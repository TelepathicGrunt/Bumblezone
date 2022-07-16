package com.telepathicgrunt.the_bumblezone.entities.pollenpuffentityflowers;

import com.google.gson.annotations.Expose;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.TradeEntryObj;

import java.util.List;

public class EntriesObj {
    @Expose
    public String entity_type;

    @Expose
    public List<String> plants_to_spawn;

    public EntriesObj(String entity_type, List<String> plants_to_spawn) {
        this.entity_type = entity_type;
        this.plants_to_spawn = plants_to_spawn;
    }
}
