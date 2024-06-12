package dan.plugin.manhunt.utils;

import java.util.HashMap;
import java.util.Map;

public class OptionManager {
    private final Map<String, Object> options = new HashMap<>();

    // Method to set an option
    public void setOption(String key, Object value) {
        if (value instanceof Boolean || value instanceof Integer || value instanceof Float) {
            options.put(key, value);
        } else {
            throw new IllegalArgumentException("Value must be a boolean, int, or float");
        }
    }

    // Generic method to get an option, with a default value if the option is not found
    public <T> T getOption(String key, T defaultValue) {
        return options.containsKey(key) ? (T) options.get(key) : defaultValue;
    }

    /**
     * gets the option stored at the option key. Can be a float, int or boolean.
     * @param key
     * @return returns the option stored, or null. Be careful because the option may not exist.
     * @param <T>
     */
    public <T> T getOption(String key) {
        return getOption(key, null);
    }

    // Specific getters for convenience and type safety
    public boolean getBooleanOption(String key, boolean defaultValue) {
        Object value = options.get(key);
        return value instanceof Boolean ? (Boolean) value : defaultValue;
    }

    public boolean getBooleanOption(String key) {
        return getBooleanOption(key, false);
    }

    public int getIntOption(String key, int defaultValue) {
        Object value = options.get(key);
        return value instanceof Integer ? (Integer) value : defaultValue;
    }

    public int getIntOption(String key) {
        return getIntOption(key, -1);
    }

    public float getFloatOption(String key, float defaultValue) {
        Object value = options.get(key);
        return value instanceof Float ? (Float) value : defaultValue;
    }

    public float getFloatOption(String key) {
        return getFloatOption(key, -1);
    }
}
