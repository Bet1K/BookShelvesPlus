package dev.jaqobb.realisticbookshelves.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public final class ItemUtils {

	private ItemUtils() {
		throw new UnsupportedOperationException("Cannot create instance of this class");
	}

	public static String itemStackToBase64(ItemStack item) {
		try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(); BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(arrayOutputStream)) {
			objectOutputStream.writeObject(item);
			return Base64Coder.encodeLines(arrayOutputStream.toByteArray());
		} catch (Exception exception) {
			throw new RuntimeException("Could not encode ItemStack", exception);
		}
	}

	public static String itemStackArrayToBase64(ItemStack[] items) {
		try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(); BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(arrayOutputStream)) {
			objectOutputStream.writeInt(items.length);
			for (ItemStack item : items) {
				objectOutputStream.writeObject(item);
			}
			return Base64Coder.encodeLines(arrayOutputStream.toByteArray());
		} catch (Exception exception) {
			throw new RuntimeException("Could not encode ItemStack array", exception);
		}
	}

	public static ItemStack itemStackFromBase64(String data) {
		try (ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data)); BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(arrayInputStream)) {
			return (ItemStack) objectInputStream.readObject();
		} catch (Exception exception) {
			throw new RuntimeException("Could not decode ItemStack", exception);
		}
	}

	public static ItemStack[] itemStackArrayFromBase64(String data) {
		try (ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data)); BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(arrayInputStream)) {
			ItemStack[] items = new ItemStack[objectInputStream.readInt()];
			for (int index = 0; index < items.length; index++) {
				items[index] = (ItemStack) objectInputStream.readObject();
			}
			return items;
		} catch (Exception exception) {
			throw new RuntimeException("Could not decode ItemStack array", exception);
		}
	}
}