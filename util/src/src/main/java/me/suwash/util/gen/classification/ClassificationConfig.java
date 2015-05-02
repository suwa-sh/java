package me.suwash.util.gen.classification;

import java.util.List;

public class ClassificationConfig {
    private String      id;
    private String      name;
    private String      packageName;
    private List<ClassificationValue> values;
    private List<ClassificationValueGroup> groups;

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
     * @return packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName
     *            セットする packageName
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return values
     */
    public List<ClassificationValue> getValues() {
        return values;
    }

    /**
     * @param values
     *            セットする values
     */
    public void setValues(List<ClassificationValue> values) {
        this.values = values;
    }

    /**
     * @return groups
     */
    public List<ClassificationValueGroup> getGroups() {
        return groups;
    }

    /**
     * @param groups
     *            セットする groups
     */
    public void setGroups(List<ClassificationValueGroup> groups) {
        this.groups = groups;
    }

}
