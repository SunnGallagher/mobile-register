package model;

public class ResponseErrorDTO<T> extends ResponseDTO<T> {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
