package com.braintest.exception;

/**
 * UnknownUnpackerException
 *
 * @author den, @date 29.09.2012 1:09:15
 */
public class UnpackerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnpackerNotFoundException(String msg) {
        super(msg);
    }

    public UnpackerNotFoundException(Exception e) {
        super(e);
    }
}
