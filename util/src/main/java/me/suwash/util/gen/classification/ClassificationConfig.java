package me.suwash.util.gen.classification;

import java.util.List;

/**
 * 区分設定。
 */
@lombok.Setter
@lombok.Getter
public class ClassificationConfig {
    private String id;
    private String name;
    private String packageName;
    private List<ClassificationValue> values;
    private List<ClassificationValueGroup> groups;
}
