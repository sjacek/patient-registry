package com.grinnotech.patientsorig;

import java.util.function.Supplier;

public class NotFoundExceptionSuppl implements Supplier<NotFoundException> {

	private String message;

	private Exception exception;

	public NotFoundExceptionSuppl(String message, Object... args) {
		this.message = String.format(message, args);
	}

	public NotFoundExceptionSuppl(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}

	@Override
	public NotFoundException get() {
		return new NotFoundException(message, exception);
	}
}
