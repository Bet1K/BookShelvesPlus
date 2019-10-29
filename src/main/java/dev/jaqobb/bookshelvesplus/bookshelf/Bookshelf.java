package dev.jaqobb.bookshelvesplus.bookshelf;

import dev.jaqobb.bookshelvesplus.util.ItemUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public class Bookshelf implements ConfigurationSerializable {


	private Location location;
	private int rows;
	private int pages;
	private ItemStack[] contents;

	public Bookshelf(Location location, int rows, int pages, ItemStack[] contents) {
		this.location = location;
		this.rows = rows;
		this.pages = pages;
		this.contents = contents;
	}

	public Location getLocation() {
		return this.location;
	}

	public int getRows() {
		return this.rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPages() {
		return this.pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public ItemStack[] getContents() {
		return this.contents;
	}

	public void setContents(ItemStack[] contents) {
		this.contents = contents;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<>(4);
		data.put("location", this.location.serialize());
		data.put("rows", this.rows);
		data.put("pages", this.pages);
		data.put("contents", ItemUtils.itemStackArrayToBase64(this.contents));
		return data;
	}

	@SuppressWarnings("unchecked")
	public static Bookshelf deserialize(Map<String, Object> data) {
		Location location = Location.deserialize((Map<String, Object>) data.get("location"));
		int rows = (int) data.get("rows");
		int pages = (int) data.get("pages");
		ItemStack[] contents = ItemUtils.itemStackArrayFromBase64((String) data.get("contents"));
		return new Bookshelf(location, rows, pages, contents);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || this.getClass() != object.getClass()) {
			return false;
		}
		Bookshelf that = (Bookshelf) object;
		return new EqualsBuilder()
			.append(this.rows, that.rows)
			.append(this.pages, that.pages)
			.append(this.location, that.location)
			.append(this.contents, that.contents)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(this.location)
			.append(this.rows)
			.append(this.pages)
			.append(this.contents)
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("location", this.location)
			.append("rows", this.rows)
			.append("pages", this.pages)
			.append("contents", this.contents)
			.toString();
	}
}
