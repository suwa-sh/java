package me.suwash.ddd.policy.layer.sv;

import me.suwash.ddd.policy.Input;
import me.suwash.ddd.policy.LayerSuperType;
import me.suwash.ddd.policy.Output;

/**
 * サービス層のLayerSuperType。
 *
 * @param <I> InBean
 * @param <O> OutBean
 */
public interface Service<I extends Input, O extends Output<I>> extends LayerSuperType<I, O> {
}
