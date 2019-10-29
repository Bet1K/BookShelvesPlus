package dev.jaqobb.bookshelvesplus.configuration.bookshelf;

import dev.jaqobb.bookshelvesplus.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class Bookshelves extends Configuration {

	public Bookshelves(JavaPlugin plugin) {
		super(plugin, "bookshelves");
	}

	public Bookshelves(JavaPlugin plugin, int currentVersion) {
		super(plugin, currentVersion, "bookshelves");
	}

	public Bookshelves(JavaPlugin plugin, String fileName) {
		super(plugin, fileName);
	}

	public Bookshelves(JavaPlugin plugin, int currentVersion, String fileName) {
		super(plugin, currentVersion, fileName);
	}
}
