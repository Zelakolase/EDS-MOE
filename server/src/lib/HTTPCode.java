package lib;

/**
 * HTTP Code headers
 * @author Morad A.
 */
public enum HTTPCode {
    OK("HTTP/1.1 200 OK"),
    CREATED("HTTP/1.1 201 Created"),
    NO_CONTENT("HTTP/1.1 204 No Content"),
    PARTIAL_CONTENT("HTTP/1.1 206 Partial Content"),
    MOVED_PERM("HTTP/1.1 301 Moved Permanently"),
    FOUND("HTTP/1.1 302 Found"),
    BAD_REQUEST("HTTP/1.1 400 Bad Request"),
    UNAUTHORIZED("HTTP/1.1 401 Unauthorized"),
    FORBIDDEN("HTTP/1.1 403 Forbidden"),
    NOT_FOUND("HTTP/1.1 404 Not Found"),
    METHOD_NOT_ALLOWED("HTTP/1.1 405 Method Not Allowed"),
    REQUEST_TIMEOUT("HTTP/1.1 408 Request Timeout"),
    URI_TOO_LONG("HTTP/1.1 414 URI Too Long"),
    PAYLOAD_TOO_LARG("HTTP/1.1 413 Payload Too Large"),
    TOO_MANY_REQUESTS("HTTP/1.1 429 Too Many Requests"),
    INTERNAL_SERVER_ERROR("HTTP/1.1 500 Internal Server Error"),
    BAD_GATEWAY("HTTP/1.1 502 Bad Gateway"),
    SERVICE_UNAVAILABLE("HTTP/1.1 503 Service Unavailable");

    private final String value;

    HTTPCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}