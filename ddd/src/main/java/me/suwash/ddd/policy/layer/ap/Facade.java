package me.suwash.ddd.policy.layer.ap;

import me.suwash.ddd.policy.Input;
import me.suwash.ddd.policy.LayerSuperType;
import me.suwash.ddd.policy.Output;

/**
 * アプリケーション層のLayerSuperType。
 *
 * @param <I> InDto
 * @param <O> OutDto
 */
public interface Facade<I extends Input, O extends Output<I>> extends LayerSuperType<I, O> {
}
