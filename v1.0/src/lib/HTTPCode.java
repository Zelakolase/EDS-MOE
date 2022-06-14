package lib;

public interface HTTPCode {
	String OK = "HTTP/1.1 200 OK";
	String CREATED = "HTTP/1.1 201 Created";
	String NO_CONTENT = "HTTP/1.1 204 No Content";
	String PARTIAL_CONTENT = "HTTP/1.1 206 Partial Content";
	String MOVED_PERM = "HTTP/1.1 301 Moved Permanently";
	String FOUND = "HTTP/1.1 302 Found";
	String BAD_REQUEST = "HTTP/1.1 400 Bad Request";
	String UNAUTHORIZED = "HTTP/1.1 401 Unauthorized";
	String FORBIDDEN = "HTTP/1.1 403 Forbidden";
	String NOT_FOUND = "HTTP/1.1 404 Not Found";
	String METHOD_NOT_ALLOWED = "HTTP/1.1 405 Method Not Allowed";
	String REQUEST_TIMEOUT = "HTTP/1.1 408 Request Timeout";
	String URI_TOO_LONG = "HTTP/1.1 414 URI Too Long";
	String PAYLOAD_TOO_LARG = "HTTP/1.1 413 Payload Too Large";
	String TOO_MANY_REQUESTS = "HTTP/1.1 429 Too Many Requests";
	String INTERNAL_SERVER_ERROR = "HTTP/1.1 500 Internal Server Error";
	String BAD_GATEWAY = "HTTP/1.1 502 Bad Gateway";
	String SERVICE_UNAVAILABLE = "HTTP/1.1 503 Service Unavailable";
}
