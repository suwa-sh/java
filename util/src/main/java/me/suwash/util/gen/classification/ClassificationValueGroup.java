package me.suwash.util.gen.classification;

import java.util.List;

/**
 * 区分値グループ。
 */
@lombok.Setter
@lombok.Getter
public class ClassificationValueGroup {
    private String id;
    private String name;
    private List<String> values;
    private String defaultValue;
}
