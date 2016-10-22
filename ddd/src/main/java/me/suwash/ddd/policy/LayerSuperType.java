package me.suwash.ddd.policy;

import java.util.Set;

import javax.validation.ConstraintViolation;

/**
 * LayerSuperTypeパターン。
 *
 * @param <I> 入力データモデル
 * @param <O> 出力データモデル
 */
public interface LayerSuperType<I extends Input, O extends Output<I>> {

    /**
     * 入力データモデルの妥当性検証（JSR303 BeanValidation）。
     *
     * @param input 入力データモデル
     * @return 検証結果セット
     */
    Set<ConstraintViolation<I>> validate(I input);

    /**
     * 処理本体。
     *
     * @param input 入力データモデル
     * @return 出力データモデル
     */
    O execute(I input);
}
