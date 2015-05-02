package me.suwash.ddd.policy.layer.sv;

import me.suwash.ddd.policy.LayerSuperType;

/**
 * サービス層のLayerSuperType。
 *
 * @param <IN> InBean
 * @param <OUT> OutBean
 */
public interface Service<IN extends InBean, OUT extends OutBean<IN>> extends LayerSuperType<IN, OUT>{
}
