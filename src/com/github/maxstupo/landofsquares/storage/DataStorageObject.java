package com.github.maxstupo.landofsquares.storage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataStorageObject is a map of key to value entries. The values can be any of these types: <code> Boolean, String, Integer, Float,
 * Double, Long, Byte, DataStorageObject</code>, and all listed types in array form.
 * 
 * @author Maxstupo
 */
public class DataStorageObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private Map<String, Object> data = new HashMap<>();
	private boolean caseSensitive = true;

	/**
	 * Create a new StorageObject, all keys are case-sensitive.
	 */
	public DataStorageObject() {
		this(true);
	}

	/**
	 * Create a new StorageObject.
	 * 
	 * @param caseSensitive
	 *            if true keys will be matched with case.
	 */
	public DataStorageObject(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	/**
	 * Create a new StorageObject, and copy all data from given KVSObject into this one.
	 * 
	 * @param obj
	 *            the StorageObject to copy.
	 */
	public DataStorageObject(DataStorageObject obj) {
		this.data = new HashMap<>(obj.getMap());
	}

	public String[] getArrayString(String key) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof String[])
				return (String[]) value;
		}
		return null;
	}

	public int[] getArrayInt(String key) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof int[])
				return (int[]) value;
		}
		return null;
	}

	public float[] getArrayFloat(String key) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof float[])
				return (float[]) value;
		}
		return null;
	}

	public double[] getArrayDouble(String key) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof double[])
				return (double[]) value;
		}
		return null;
	}

	public long[] getArrayLong(String key) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof long[])
				return (long[]) value;
		}
		return null;
	}

	public boolean[] getArrayBoolean(String key) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof boolean[])
				return (boolean[]) value;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<DataStorageObject> getList(String key) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof List<?>)
				return (List<DataStorageObject>) value;
		}
		return null;
	}

	public DataStorageObject getStorageObject(String key) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof DataStorageObject)
				return (DataStorageObject) value;
		}
		return null;
	}

	public String getString(String key, String defaultValue) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof String)
				return (String) value;
		}
		return defaultValue;
	}

	public String getString(String key) {
		return getString(key, "");
	}

	public int getInt(String key, int defaultValue) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof Integer)
				return (int) value;
		}
		return defaultValue;
	}

	public int getInt(String key) {
		return getInt(key, 0);
	}

	public float getFloat(String key, float defaultValue) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof Float)
				return (float) value;
		}
		return defaultValue;
	}

	public float getFloat(String key) {
		return getFloat(key, 0f);
	}

	public double getDouble(String key, double defaultValue) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof Double)
				return (double) value;
		}
		return defaultValue;
	}

	public double getDouble(String key) {
		return getDouble(key, 0D);
	}

	public long getLong(String key, long defaultValue) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof Long)
				return (long) value;
		}
		return defaultValue;
	}

	public long getLong(String key) {
		return getLong(key, 0);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		Object value = getObject(key);
		if (value != null) {
			if (value instanceof Boolean)
				return (boolean) value;
		}
		return defaultValue;
	}

	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	/* ################################################################################# */
	public void set(String key, boolean[] value) {
		setObject(key, value);
	}

	public void set(String key, long[] value) {
		setObject(key, value);
	}

	public void set(String key, double[] value) {
		setObject(key, value);
	}

	public void set(String key, float[] value) {
		setObject(key, value);
	}

	public void set(String key, int[] value) {
		setObject(key, value);
	}

	public void set(String key, String[] value) {
		setObject(key, value);
	}

	public void set(String key, List<DataStorageObject> value) {
		setObject(key, value);
	}

	public void set(String key, DataStorageObject value) {
		setObject(key, value);
	}

	public void set(String key, String value) {
		setObject(key, value);
	}

	public void set(String key, int value) {
		setObject(key, value);
	}

	public void set(String key, float value) {
		setObject(key, value);
	}

	public void set(String key, double value) {
		setObject(key, value);
	}

	public void set(String key, long value) {
		setObject(key, value);
	}

	public void set(String key, boolean value) {
		setObject(key, value);
	}

	private void setObject(String key, Object value) {
		data.put(caseSensitive ? key : key.toLowerCase(), value);
	}

	/**
	 * Returns true if this StorageObject has the given key.
	 * 
	 * @param key
	 *            the key to be tested.
	 * @return true if this StorageObject has the given key.
	 */
	public boolean hasKey(String key) {
		return data.containsKey(caseSensitive ? key : key.toLowerCase());
	}

	/**
	 * Returns true if this StorageObject contains no key-value mappings.
	 * 
	 * @return true if this StorageObject contains no key-value mappings.
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}

	/**
	 * Remove all key-value mappings from this StorageObject.
	 */
	public void clear() {
		data.clear();
	}

	/**
	 * Remove the mapping associated with the given key.
	 * 
	 * @param key
	 *            the key that is associated to the mapping.
	 */
	public void remove(String key) {
		data.remove(key);
	}

	private Object getObject(String key) {
		return data.get(caseSensitive ? key : key.toLowerCase());
	}

	/**
	 * Returns the map containing the key-value mappings.
	 * 
	 * @return the map containing the key-value mappings.
	 */
	public Map<String, Object> getMap() {
		return data;
	}

}
