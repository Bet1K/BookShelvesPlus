package dev.jaqobb.bookshelvesplus.configuration;

import dev.jaqobb.bookshelvesplus.bookshelf.Bookshelf;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Configuration {

	static {
		ConfigurationSerialization.registerClass(Bookshelf.class, "Bookshelf");
	}

	private JavaPlugin plugin;
	private int currentVersion;
	private String fileName;
	private File file;
	private FileConfiguration fileConfiguration;
	private Map<String, Object> values;

	public Configuration(JavaPlugin plugin) {
		this(plugin, "configuration");
	}

	public Configuration(JavaPlugin plugin, int currentVersion) {
		this(plugin, currentVersion, "configuration");
	}

	public Configuration(JavaPlugin plugin, String fileName) {
		this(plugin, -1, fileName);
	}

	public Configuration(JavaPlugin plugin, int currentVersion, String fileName) {
		this.plugin = plugin;
		this.currentVersion = currentVersion;
		this.fileName = fileName;
		this.file = new File(this.plugin.getDataFolder(), this.fileName + ".yml");
		if (!this.file.exists()) {
			this.plugin.saveResource(this.fileName + ".yml", false);
		}
		this.reload();
	}

	public boolean isSet(String path) {
		if (path == null) {
			throw new NullPointerException("path cannot be null");
		}
		if (path.isEmpty()) {
			throw new IllegalArgumentException("path cannot be empty");
		}
		return this.values.containsKey(path);
	}

	public Object get(String path) {
		return this.get(path, null);
	}

	public Object get(String path, Object defaultValue) {
		if (!this.isSet(path)) {
			return defaultValue;
		}
		return this.values.getOrDefault(path, defaultValue);
	}

	public void set(String path, Object value) {
		if (path == null) {
			throw new NullPointerException("path cannot be null");
		}
		if (path.isEmpty()) {
			throw new IllegalArgumentException("path cannot be empty");
		}
		this.values.put(path, value);
	}

	public String getString(String path) {
		return this.getString(path, null);
	}

	public String getString(String path, String defaultValue) {
		Object value = this.get(path, defaultValue);
		if (value == null) {
			return defaultValue;
		}
		return value.toString();
	}

	public boolean isString(String path) {
		return this.get(path) instanceof String;
	}

	public int getInt(String path) {
		return this.getInt(path, 0);
	}

	public int getInt(String path, int defaultValue) {
		Object value = this.get(path, defaultValue);
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		if (value instanceof Integer) {
			return (int) value;
		}
		try {
			return Integer.parseInt(value.toString());
		} catch (NumberFormatException exception) {
			return defaultValue;
		}
	}

	public boolean isInt(String path) {
		return this.get(path) instanceof Integer;
	}

	public boolean getBoolean(String path) {
		return this.getBoolean(path, false);
	}

	public boolean getBoolean(String path, boolean defaultValue) {
		Object value = this.get(path, defaultValue);
		if (!(value instanceof Boolean)) {
			return defaultValue;
		}
		return (boolean) value;
	}

	public boolean isBoolean(String path) {
		return this.get(path) instanceof Boolean;
	}

	public char getChar(String path) {
		return this.getChar(path, '\u0000');
	}

	public char getChar(String path, char defaultValue) {
		Object value = this.get(path, defaultValue);
		if (value instanceof String) {
			String string = (String) value;
			if (string.isEmpty()) {
				return defaultValue;
			}
			return string.charAt(0);
		}
		if (!(value instanceof Character)) {
			return defaultValue;
		}
		return (char) value;
	}

	public boolean isChar(String path) {
		return this.get(path) instanceof Character;
	}

	public byte getByte(String path) {
		return this.getByte(path, (byte) 0);
	}

	public byte getByte(String path, byte defaultValue) {
		Object value = this.get(path, defaultValue);
		if (value instanceof Number) {
			return ((Number) value).byteValue();
		}
		if (value instanceof Byte) {
			return (byte) value;
		}
		try {
			return Byte.parseByte(value.toString());
		} catch (NumberFormatException exception) {
			return defaultValue;
		}
	}

	public boolean isByte(String path) {
		return this.get(path) instanceof Byte;
	}

	public short getShort(String path) {
		return this.getShort(path, (short) 0);
	}

	public short getShort(String path, short defaultValue) {
		Object value = this.get(path, defaultValue);
		if (value instanceof Number) {
			return ((Number) value).shortValue();
		}
		if (value instanceof Short) {
			return (short) value;
		}
		try {
			return Short.parseShort(value.toString());
		} catch (NumberFormatException exception) {
			return defaultValue;
		}
	}

	public boolean isShort(String path) {
		return this.get(path) instanceof Short;
	}

	public double getDouble(String path) {
		return this.getDouble(path, 0.0D);
	}

	public double getDouble(String path, double defaultValue) {
		Object value = this.get(path, defaultValue);
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		if (value instanceof Double) {
			return (double) value;
		}
		try {
			return Double.parseDouble(value.toString());
		} catch (NumberFormatException exception) {
			return defaultValue;
		}
	}

	public boolean isDouble(String path) {
		return this.get(path) instanceof Double;
	}

	public long getLong(String path) {
		return this.getLong(path, 0L);
	}

	public long getLong(String path, long defaultValue) {
		Object value = this.get(path, defaultValue);
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		if (value instanceof Long) {
			return (long) value;
		}
		try {
			return Long.parseLong(value.toString());
		} catch (NumberFormatException exception) {
			return defaultValue;
		}
	}

	public boolean isLong(String path) {
		return this.get(path) instanceof Long;
	}

	public float getFloat(String path) {
		return this.getFloat(path, 0.0F);
	}

	public float getFloat(String path, float defaultValue) {
		Object value = this.get(path, defaultValue);
		if (value instanceof Number) {
			return ((Number) value).floatValue();
		}
		if (value instanceof Float) {
			return (float) value;
		}
		try {
			return Float.parseFloat(value.toString());
		} catch (NumberFormatException exception) {
			return defaultValue;
		}
	}

	public boolean isFloat(String path) {
		return this.get(path) instanceof Float;
	}

	public List<?> getList(String path) {
		return this.getList(path, null);
	}

	public List<?> getList(String path, List<?> defaultValue) {
		Object value = this.get(path, defaultValue);
		if (value == null) {
			return defaultValue;
		}
		if (!(value instanceof List)) {
			return defaultValue;
		}
		return (List<?>) value;
	}

	public boolean isList(String path) {
		return this.get(path) instanceof List;
	}

	public List<String> getStringList(String path) {
		List<?> list = this.getList(path);
		if (list == null) {
			return new ArrayList<>(0);
		}
		List<String> result = new ArrayList<>(list.size());
		for (Object object : list) {
			if (object instanceof String || object instanceof Integer || object instanceof Boolean || object instanceof Character || object instanceof Byte || object instanceof Short || object instanceof Double || object instanceof Long || object instanceof Float) {
				result.add(String.valueOf(object));
			}
		}
		return result;
	}

	public List<Integer> getIntegerList(String path) {
		List<?> list = this.getList(path);
		if (list == null) {
			return new ArrayList<>(0);
		}
		List<Integer> result = new ArrayList<>(list.size());
		for (Object object : list) {
			if (object instanceof Integer) {
				result.add((Integer) object);
			} else if (object instanceof String) {
				try {
					result.add(Integer.valueOf((String) object));
				} catch (Exception ignored) {
				}
			} else if (object instanceof Character) {
				result.add((int) (Character) object);
			} else if (object instanceof Number) {
				result.add(((Number) object).intValue());
			}
		}
		return result;
	}

	public List<Boolean> getBooleanList(String path) {
		List<?> list = this.getList(path);
		if (list == null) {
			return new ArrayList<>(0);
		}
		List<Boolean> result = new ArrayList<>(list.size());
		for (Object object : list) {
			if (object instanceof Boolean) {
				result.add((Boolean) object);
			} else if (object instanceof String) {
				if (Boolean.TRUE.toString().equals(object)) {
					result.add(true);
				} else if (Boolean.FALSE.toString().equals(object)) {
					result.add(false);
				}
			}
		}
		return result;
	}

	public List<Character> getCharacterList(String path) {
		List<?> list = this.getList(path);
		if (list == null) {
			return new ArrayList<>(0);
		}
		List<Character> result = new ArrayList<>(list.size());
		for (Object object : list) {
			if (object instanceof Character) {
				result.add((Character) object);
			} else if (object instanceof String) {
				String string = (String) object;
				if (string.length() == 1) {
					result.add(string.charAt(0));
				}
			} else if (object instanceof Number) {
				result.add((char) ((Number) object).intValue());
			}
		}
		return result;
	}

	public List<Byte> getByteList(String path) {
		List<?> list = this.getList(path);
		if (list == null) {
			return new ArrayList<>(0);
		}
		List<Byte> result = new ArrayList<>(list.size());
		for (Object object : list) {
			if (object instanceof Byte) {
				result.add((Byte) object);
			} else if (object instanceof String) {
				try {
					result.add(Byte.valueOf((String) object));
				} catch (Exception ignored) {
				}
			} else if (object instanceof Character) {
				result.add((byte) ((Character) object).charValue());
			} else if (object instanceof Number) {
				result.add(((Number) object).byteValue());
			}
		}
		return result;
	}

	public List<Short> getShortList(String path) {
		List<?> list = this.getList(path);
		if (list == null) {
			return new ArrayList<>(0);
		}
		List<Short> result = new ArrayList<>(list.size());
		for (Object object : list) {
			if (object instanceof Short) {
				result.add((Short) object);
			} else if (object instanceof String) {
				try {
					result.add(Short.valueOf((String) object));
				} catch (Exception ignored) {
				}
			} else if (object instanceof Character) {
				result.add((short) ((Character) object).charValue());
			} else if (object instanceof Number) {
				result.add(((Number) object).shortValue());
			}
		}
		return result;
	}

	public List<Double> getDoubleList(String path) {
		List<?> list = this.getList(path);
		if (list == null) {
			return new ArrayList<>(0);
		}
		List<Double> result = new ArrayList<>(list.size());
		for (Object object : list) {
			if (object instanceof Double) {
				result.add((Double) object);
			} else if (object instanceof String) {
				try {
					result.add(Double.valueOf((String) object));
				} catch (Exception ignored) {
				}
			} else if (object instanceof Character) {
				result.add((double) (Character) object);
			} else if (object instanceof Number) {
				result.add(((Number) object).doubleValue());
			}
		}
		return result;
	}

	public List<Long> getLongList(String path) {
		List<?> list = this.getList(path);
		if (list == null) {
			return new ArrayList<>(0);
		}
		List<Long> result = new ArrayList<>(list.size());
		for (Object object : list) {
			if (object instanceof Long) {
				result.add((Long) object);
			} else if (object instanceof String) {
				try {
					result.add(Long.valueOf((String) object));
				} catch (Exception ignored) {
				}
			} else if (object instanceof Character) {
				result.add((long) (Character) object);
			} else if (object instanceof Number) {
				result.add(((Number) object).longValue());
			}
		}
		return result;
	}

	public List<Float> getFloatList(String path) {
		List<?> list = this.getList(path);
		if (list == null) {
			return new ArrayList<>(0);
		}
		List<Float> result = new ArrayList<>(list.size());
		for (Object object : list) {
			if (object instanceof Float) {
				result.add((Float) object);
			} else if (object instanceof String) {
				try {
					result.add(Float.valueOf((String) object));
				} catch (Exception ignored) {
				}
			} else if (object instanceof Character) {
				result.add((float) (Character) object);
			} else if (object instanceof Number) {
				result.add(((Number) object).floatValue());
			}
		}
		return result;
	}

	public List<Map<?, ?>> getMapList(String path) {
		List<?> list = this.getList(path);
		if (list == null) {
			return new ArrayList<>(0);
		}
		List<Map<?, ?>> result = new ArrayList<>(list.size());
		for (Object object : list) {
			if (object instanceof Map) {
				result.add((Map<?, ?>) object);
			}
		}
		return result;
	}

	public <T> T getObject(String path, Class<T> clazz) {
		return this.getObject(path, clazz, null);
	}

	public <T> T getObject(String path, Class<T> clazz, T defaultValue) {
		Object value = this.get(path, defaultValue);
		if (value == null) {
			return defaultValue;
		}
		if (clazz == null) {
			return defaultValue;
		}
		if (!clazz.isInstance(value)) {
			return defaultValue;
		}
		return clazz.cast(value);
	}

	public <T extends ConfigurationSerializable> T getSerializableObject(String path, Class<T> clazz) {
		return this.getObject(path, clazz);
	}

	public <T extends ConfigurationSerializable> T getSerializableObject(String path, Class<T> clazz, T defaultValue) {
		return this.getObject(path, clazz, defaultValue);
	}

	public Vector getVector(String path) {
		return this.getSerializableObject(path, Vector.class);
	}

	public Vector getVector(String path, Vector defaultValue) {
		return this.getSerializableObject(path, Vector.class, defaultValue);
	}

	public boolean isVector(String path) {
		return this.getSerializableObject(path, Vector.class) != null;
	}

	public OfflinePlayer getOfflinePlayer(String path) {
		return this.getSerializableObject(path, OfflinePlayer.class);
	}

	public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer defaultValue) {
		return this.getSerializableObject(path, OfflinePlayer.class, defaultValue);
	}

	public boolean isOfflinePlayer(String path) {
		return this.getSerializableObject(path, OfflinePlayer.class) != null;
	}

	public ItemStack getItemStack(String path) {
		return this.getSerializableObject(path, ItemStack.class);
	}

	public ItemStack getItemStack(String path, ItemStack defaultValue) {
		return this.getSerializableObject(path, ItemStack.class, defaultValue);
	}

	public boolean isItemStack(String path) {
		return this.getSerializableObject(path, ItemStack.class) != null;
	}

	public Color getColor(String path) {
		return this.getSerializableObject(path, Color.class);
	}

	public Color getColor(String path, Color defaultValue) {
		return this.getSerializableObject(path, Color.class, defaultValue);
	}

	public boolean isColor(String path) {
		return this.getSerializableObject(path, Color.class) != null;
	}

	public ConfigurationSection getConfigurationSection(String path) {
		Object value = this.get(path, null);
		if (value == null) {
			return this.createSection(path);
		}
		if (!(value instanceof ConfigurationSection)) {
			return null;
		}
		return (ConfigurationSection) value;
	}

	public boolean isConfigurationSection(String path) {
		return this.get(path) instanceof ConfigurationSection;
	}

	public ConfigurationSection createSection(String path) {
		if (path == null) {
			throw new NullPointerException("path cannot be null");
		}
		if (path.isEmpty()) {
			throw new IllegalArgumentException("path cannot be empty");
		}
		ConfigurationSection section = this.fileConfiguration.createSection(path);
		this.set(path, section);
		return section;
	}

	public ConfigurationSection createSection(String path, Map<?, ?> map) {
		if (path == null) {
			throw new NullPointerException("path cannot be null");
		}
		if (path.isEmpty()) {
			throw new IllegalArgumentException("path cannot be empty");
		}
		ConfigurationSection section = this.fileConfiguration.createSection(path, map);
		this.set(path, section);
		return section;
	}

	public void load() {
		try {
			String oldFileSuffix = ".old";
			File oldFile = new File(this.plugin.getDataFolder(), this.fileName + oldFileSuffix + ".yml");
			while (oldFile.exists()) {
				oldFileSuffix += ".old";
				oldFile = new File(this.plugin.getDataFolder(), this.fileName + oldFileSuffix + ".yml");
			}
			if (this.currentVersion != -1) {
				if (!this.fileConfiguration.isSet("version") || !this.fileConfiguration.isInt("version") || (this.fileConfiguration.getInt("version") != -1 && this.fileConfiguration.getInt("version") != this.currentVersion)) {
					this.plugin.getLogger().log(Level.INFO, this.fileName + ".yml file seems to be outdated, updating...");
					if (!this.file.renameTo(oldFile)) {
						this.plugin.getLogger().log(Level.WARNING, "Could not rename outdated " + this.fileName + ".yml file, update stopped.");
					} else {
						this.file = new File(this.plugin.getDataFolder(), this.fileName + ".yml");
						this.plugin.saveResource(this.fileName + ".yml", false);
						this.reload(false);
						this.plugin.getLogger().log(Level.INFO, this.fileName + ".yml file updated.");
					}
				}
			}
			this.values = new HashMap<>(48, 0.85F);
			this.load0(this.fileConfiguration, "");
		} catch (Exception exception) {
			this.plugin.getLogger().log(Level.WARNING, "Could not load " + this.fileName + ".yml file.", exception);
		}
	}

	private void load0(ConfigurationSection section, String prefix) {
		for (String key : section.getKeys(false)) {
			String newPrefix = prefix.isEmpty() ? key : prefix + "." + key;
			if (section.isConfigurationSection(key)) {
				this.values.put(newPrefix, section.getConfigurationSection(key));
				this.load0(section.getConfigurationSection(key), newPrefix);
			} else {
				this.values.put(newPrefix, section.get(key));
			}
		}
	}

	public void save() {
		try {
			this.values.forEach((key, value) -> this.fileConfiguration.set(key, value));
			this.fileConfiguration.save(this.file);
		} catch (Exception exception) {
			this.plugin.getLogger().log(Level.WARNING, "Could not save " + this.fileName + ".yml file.", exception);
		}
	}

	public void reload() {
		this.reload(true);
	}

	public void reload(boolean load) {
		this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
		InputStream configurationStream = this.plugin.getResource(this.fileName + ".yml");
		if (configurationStream != null) {
			this.fileConfiguration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(configurationStream, StandardCharsets.UTF_8)));
		}
		if (load) {
			this.load();
		}
	}
}