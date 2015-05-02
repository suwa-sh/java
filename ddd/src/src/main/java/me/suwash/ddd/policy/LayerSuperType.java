package me.suwash.ddd.policy;

import java.util.Set;

import javax.validation.ConstraintViolation;

/**
 * LayerSuperTypeパターン。
 *
 * @param <IN> 入力データモデル
 * @param <OUT> 出力データモデル
 */
public interface LayerSuperType<IN extends Input, OUT extends Output<IN>> {

    /**
     * 入力データモデルの妥当性検証。
     * JSR303 BeanValidation
     *
     * @param input 入力データモデル
     * @return 検証結果セット
     */
    abstract public Set<ConstraintViolation<IN>> validate(IN input);

    /**
     * 処理本体。
     *
     * @param input 入力データモデル
     * @return 出力データモデル
     */
    abstract public OUT execute(IN input);
}
