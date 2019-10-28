package dev.jaqobb.realisticbookshelves.listener;

import dev.jaqobb.realisticbookshelves.RealisticBookshelvesPlugin;
import dev.jaqobb.realisticbookshelves.bookshelf.Bookshelf;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

	private RealisticBookshelvesPlugin plugin;

	public PlayerInteractListener(RealisticBookshelvesPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if (!event.hasBlock()) {
			return;
		}
		Block block = event.getClickedBlock();
		if (block.getType() != Material.BOOKSHELF) {
			Bookshelf bookshelf = this.plugin.getBookshelfRepository().get(block.getLocation());
			if (bookshelf != null) {
				this.plugin.getBookshelfRepository().remove(bookshelf);
			}
			return;
		}
		event.setCancelled(true);
		Bookshelf bookshelf = this.plugin.getBookshelfRepository().get(block.getLocation());
		if (bookshelf == null) {
			bookshelf = new Bookshelf(block.getLocation(), this.plugin.getConfiguration().getInt("default.rows"), this.plugin.getConfiguration().getInt("default.pages"), new ItemStack[this.plugin.getConfiguration().getInt("default.rows") * 9 * this.plugin.getConfiguration().getInt("default.pages")]);
			this.plugin.getBookshelfRepository().add(bookshelf);
		}
		this.plugin.openBookshelf(event.getPlayer(), bookshelf, 1, true);
	}
}
