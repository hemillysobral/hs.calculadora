import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;

public class Servidor {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Rota para a página HTML principal
        server.createContext("/", new FileHandler("index.html", "text/html"));

        // Rota para o CSS
        server.createContext("/style.css", new FileHandler("style.css", "text/css"));

        // Rota para o JS (Ajuste 1: adicionando este handler)
        server.createContext("/script.js", new FileHandler("script.js", "application/javascript"));

        server.setExecutor(null); // usa executor padrão
        server.start();
        System.out.println("Servidor rodando na porta 8080");
    }

    static class FileHandler implements HttpHandler {
        private final String filename;
        private final String contentType;

        public FileHandler(String filename, String contentType) {
            this.filename = filename;
            this.contentType = contentType;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File file = new File(filename);

            if (!file.exists()) {
                String response = "404 (Not Found)\n";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            byte[] bytes = Files.readAllBytes(file.toPath());
            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
}
