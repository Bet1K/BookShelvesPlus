package dev.jaqobb.realisticbookshelves.listener;

import dev.jaqobb.realisticbookshelves.RealisticBookshelvesPlugin;
import dev.jaqobb.realisticbookshelves.bookshelf.Bookshelf;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryClickListener implements Listener {

	private RealisticBookshelvesPlugin plugin;

	public PlayerInventoryClickListener(RealisticBookshelvesPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getInventory();
		InventoryView inventoryView = event.getView();
		if (!this.plugin.getCurrentPages().containsKey(player.getUniqueId()) || !this.plugin.getCurrentBookshelves().containsKey(player.getUniqueId())) {
			return;
		}
		Bookshelf bookshelf = this.plugin.getCurrentBookshelves().get(player.getUniqueId());
		int slot = event.getRawSlot();
		if (slot < 0 || slot > bookshelf.getRows() * 9 + 9 - 1) {
			ItemStack currentItem = event.getCurrentItem();
			if (currentItem != null && currentItem.getType() != Material.ENCHANTED_BOOK && currentItem.getType() != Material.BOOK && currentItem.getType() != Material.KNOWLEDGE_BOOK && currentItem.getType() != Material.WRITABLE_BOOK && currentItem.getType() != Material.WRITTEN_BOOK) {
				event.setCancelled(true);
			}
			return;
		}
		ItemStack item = inventory.getItem(slot);
		if (item == null || item.getType() == Material.AIR) {
			return;
		}
		if (slot == bookshelf.getRows() * 9 + 9 - 5 && item.isSimilar(this.plugin.getCloseInventoryItem())) {
			event.setCancelled(true);
			player.closeInventory();
			return;
		}
		if (slot == bookshelf.getRows() * 9 + 9 - 6 && item.isSimilar(this.plugin.getPreviousPageItem())) {
			event.setCancelled(true);
			this.plugin.openBookshelf(player, this.plugin.getCurrentBookshelves().get(player.getUniqueId()), this.plugin.getCurrentPages().get(player.getUniqueId()) - 1, false);
			return;
		} else if (slot == bookshelf.getRows() * 9 + 9 - 4 && item.isSimilar(this.plugin.getNextPageItem())) {
			event.setCancelled(true);
			this.plugin.openBookshelf(player, this.plugin.getCurrentBookshelves().get(player.getUniqueId()), this.plugin.getCurrentPages().get(player.getUniqueId()) + 1, false);
			return;
		}
		ItemStack currentItem = event.getCurrentItem();
		if (currentItem != null && currentItem.getType() != Material.ENCHANTED_BOOK && currentItem.getType() != Material.BOOK && currentItem.getType() != Material.KNOWLEDGE_BOOK && currentItem.getType() != Material.WRITABLE_BOOK && currentItem.getType() != Material.WRITTEN_BOOK) {
			event.setCancelled(true);
		}
	}
}