package com.grinnotech.patients;

import java.util.function.Supplier;

public class NotFoundException extends PatientException {

	public NotFoundException(String message, Exception ex) {
		super(message, ex);
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Object... args) {
		super(String.format(message, args));
	}

	public static NotFoundException supplyNotFoundException(String message, Object... args) {
		Supplier<NotFoundException> supplier = () -> new NotFoundException(message, args);
		return supplier.get();
	}
}
