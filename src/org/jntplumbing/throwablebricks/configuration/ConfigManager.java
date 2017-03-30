package org.jntplumbing.throwablebricks.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
	
	public ConfigManager(Plugin plugin) {
		FileConfiguration config = plugin.getConfig();
		config.options().copyDefaults(true);
		
		//config.addDefault("", "");
		
		plugin.saveConfig();
	}

}
