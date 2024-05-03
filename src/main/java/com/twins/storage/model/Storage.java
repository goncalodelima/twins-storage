package com.twins.storage.model;

import com.intellectualcrafters.plot.object.Plot;
import com.twins.storage.StoragePlugin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Storage {

    private Plot plot;
    private int amount;
    private int limit;
    private int level;

}
