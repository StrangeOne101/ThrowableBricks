package org.jntplumbing.throwablebricks.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
			return;
		}
		ItemStack itemStack = event.getItem();
		int slot = player.getInventory().getHeldItemSlot();
		if (itemStack == null) {
			return;
		} else if (itemStack.getType() != Material.CLAY_BRICK) {
			return;
		}
		player.getInventory().setItem(slot, new ItemStack(Material.CLAY_BRICK, itemStack.getAmount() - 1));
		Item item = player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.CLAY_BRICK));
		item.setVelocity(player.getLocation().getDirection().multiply(1));
	}

}
