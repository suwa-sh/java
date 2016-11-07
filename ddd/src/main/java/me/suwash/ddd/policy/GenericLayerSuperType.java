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
@lombok.extern.slf4j.Slf4j
public abstract class GenericLayerSuperType<I extends Input, O extends Output<I>> implements LayerSuperType<I, O> {

    /* (非 Javadoc)
     * @see me.suwash.util.policy.LayerSuperType#execute(me.suwash.util.policy.Input)
     */
    @Override
    public O execute(final I input) {
        // validate
        log.debug("START validate");
        final Set<ConstraintViolation<I>> violationSet = validate(input);
        log.debug("END   validate");
        if (! violationSet.isEmpty()) {
            final O validateOutput = getOutput(input);
            validateOutput.setViolationSet(violationSet);
            validateOutput.setProcessStatus(ProcessStatus.Failure);
            return validateOutput;
        }

        // preExecute
        log.debug("START preExecute");
        final O preExecuteOutput = preExecute(input);
        log.debug("END   preExecute");
        if (ProcessStatus.Failure.equals(preExecuteOutput.getProcessStatus())) {
            return preExecuteOutput;
        }

        // main
        log.debug("START mainExecute");
        final O mainExecuteOutput = mainExecute(input, preExecuteOutput);
        log.debug("END   mainExecute");
        if (ProcessStatus.Failure.equals(mainExecuteOutput.getProcessStatus())) {
            return mainExecuteOutput;
        }

        // postExecute
        log.debug("START postExecute");
        final O postExecuteOutput = postExecute(input, mainExecuteOutput);
        log.debug("END   postExecute");
        return postExecuteOutput;
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
