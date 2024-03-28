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

    public void setAmount(int amount) {
        this.amount = amount;
        StoragePlugin.getInstance().getStorageService().update(this);
    }

    public void setLimit(int limit) {
        this.limit = limit;
        StoragePlugin.getInstance().getStorageService().update(this);
    }

    public void setLevel(int level) {
        this.level = level;
        StoragePlugin.getInstance().getStorageService().update(this);
    }

}
