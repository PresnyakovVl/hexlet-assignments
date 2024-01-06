package exercise.exception;

public class ResourceNotFoundExceptionBadRequest extends RuntimeException {
    public ResourceNotFoundExceptionBadRequest(String message) {
        super(message);
    }
}
