package dev.jaqobb.realisticbookshelves.listener;

import dev.jaqobb.realisticbookshelves.RealisticBookshelvesPlugin;
import dev.jaqobb.realisticbookshelves.bookshelf.Bookshelf;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class PlayerInventoryCloseListener implements Listener {

	private RealisticBookshelvesPlugin plugin;

	public PlayerInventoryCloseListener(RealisticBookshelvesPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInventoryClose(InventoryCloseEvent event) {
		UUID uniqueId = event.getPlayer().getUniqueId();
		if (!this.plugin.getCurrentPages().containsKey(uniqueId) || !this.plugin.getCurrentBookshelves().containsKey(uniqueId)) {
			return;
		}
		Bookshelf bookshelf = this.plugin.getCurrentBookshelves().get(uniqueId);
		int page = this.plugin.getCurrentPages().get(uniqueId);
		int entriesPerPage = bookshelf.getRows() * 9;
		int startIndex = (page - 1) * entriesPerPage;
		int endIndex = startIndex + entriesPerPage;
		if (endIndex >= bookshelf.getContents().length) {
			endIndex = bookshelf.getContents().length;
		}
		int slot;
		int index;
		for (slot = 0, index = startIndex; index < endIndex; index++) {
			bookshelf.getContents()[index] = event.getInventory().getItem(slot);
			slot++;
		}
		this.plugin.getCurrentPages().remove(uniqueId);
		this.plugin.getCurrentBookshelves().remove(uniqueId);
		this.plugin.getBookshelfRepository().save();
	}
}