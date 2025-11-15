package models.exceptions;

public class InvalidOrMissingHashPasswordException extends Exception {
    public InvalidOrMissingHashPasswordException(String message){
        super(message);
    }
}
