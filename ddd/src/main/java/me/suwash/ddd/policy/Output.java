package me.suwash.ddd.policy;

import java.util.Set;

import javax.validation.ConstraintViolation;

import me.suwash.ddd.classification.ProcessStatus;

/**
 * 出力データモデル。
 *
 * @param <IN> 入力データモデル
 */
public interface Output<IN extends Input> {
    /**
     * 入力データモデルを返します。
     *
     * @return 入力データモデル
     */
    IN getInput();

    /**
     * 妥当性検証結果セットを設定します。
     *
     * @param violationSet 妥当性検証結果セット
     */
    void setViolationSet(Set<ConstraintViolation<IN>> violationSet);

    /**
     * 妥当性検証結果セットを返します。
     *
     * @return 妥当性検証結果セット
     */
    Set<ConstraintViolation<IN>> getViolationSet();

    /**
     * 処理ステータスを設定します。
     *
     * @param processStatus 処理ステータス
     */
    void setProcessStatus(ProcessStatus processStatus);

    /**
     * 処理ステータスを返します。
     *
     * @return 処理ステータス
     */
    ProcessStatus getProcessStatus();
}
