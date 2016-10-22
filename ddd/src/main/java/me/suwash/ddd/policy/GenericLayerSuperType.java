package me.suwash.ddd.policy;

import java.util.Set;

import javax.validation.ConstraintViolation;

import me.suwash.ddd.classification.ProcessStatus;

/**
 * LayerSuperTypeパターンの基底クラス。
 *
 * @param <I> 入力データモデル
 * @param <O> 出力データモデル
 */
public abstract class GenericLayerSuperType<I extends Input, O extends Output<I>> implements LayerSuperType<I, O> {

    /* (非 Javadoc)
     * @see me.suwash.util.policy.LayerSuperType#execute(me.suwash.util.policy.Input)
     */
    @Override
    public O execute(final I input) {
        // validate
        final Set<ConstraintViolation<I>> violationSet = validate(input);
        if (! violationSet.isEmpty()) {
            final O validateOutput = getOutput(input);
            validateOutput.setViolationSet(violationSet);
            validateOutput.setProcessStatus(ProcessStatus.Failure);
            return validateOutput;
        }

        // preExecute
        final O preExecuteOutput = preExecute(input);
        if (ProcessStatus.Failure.equals(preExecuteOutput.getProcessStatus())) {
            return preExecuteOutput;
        }

        // main
        final O mainExecuteOutput = mainExecute(input, preExecuteOutput);
        if (ProcessStatus.Failure.equals(mainExecuteOutput.getProcessStatus())) {
            return mainExecuteOutput;
        }

        // postExecute
        return postExecute(input, mainExecuteOutput);
    }

    /**
     * 出力データモデルを返却するように実装してください。
     *
     * @param input 入力データモデル
     * @return 出力データモデル
     */
    protected abstract O getOutput(I input);

    /**
     * 前処理。
     * 必要に応じて実装してください。デフォルトの実装は、出力データモデルを生成して返却します。
     * 出力データモデルに、ProcessStatus.Failureを設定すると、後続の主処理を実行せずに処理を終了します。
     *
     * @param input 入力データモデル
     * @return 出力データモデル
     */
    protected O preExecute(final I input) {
        return getOutput(input);
    }

    /**
     * 主処理。
     * 出力データモデルに、ProcessStatus.Failureを設定すると、後続の後処理を実行せずに処理を終了します。
     *
     * @param input 入力データモデル
     * @param preExecuteOutput 前処理の出力データモデル
     * @return 出力データモデル
     */
    protected abstract O mainExecute(I input, O preExecuteOutput);

    /**
     * 後処理。
     * 必要に応じて実装してください。デフォルトの実装は、主処理の出力データモデルをそのまま返却します。
     *
     * @param input 入力データモデル
     * @param mainExecuteOutput 主処理の出力データモデル
     * @return 出力データモデル
     */
    protected O postExecute(final I input, final O mainExecuteOutput) {
        return mainExecuteOutput;
    }
}
