package dev.jaqobb.realisticbookshelves.bookshelf;

import dev.jaqobb.realisticbookshelves.RealisticBookshelvesPlugin;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;

public class BookshelfRepository {

	private RealisticBookshelvesPlugin plugin;
	private Map<Location, Bookshelf> bookshelves = new HashMap<>(100, 0.85F);

	public BookshelfRepository(RealisticBookshelvesPlugin plugin) {
		this.plugin = plugin;
		this.load();
	}

	public Collection<Bookshelf> getAll() {
		return Collections.unmodifiableCollection(this.bookshelves.values());
	}

	public Bookshelf get(Location location) {
		return this.bookshelves.get(location);
	}

	public void add(Bookshelf bookshelf) {
		this.bookshelves.put(bookshelf.getLocation(), bookshelf);
	}

	public void remove(Bookshelf bookshelf) {
		this.bookshelves.remove(bookshelf.getLocation());
	}

	@SuppressWarnings("unchecked")
	public void load() {
		for (Bookshelf bookshelf : (List<Bookshelf>) this.plugin.getBookshelves().getList("bookshelves")) {
			this.bookshelves.put(bookshelf.getLocation(), bookshelf);
		}
	}

	public void save() {
		this.plugin.getBookshelves().set("bookshelves", new ArrayList<>(( this.bookshelves.values())));
		this.plugin.getBookshelves().save();
	}
}
