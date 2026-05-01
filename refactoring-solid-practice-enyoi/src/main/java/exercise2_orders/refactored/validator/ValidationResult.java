package exercise2_orders.refactored.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private boolean valid;
    private final List<String> errors;

    public ValidationResult(List<String> errors) {
        this.valid = true;
        this.errors = errors;
    }

    public ValidationResult() {
        this.valid = true;
        this.errors = List.of();
    }

    public static ValidationResult invalid(List<String> error) {
        ValidationResult result = new ValidationResult(error);
        result.failure();
        return  result;
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    private void failure() {
        this.valid = false;
    }

    public static ValidationResult valid() {
        return new ValidationResult();
    }
}
