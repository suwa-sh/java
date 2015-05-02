package me.suwash.ddd.policy.layer.sv;

import me.suwash.ddd.policy.GenericLayerSuperType;

/**
 * サービス層のLayerSuperType基底クラス。
 *
 * @param <IN> InBean
 * @param <OUT> OutBean
 */
public abstract class GenericService<IN extends InBean, OUT extends OutBean<IN>> extends GenericLayerSuperType<IN, OUT> implements Service<IN, OUT> {
}
