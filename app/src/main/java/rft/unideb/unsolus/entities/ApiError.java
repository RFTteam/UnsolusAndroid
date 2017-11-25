package rft.unideb.unsolus.entities;

import java.util.List;
import java.util.Map;

/**
 * Created by Tibor on 2017. 11. 25..
 */

public class ApiError {
    String message;
    Map<String, List<String>> errors;

    public String getMessage() {
        return message;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }
}
