package me.suwash.ddd.policy.layer.ap;

import me.suwash.ddd.policy.GenericLayerSuperType;
import me.suwash.ddd.policy.Input;
import me.suwash.ddd.policy.LayerSuperType;
import me.suwash.ddd.policy.Output;

/**
 * アプリケーション層のLayerSuperType基底クラス。
 *
 * @param <I> InDto
 * @param <O> OutDto
 */
public abstract class GenericFacade<I extends Input, O extends Output<I>> extends GenericLayerSuperType<I, O> implements LayerSuperType<I, O> {
}
