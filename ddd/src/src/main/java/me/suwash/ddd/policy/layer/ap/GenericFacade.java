package me.suwash.ddd.policy.layer.ap;

import me.suwash.ddd.policy.GenericLayerSuperType;
import me.suwash.ddd.policy.LayerSuperType;

/**
 * アプリケーション層のLayerSuperType基底クラス。
 *
 * @param <IN> InDto
 * @param <OUT> OutDto
 */
public abstract class GenericFacade<IN extends InDto, OUT extends OutDto<IN>> extends GenericLayerSuperType<IN, OUT> implements LayerSuperType<IN, OUT> {
}
