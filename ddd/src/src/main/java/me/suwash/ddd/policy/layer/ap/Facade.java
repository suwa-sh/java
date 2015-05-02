package me.suwash.ddd.policy.layer.ap;

import me.suwash.ddd.policy.LayerSuperType;

/**
 * アプリケーション層のLayerSuperType。
 *
 * @param <IN> InDto
 * @param <OUT> OutDto
 */
public interface Facade<IN extends InDto, OUT extends OutDto<IN>> extends LayerSuperType<IN, OUT> {
}
