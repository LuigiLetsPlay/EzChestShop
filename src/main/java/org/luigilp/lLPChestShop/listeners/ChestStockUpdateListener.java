package org.luigilp.lLPChestShop.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.luigilp.lLPChestShop.LLPChestShop;
import org.luigilp.lLPChestShop.model.Shop;

public final class ChestStockUpdateListener implements Listener {

    private final LLPChestShop plugin;

    public ChestStockUpdateListener(LLPChestShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        Shop shop = resolveShopFromInventory(inv);
        if (shop == null) return;

        plugin.getShopManager().refreshSign(shop);
    }

    private Shop resolveShopFromInventory(Inventory inv) {
        Location loc = inv.getLocation();
        if (loc == null) return null;

        Block base = loc.getBlock();
        Shop s = plugin.getShopManager().getByChest(base);
        if (s != null) return s;

        for (BlockFace face : new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST}) {
            Block adj = base.getRelative(face);
            s = plugin.getShopManager().getByChest(adj);
            if (s != null) return s;
        }

        return null;
    }
}
