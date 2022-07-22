package com.telepathicgrunt.the_bumblezone.entities.pollenpuffentityflowers;

import com.google.gson.annotations.Expose;

import java.util.List;

public class EntriesCollectionObj {
    @Expose
    public List<EntriesObj> entries;

    public EntriesCollectionObj(List<EntriesObj> entries) {
        this.entries = entries;
    }
}
