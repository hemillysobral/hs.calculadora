import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;

public class Servidor {
    public static void main(String[] args) throws IOException {
        int port = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new FileHandler("index.html", "text/html"));
        server.createContext("/style.css", new FileHandler("style.css", "text/css"));
        // Adicione mais contextos se tiver JS, imagens etc.

        server.setExecutor(null); // default executor
        System.out.println("Servidor rodando na porta " + port);
        server.start();
    }

    static class FileHandler implements HttpHandler {
        private final String filePath;
        private final String contentType;

        FileHandler(String filePath, String contentType) {
            this.filePath = filePath;
            this.contentType = contentType;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File file = new File(filePath);
            if (!file.exists()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            exchange.getResponseHeaders().set("Content-Type", contentType);
            byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
}
