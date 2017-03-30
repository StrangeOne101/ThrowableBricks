package org.jntplumbing.throwablebricks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class BrickContactEvent extends Event {

	public BrickContactEvent(Item item, Location location, Player player, Block block) {
		this.brick = item;
		this.loc = location;
		this.thrower = player;
		this.blockHit = block;
		this.isBlockHit = true;
		this.isEntityHit = false;
	}
	
	public BrickContactEvent(Item item, Location location, Player player, LivingEntity entity) {
		this.brick = item;
		this.loc = location;
		this.thrower = player;
		this.entityHit = entity;
		this.isBlockHit = false;
		this.isEntityHit = true;
	}
	
	public Item brick;
	public Player thrower;
	public Location loc;
	public boolean isBlockHit;
	public boolean isEntityHit;
	public Block blockHit;
	public LivingEntity entityHit;
	
	@Override
	public HandlerList getHandlers() {
		return null;
	}
	
	public Item getBrickEntity() {
		return brick;
	}
	
	public ItemStack getBrickItemStack() {
		return brick.getItemStack();
	}
	
	public Player getThrower() {
		return thrower;
	}
	
	public boolean isEntityHit() {
		return isEntityHit;
	}
	
	public boolean isBlockHit() {
		return isBlockHit;
	}
	
	public Block getBlockHit() {
		return blockHit;
	}
	
	public LivingEntity getEntityHit() {
		return entityHit;
	}

}
