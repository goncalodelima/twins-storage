package com.twins.storage.command;

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.plotsquared.bukkit.util.BukkitUtil;
import com.twins.storage.StoragePlugin;
import com.twins.storage.inventory.StorageInventory;
import com.twins.storage.model.Storage;
import com.twins.storage.util.BukkitUtils;
import com.twins.storage.util.ItemBuilder;
import com.twins.storage.util.ItemNBT;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StorageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        if (args.length == 0 || !player.hasPermission("storage.admin")) {

            Location location = BukkitUtil.getLocation(player.getLocation());
            Plot plot = location.getPlotAbs();

            if (plot == null || !BukkitUtils.isPlotAdded(plot, player.getUniqueId())) {
                player.sendMessage("§cVocê precisa estar dentro do seu terreno para fazer isso.");
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
                return false;
            }

            Storage storage = StoragePlugin.getInstance().getStorageService().get(plot.getId().toString());

            if (storage == null) {
                StoragePlugin.getInstance().getStorageService().put(
                        storage = Storage
                                .builder()
                                .plot(plot)
                                .build()
                );
            }

            StoragePlugin.getInstance().getViewFrame().open(StorageInventory.class, player, storage);
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException exception) {
            player.sendMessage("§cNúmero inválido.");
            return false;
        }

        ItemStack storage = BukkitUtils.getStorageItem(player.getInventory().getContents());

        if (storage == null) {
            ItemStack itemStack = new ItemBuilder(Material.NETHER_STAR)
                    .name("§bLimite Armazém §8§l✦ §f" + StoragePlugin.FORMATTER.formatNumber(amount))
                    .setLore("§7Utilize este item para", "§7aumentar o armazenamento", "§7do seu armazém.")
                    .build();

            ItemNBT.setInt(itemStack, "plot-storage-limit", amount);
            player.getInventory().addItem(itemStack);
        } else {
            amount += ItemNBT.getInt(storage, "plot-storage-limit");

            storage = new ItemBuilder(storage)
                    .name("§bLimite Armazém §8§l✦ §f" + StoragePlugin.FORMATTER.formatNumber(amount))
                    .build();

            ItemNBT.setInt(storage, "plot-storage-limit", amount);
            player.getInventory().setItem(BukkitUtils.getStorageSlot(player.getInventory()), storage);
        }

        player.sendMessage("§aLimite de armazém dado com sucesso.");
        return true;
    }

}
