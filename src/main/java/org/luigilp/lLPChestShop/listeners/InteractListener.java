package org.luigilp.lLPChestShop.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.luigilp.lLPChestShop.LLPChestShop;
import org.luigilp.lLPChestShop.model.Shop;

public final class InteractListener implements Listener {

    private final LLPChestShop plugin;

    public InteractListener(LLPChestShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        Block clicked = event.getClickedBlock();
        if (clicked == null) return;

        Action action = event.getAction();

        if (plugin.getShopManager().isSignBlock(clicked)) {
            Shop shop = plugin.getShopManager().getBySign(clicked);
            if (shop == null) return;

            if (action == Action.LEFT_CLICK_BLOCK && isAxe(player.getInventory().getItemInMainHand()) && canRemove(player, shop)) {

                return;
            }

            event.setCancelled(true);

            if (action == Action.LEFT_CLICK_BLOCK) {
                plugin.openDetails(player, shop);
            } else if (action == Action.RIGHT_CLICK_BLOCK) {
                plugin.buy(player, shop);
            }
            return;
        }

        if (plugin.getShopManager().isChestBlock(clicked)) {
            Shop shop = plugin.getShopManager().getByChest(clicked);
            if (shop == null) return;

            boolean owner = shop.getOwner().equals(player.getUniqueId());
            boolean bypass = player.hasPermission("ezchestshop.bypass");

            if (!owner && !bypass) {
                event.setCancelled(true);
                plugin.openDetails(player, shop);
            }
        }
    }

    private boolean canRemove(Player p, Shop shop) {
        boolean owner = shop.getOwner().equals(p.getUniqueId());
        return (owner && p.hasPermission("ezchestshop.remove.own")) || p.hasPermission("ezchestshop.remove.any");
    }

    private boolean isAxe(ItemStack it) {
        if (it == null) return false;
        return it.getType().name().endsWith("_AXE");
    }
}
