package com.rusinek.bitmexmonolith.exceptions.fileExceptions;

/**
 * Created by Adrian Rusinek on 28.03.2020
 **/

public class FileStorageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
