package me.suwash.ddd.policy.layer.ap;

public interface IODto<IN extends InDto> extends InDto, OutDto<IN>{
}
