package dev.jaqobb.realisticbookshelves.listener;

import dev.jaqobb.realisticbookshelves.RealisticBookshelvesPlugin;
import dev.jaqobb.realisticbookshelves.bookshelf.Bookshelf;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerBlockBreakListener implements Listener {

	private RealisticBookshelvesPlugin plugin;

	public PlayerBlockBreakListener(RealisticBookshelvesPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getType() != Material.BOOKSHELF) {
			Bookshelf bookshelf = this.plugin.getBookshelfRepository().get(block.getLocation());
			if (bookshelf != null) {
				this.plugin.getBookshelfRepository().remove(bookshelf);
			}
			return;
		}
		Bookshelf bookshelf = this.plugin.getBookshelfRepository().get(block.getLocation());
		if (bookshelf != null) {
			for (ItemStack content : bookshelf.getContents()) {
				if (content == null) {
					continue;
				}
				block.getWorld().dropItemNaturally(block.getLocation(), content);
			}
			this.plugin.getBookshelfRepository().remove(bookshelf);
		}
	}
}
