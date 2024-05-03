package com.twins.storage.listener;

import com.intellectualcrafters.plot.object.Plot;
import com.plotsquared.bukkit.util.BukkitUtil;
import com.twins.storage.StoragePlugin;
import com.twins.storage.model.Storage;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlotListener implements Listener {

    @EventHandler
    private void onItemSpawn(ItemSpawnEvent event) {

        if (event.getEntity().getItemStack() == null || event.getEntity().getItemStack().getType() != Material.CACTUS)
            return;

        Plot plot = BukkitUtil.getLocation(event.getLocation()).getPlotAbs();
        if (plot == null)
            return;

        Storage storage = StoragePlugin.getInstance().getStorageService().get(plot.getId().toString());

        if (storage.getAmount() >= storage.getLimit())
            return;

        int set = Math.min(storage.getLimit(), storage.getAmount() + (StoragePlugin.RANDOM.nextInt(storage.getLevel() + 1) + 1));

        event.setCancelled(true);
        storage.setAmount(set);
        StoragePlugin.getInstance().getStorageService().update(storage);

    }

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {

        if (event.getItemDrop().getItemStack().getType() != Material.CACTUS)
            return;

        Plot plot = BukkitUtil.getLocation(event.getItemDrop().getLocation()).getPlotAbs();
        if (plot == null)
            return;

        Storage storage = StoragePlugin.getInstance().getStorageService().get(plot.getId().toString());

        if (storage.getAmount() >= storage.getLimit())
            return;

        event.setCancelled(true);
        storage.setAmount(storage.getAmount() + event.getItemDrop().getItemStack().getAmount());
        StoragePlugin.getInstance().getStorageService().update(storage);
        
    }

}
