package com.twins.storage;

import com.twins.storage.command.StorageCommand;
import com.twins.storage.database.DatabaseProvider;
import com.twins.storage.inventory.StorageInventory;
import com.twins.storage.listener.PlotListener;
import com.twins.storage.model.service.StorageFoundationService;
import com.twins.storage.model.service.StorageService;
import com.twins.storage.util.Formatter;
import lombok.Getter;
import me.devnatan.inventoryframework.ViewFrame;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

@Getter
public class StoragePlugin extends JavaPlugin {

    public static final Formatter FORMATTER = new Formatter();
    public static final Random RANDOM = new Random();
    private Economy vault;
    private StorageFoundationService storageService;
    private ViewFrame viewFrame;

    @Override
    public void onEnable() {

        RegisteredServiceProvider<Economy> registeredServiceProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (registeredServiceProvider == null) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();

        vault = registeredServiceProvider.getProvider();
        storageService = new StorageService(DatabaseProvider.create(getConfig().getConfigurationSection("database")));
        viewFrame = ViewFrame
                .create(this)
                .with(new StorageInventory())
                .register();

        Bukkit.getPluginCommand("storage").setExecutor(new StorageCommand());
        Bukkit.getPluginManager().registerEvents(new PlotListener(), this);

    }

    public static StoragePlugin getInstance() {
        return getPlugin(StoragePlugin.class);
    }

}
