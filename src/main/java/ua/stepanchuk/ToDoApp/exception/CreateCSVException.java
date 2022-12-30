package ua.stepanchuk.ToDoApp.exception;

public class CreateCSVException extends Exception{

    public CreateCSVException() {
        super();
    }

    public CreateCSVException(String message) {
        super(message);
    }

    public CreateCSVException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateCSVException(Throwable cause) {
        super(cause);
    }
}
