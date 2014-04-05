import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

public class IdentMe {
    static final HttpString ACCESS_CONTROL_HEADER = HttpString.tryFromString("Access-Control-Allow-Origin");

    public static void main(String[] args) {
        Undertow.builder()
                .addHttpListener(8091, "localhost")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        final String forwardedFor = exchange.getRequestHeaders().getFirst("X-Forwarded-For");

                        final String clientIP = (forwardedFor == null) ?
                                exchange.getSourceAddress().getAddress().getHostAddress() :
                                forwardedFor.split(",")[0];

                        final HeaderMap responseHeaders = exchange.getResponseHeaders();
                        responseHeaders.add(Headers.CONTENT_TYPE, "text/plain");
                        responseHeaders.add(ACCESS_CONTROL_HEADER, "*");
                        exchange.getResponseSender().send(clientIP);
                    }
                }).build().start();
    }
}
