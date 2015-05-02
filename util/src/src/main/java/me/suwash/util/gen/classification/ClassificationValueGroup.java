package me.suwash.util.gen.classification;

import java.util.List;

public class ClassificationValueGroup {
    private String       id;
    private String       name;
    private List<String> values;
    private String       defaultValue;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            セットする id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            セットする name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return values
     */
    public List<String> getValues() {
        return values;
    }

    /**
     * @param values
     *            セットする values
     */
    public void setValues(List<String> values) {
        this.values = values;
    }

    /**
     * @return defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue
     *            セットする defaultValue
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }}
