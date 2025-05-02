package Utility;

public class ValidationResult {
    private String errorcode;
    private String message;

    public ValidationResult(String errorcode, String message) {
        this.errorcode = errorcode;
        this.message = message;
    }

    public String getCode() {
        return errorcode;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return errorcode == null || errorcode.isEmpty();
    }
}