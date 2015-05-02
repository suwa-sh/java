package me.suwash.ddd.policy;

import java.util.Set;

import javax.validation.ConstraintViolation;

import me.suwash.ddd.classification.ProcessStatus;

/**
 * LayerSuperTypeパターンの基底クラス。
 *
 * @param <IN> 入力データモデル
 * @param <OUT> 出力データモデル
 */
public abstract class GenericLayerSuperType<IN extends Input, OUT extends Output<IN>> implements LayerSuperType<IN, OUT> {

    /* (非 Javadoc)
     * @see me.suwash.util.policy.LayerSuperType#execute(me.suwash.util.policy.Input)
     */
    @Override
    public OUT execute(IN input) {
        // validate
        Set<ConstraintViolation<IN>> violationSet = validate(input);
        if (! violationSet.isEmpty()) {
            OUT validateOutput = getOutput(input);
            validateOutput.setViolationSet(violationSet);
            validateOutput.setProcessStatus(ProcessStatus.Failure);
            return validateOutput;
        }

        // preExecute
        OUT preExecuteOutput = preExecute(input);
        if (ProcessStatus.Failure.equals(preExecuteOutput.getProcessStatus())) {
            return preExecuteOutput;
        }

        // main
        OUT mainExecuteOutput = mainExecute(input, preExecuteOutput);
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
    abstract protected OUT getOutput(IN input);

    /**
     * 前処理。
     * 必要に応じて実装してください。デフォルトの実装は、出力データモデルを生成して返却します。
     * 出力データモデルに、ProcessStatus.Failureを設定すると、後続の主処理を実行せずに処理を終了します。
     *
     * @param input 入力データモデル
     * @return 出力データモデル
     */
    protected OUT preExecute(IN input) {
        return getOutput(input);
    }

    /**
     * 主処理。
     * 出力データモデルに、ProcessStatus.Failureを設定すると、後続の後処理を実行せずに処理を終了します。
     *
     * @param input 入力データモデル
     * @return 出力データモデル
     */
    abstract protected OUT mainExecute(IN input, OUT preExecuteOutput);

    /**
     * 後処理。
     * 必要に応じて実装してください。デフォルトの実装は、主処理の出力データモデルをそのまま返却します。
     *
     * @param input 入力データモデル
     * @return 出力データモデル
     */
    protected OUT postExecute(IN input, OUT mainExecuteOutput) {
        return mainExecuteOutput;
    }
}
