package com.telecom.exceptions;

public class InvalidPhoneNumberFormatException extends RuntimeException {
    public InvalidPhoneNumberFormatException(String message) {
        super(message);
    }
}
