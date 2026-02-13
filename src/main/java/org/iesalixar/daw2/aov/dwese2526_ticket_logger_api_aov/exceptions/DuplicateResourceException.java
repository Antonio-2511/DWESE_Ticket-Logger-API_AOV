package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions;

public class DuplicateResourceException extends RuntimeException {

    private final String resource;
    private final String field;
    private final Object value;

    public DuplicateResourceException(String resource, String field, Object value) {
        super("Duplicate " + resource + " (" + field + "=" + value + ")");
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

    public String getResource() {
        return resource;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }
}
