package com.twins.storage.inventory;

import com.twins.storage.StoragePlugin;
import com.twins.storage.model.Storage;
import com.twins.storage.util.BukkitUtils;
import com.twins.storage.util.ItemBuilder;
import com.twins.storage.util.ItemNBT;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.component.Pagination;
import me.devnatan.inventoryframework.context.Context;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.state.State;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StorageInventory extends View {

    private final ItemStack cactus = new ItemStack(Material.CACTUS, 64);
    private final List<String> players = new ArrayList<>();

    private final State<Pagination> limitState = buildComputedPaginationState(context -> Collections.singletonList(false))
            .layoutTarget('L')
            .elementFactory((context, bukkitItemComponentBuilder, i, slot) -> {

                if (!(context.getInitialData() instanceof Storage))
                    return;

                Storage storage = (Storage) context.getInitialData();


                if (players.contains(context.getPlayer().getName())) {
                    players.remove(context.getPlayer().getName());
                    bukkitItemComponentBuilder.withItem(new ItemBuilder(Material.ENDER_CHEST)
                            .name("§aAumentar limite")
                            .setLore(
                                    "§fQuanto maior for o limite, mais",
                                    "§fcactos o armazém poderá armazenar.",
                                    "",
                                    " §fLimite atual: §a" + StoragePlugin.FORMATTER.formatNumber(storage.getLimit()),
                                    "",
                                    "§ePara aumentar o limite, basta",
                                    "§eclicar no cheque de limite",
                                    "§ecom este menu aberto."
                            )
                            .build());
                } else {
                    players.add(context.getPlayer().getName());
                    bukkitItemComponentBuilder.withItem(new ItemBuilder(Material.CHEST)
                            .name("§aAumentar limite")
                            .setLore(
                                    "§fQuanto maior for o limite, mais",
                                    "§fcactos o armazém poderá armazenar.",
                                    "",
                                    " §fLimite atual: §a" + StoragePlugin.FORMATTER.formatNumber(storage.getLimit()),
                                    "",
                                    "§ePara aumentar o limite, basta",
                                    "§eclicar no cheque de limite",
                                    "§ecom este menu aberto."
                            )
                            .build());
                }

            })
            .build();

    private final State<ItemStack> farmState = computedState(context -> {

        if (!(context.getInitialData() instanceof Storage))
            return null;

        Storage storage = (Storage) context.getInitialData();

        return new ItemBuilder(Material.CACTUS)
                .name("§aCacto")
                .setLore(
                        "§fQuantidade: §e" + StoragePlugin.FORMATTER.formatNumber(storage.getAmount()) + "§8/§c" + StoragePlugin.FORMATTER.formatNumber(storage.getLimit()),
                        "§fBotão Esquerdo: §c§lVENDER",
                        "§fBotão Direito: §2§lCOLETAR"
                )
                .build();

    });

    private final State<ItemStack> dropState = computedState(context -> {

        if (!(context.getInitialData() instanceof Storage))
            return null;

        Storage storage = (Storage) context.getInitialData();

        return new ItemBuilder(Material.DROPPER)
                .name("§aAumentar renda")
                .setLore(
                        "§fQuanto maior for o nível, mais",
                        "§fdrops vão vir, e consequentemente",
                        "§fvai render mais dinheiro.",
                        "",
                        "§fNível atual: §a" + storage.getLevel(),
                        "§fPreço: §2§l$§a" + StoragePlugin.FORMATTER.formatNumber(15000000000000D + 5000000000000D * storage.getLevel()) + " §8(" + storage.getLevel() + " → " + (storage.getLevel() + 1) + ")"
                )
                .build();

    });

    @Override
    public void onFirstRender(RenderContext render) {

        if (!(render.getInitialData() instanceof Storage))
            return;

        Storage storage = (Storage) render.getInitialData();
        Player player = render.getPlayer();

        render.layoutSlot('F').onRender(r -> r.setItem(farmState.get(render))).onClick(click -> {

            if (storage.getAmount() <= 0) {
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
                return;
            }

            if (click.isLeftClick()) {

                if (storage.getLimit() <= 0) {
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
                    return;
                }

                int remove = Math.min(storage.getAmount(), storage.getLimit());
                storage.setAmount(storage.getAmount() - remove);
                StoragePlugin.getInstance().getVault().depositPlayer(player, 10000000 * remove);
            } else if (click.isRightClick()) {

                if (BukkitUtils.getFreeSlots(player.getInventory()) == 0 || storage.getAmount() < 64) {
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
                    return;
                }

                storage.setAmount(storage.getAmount() - 64);
                player.getInventory().addItem(cactus);
            }


            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
            click.update();
        });

        render.layoutSlot('D').onRender(r -> r.setItem(dropState.get(render))).onClick(click -> {

            double money = StoragePlugin.getInstance().getVault().getBalance(player);
            double price = 15000000000000D + 5000000000000D * storage.getLevel();

            if (money < price) {
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
                return;
            }

            StoragePlugin.getInstance().getVault().withdrawPlayer(player, price);
            storage.setLevel(storage.getLevel() + 1);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            click.update();
        });

    }

    @Override
    public void onInit(ViewConfigBuilder config) {
        config
                .title("Armazém")
                .size(4)
                .layout(
                        "         ",
                        "  F   D  ",
                        "      L  ",
                        "         ")
                .cancelOnClick()
                .cancelOnDrag()
                .cancelOnDrop()
                .cancelOnPickup()
                .build();

    }

    @Override
    public void onClick(SlotClickContext click) {

        if (!(click.getInitialData() instanceof Storage))
            return;

        Storage storage = (Storage) click.getInitialData();

        if (click.isOnEntityContainer()) {

            if (click.getItem() != null && click.getItem().getType() != Material.AIR) {

                int limit = ItemNBT.getInt(click.getItem(), "plot-storage-limit") * click.getItem().getAmount();

                if (limit > 0) {

                    click.getPlayer().getInventory().remove(click.getItem());
                    storage.setLimit(storage.getLimit() + limit);
                    click.getPlayer().playSound(click.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);

                    click.update();

                }

            }

        }

    }

    @Override
    public void onUpdate(@NotNull Context update) {
        limitState.get(update).forceUpdate();
    }

}
