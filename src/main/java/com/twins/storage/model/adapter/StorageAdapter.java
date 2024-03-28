package com.twins.storage.model.adapter;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import com.intellectualcrafters.plot.object.PlotId;
import com.twins.storage.model.Storage;
import pt.gongas.database.adapter.DatabaseAdapter;
import pt.gongas.database.executor.DatabaseQuery;

public class StorageAdapter implements DatabaseAdapter<Storage> {

    @Override
    public Storage adapt(DatabaseQuery query) {

        Plot plot = PlotArea.createGeneric("terreno").getPlot(PlotId.fromString((String) query.get("id")));
        if (plot == null)
            return null;

        return Storage.builder()
                        .plot(plot)
                        .amount((int) query.get("storage_amount"))
                        .limit((int) query.get("storage_limit"))
                        .level((int) query.get("storage_level"))
                        .build();
    }

}
