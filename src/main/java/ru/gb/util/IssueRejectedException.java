package ru.gb.util;

public class IssueRejectedException extends Exception {
    public IssueRejectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public IssueRejectedException(String message) {
        super(message);
    }
}