package com.gerrywen.seckill.exception;

import com.gerrywen.seckill.result.CodeMsg;

/**
 * program: spring-boot-seckill->GlobalException
 * description:
 * author: gerry
 * created: 2020-03-05 20:33
 **/
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
