package dev.jaqobb.bookshelvesplus;

import dev.jaqobb.bookshelvesplus.bookshelf.Bookshelf;
import dev.jaqobb.bookshelvesplus.bookshelf.BookshelfRepository;
import dev.jaqobb.bookshelvesplus.configuration.Configuration;
import dev.jaqobb.bookshelvesplus.configuration.bookshelf.Bookshelves;
import dev.jaqobb.bookshelvesplus.listener.PlayerBlockBreakListener;
import dev.jaqobb.bookshelvesplus.listener.PlayerInteractListener;
import dev.jaqobb.bookshelvesplus.listener.PlayerInventoryClickListener;
import dev.jaqobb.bookshelvesplus.listener.PlayerInventoryCloseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class BookShelvesPlusPlugin extends JavaPlugin {

	private Configuration configuration;
	private Bookshelves bookshelves;
	private BookshelfRepository bookshelfRepository;
	private ItemStack closeInventoryItem;
	private ItemStack nextPageItem;
	private ItemStack previousPageItem;
	private Map<UUID, Integer> currentPages = new HashMap<>(16);
	private Map<UUID, Bookshelf> currentBookshelves = new HashMap<>(16);

	@Override
	public void onEnable() {
		this.configuration = new Configuration(this, 1);
		this.bookshelves = new Bookshelves(this);
		this.bookshelfRepository = new BookshelfRepository(this);
		{
			this.closeInventoryItem = new ItemStack(Material.getMaterial(this.configuration.getString("close-inventory-item.type")));
			ItemMeta closeInventoryItemMeta = this.closeInventoryItem.getItemMeta();
			closeInventoryItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.configuration.getString("close-inventory-item.name")));
			closeInventoryItemMeta.setLore(
				this.configuration.getStringList("close-inventory-item.lore")
					.stream()
					.map(message -> ChatColor.translateAlternateColorCodes('&', message))
					.collect(Collectors.toList())
			);
			for (String enchantment : this.configuration.getConfigurationSection("close-inventory-item.enchantments").getKeys(false)) {
				//noinspection deprecation
				closeInventoryItemMeta.addEnchant(Enchantment.getByName(enchantment), this.configuration.getInt("close-inventory-item.enchantments." + enchantment), true);
			}
			for (String flag : this.configuration.getStringList("close-inventory-item.flags")) {
				closeInventoryItemMeta.addItemFlags(ItemFlag.valueOf(flag));
			}
			this.closeInventoryItem.setItemMeta(closeInventoryItemMeta);
		}
		{
			this.nextPageItem = new ItemStack(Material.getMaterial(this.configuration.getString("next-page-item.type")));
			ItemMeta nextPageItemMeta = this.nextPageItem.getItemMeta();
			nextPageItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.configuration.getString("next-page-item.name")));
			nextPageItemMeta.setLore(
				this.configuration.getStringList("next-page-item.lore")
					.stream()
					.map(message -> ChatColor.translateAlternateColorCodes('&', message))
					.collect(Collectors.toList())
			);
			for (String enchantment : this.configuration.getConfigurationSection("next-page-item.enchantments").getKeys(false)) {
				//noinspection deprecation
				nextPageItemMeta.addEnchant(Enchantment.getByName(enchantment), this.configuration.getInt("next-page-item.enchantments." + enchantment), true);
			}
			for (String flag : this.configuration.getStringList("next-page-item.flags")) {
				nextPageItemMeta.addItemFlags(ItemFlag.valueOf(flag));
			}
			this.nextPageItem.setItemMeta(nextPageItemMeta);
		}
		{
			this.previousPageItem = new ItemStack(Material.getMaterial(this.configuration.getString("previous-page-item.type")));
			ItemMeta previousPageItemMeta = this.previousPageItem.getItemMeta();
			previousPageItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.configuration.getString("previous-page-item.name")));
			previousPageItemMeta.setLore(
				this.configuration.getStringList("previous-page-item.lore")
					.stream()
					.map(message -> ChatColor.translateAlternateColorCodes('&', message))
					.collect(Collectors.toList())
			);
			for (String enchantment : this.configuration.getConfigurationSection("previous-page-item.enchantments").getKeys(false)) {
				//noinspection deprecation
				previousPageItemMeta.addEnchant(Enchantment.getByName(enchantment), this.configuration.getInt("previous-page-item.enchantments." + enchantment), true);
			}
			for (String flag : this.configuration.getStringList("previous-page-item.flags")) {
				previousPageItemMeta.addItemFlags(ItemFlag.valueOf(flag));
			}
			this.previousPageItem.setItemMeta(previousPageItemMeta);
		}
		this.getServer().getPluginManager().registerEvents(new PlayerBlockBreakListener(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerInventoryClickListener(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerInventoryCloseListener(this), this);
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

	public Bookshelves getBookshelves() {
		return this.bookshelves;
	}

	public BookshelfRepository getBookshelfRepository() {
		return this.bookshelfRepository;
	}

	public ItemStack getCloseInventoryItem() {
		return this.closeInventoryItem;
	}

	public ItemStack getNextPageItem() {
		return this.nextPageItem;
	}

	public ItemStack getPreviousPageItem() {
		return this.previousPageItem;
	}

	public Map<UUID, Integer> getCurrentPages() {
		return this.currentPages;
	}

	public Map<UUID, Bookshelf> getCurrentBookshelves() {
		return this.currentBookshelves;
	}

	public void openBookshelf(Player player, Bookshelf bookshelf, int page, boolean firstTime) {
		boolean hasNext = page < bookshelf.getPages();
		int entriesPerPage = bookshelf.getRows() * 9;
		int startIndex = (page - 1) * entriesPerPage;
		int endIndex = startIndex + entriesPerPage;
		if (endIndex >= bookshelf.getContents().length) {
			endIndex = bookshelf.getContents().length;
		}
		int pages = bookshelf.getPages();
		Inventory inventory = Bukkit.createInventory(null, bookshelf.getRows() * 9 + 9, ChatColor.translateAlternateColorCodes('&', this.configuration.getString("inventory.name").replace("{page}", String.valueOf(page)).replace("{pages}", String.valueOf(pages))));
		int index;
		int slot;
		for (slot = 0, index = startIndex; index < endIndex; index++) {
			inventory.setItem(slot, bookshelf.getContents()[index]);
			slot++;
		}
		inventory.setItem(bookshelf.getRows() * 9 + 9 - 5, this.closeInventoryItem);
		if (hasNext) {
			inventory.setItem(bookshelf.getRows() * 9 + 9 - 4, this.nextPageItem);
		}
		if (page != 1) {
			inventory.setItem(bookshelf.getRows() * 9 + 9 - 6, this.previousPageItem);
		}
		this.getServer().getScheduler().runTask(this, () -> {
			this.currentPages.put(player.getUniqueId(), page);
			this.currentBookshelves.put(player.getUniqueId(), bookshelf);
		});
		player.openInventory(inventory);
		if (!firstTime) {
			player.playSound(player.getLocation(), Sound.valueOf(this.configuration.getString("inventory.page-change-sound.name")), this.configuration.getFloat("inventory.page-change-sound.volume"), this.configuration.getFloat("inventory.page-change-sound.pitch"));
		}
	}
}
