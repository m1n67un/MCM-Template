package com.mg.core.common.exception;

import com.mg.core.common.code.ErrorCode;
import lombok.Getter;

@Getter
public class MGException extends RuntimeException {

    private final ErrorCode errorCode;

    public MGException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
