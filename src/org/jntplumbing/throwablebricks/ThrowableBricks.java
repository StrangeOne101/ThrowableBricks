package org.jntplumbing.throwablebricks;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jntplumbing.throwablebricks.configuration.ConfigManager;
import org.jntplumbing.throwablebricks.listeners.PlayerInteract;

public class ThrowableBricks extends JavaPlugin {
	
	public static ThrowableBricks plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		new ConfigManager(this);
		registerListeners();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerInteract(), this);
	}

}
