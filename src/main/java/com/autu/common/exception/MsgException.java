package com.autu.common.exception;

public class MsgException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MsgException() {
    }

    public MsgException(String message) {
        super(message);
    }

    public MsgException(String message, Throwable cause) {
        super(message, cause);
    }

    public MsgException(Throwable cause) {
        super(cause);
    }

}
