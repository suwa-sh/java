package me.suwash.ddd.policy.layer.sv;

import me.suwash.ddd.policy.GenericLayerSuperType;
import me.suwash.ddd.policy.Input;
import me.suwash.ddd.policy.Output;

/**
 * サービス層のLayerSuperType基底クラス。
 *
 * @param <I> InBean
 * @param <O> OutBean
 */
public abstract class GenericService<I extends Input, O extends Output<I>> extends GenericLayerSuperType<I, O> implements Service<I, O> {
}
