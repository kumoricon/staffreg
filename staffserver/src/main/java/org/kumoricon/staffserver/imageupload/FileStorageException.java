package org.kumoricon.staffserver.imageupload;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String message, Exception ex) {
        super(message, ex);
    }
    public FileStorageException(String message) {
        super(message);
    }
}
