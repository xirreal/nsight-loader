package dev.xirreal;

public class Result {
    int code = 0;
    String message = "";

    public Result(int code) {
        this.code = code;
        this.message = switch(code) {
            case 0 -> "OK";
            case -1 -> "Failure";
            case -2 -> "Invalid argument";
            case -3 -> "Injection failed";
            case -4 -> "Already injected";
            case -5 -> "Not injected";
            case -6 -> "Driver still loaded";
            case -7 -> "Invalid project";
            default -> "Unknown";
        };
    }

    @Override
    public String toString() {
        return message + " (" + code + ")";
    }
}
