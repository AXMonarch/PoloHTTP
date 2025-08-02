import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PoloHTTPServer {

    public static class HTTPResponse {
        public int statusCode;
        public String body;
        public String statusLine;

        public HTTPResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
            this.statusLine = "HTTP/1.1 " + statusCode + " " + getStatusText(statusCode);
        }

        private String getStatusText(int statusCode) {
            return switch (statusCode) {
                case 200 -> "OK";
                case 400 -> "Bad Request";
                case 404 -> "Not Found";
                default -> "";
            };
        }
    }

    public static HTTPResponse pathRouter(String path) {
        if (path == null) {
            return new HTTPResponse(400, "Bad Request");
        }
        return switch (path) {
            case "/" -> new HTTPResponse(200, "Welcome to the homepage!");
            case "/about" -> new HTTPResponse(200, "About us page");
            default -> new HTTPResponse(404, "Page not found");
        };
    }

    public HashMap<String, String> parseSocketResult(ArrayList<String> socketResult)
    {
        String[] firstLine = socketResult.get(0).split(" ");
        HashMap<String, String> socketMap = new HashMap<>();

        socketMap.put("Method", firstLine[0].trim());
        socketMap.put("Path", firstLine[1].trim());
        socketMap.put("Version", firstLine[2].trim());

        List<String> restOfSocketResult = socketResult.subList(1, socketResult.size());
        for (String line : restOfSocketResult) {
            int colonIndex = line.indexOf(':');
            if (colonIndex != -1) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                socketMap.put(key, value);
            }
        }

        return socketMap;
    }

    public void genericLoggingSystem(String statusLine, HashMap<String, String> headers, String body) {
          PrintWriter writer = new PrintWriter(System.out, true);

          writer.print(statusLine + "\r\n");
          for (String key : headers.keySet()) {
              writer.print(key + ": " + headers.get(key) + "\r\n");
          }
          writer.print("\r\n");
          writer.print(body + "\r\n");
          writer.flush();
    }

    public PoloHTTPServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        ArrayList<String> socketResult = new ArrayList<>();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        headers.put("Connection", "close");

        while (true) {
            System.out.println("Listening on port 8080");
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            socketResult.clear();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                socketResult.add(line);
                System.out.println(line);
            }

            HashMap<String, String> socketResultMap = parseSocketResult(socketResult);
            String path = socketResultMap.get("Path");
            HTTPResponse response = pathRouter(path);
            headers.put("Content-Length", String.valueOf(response.body.length()));
            System.out.println(socketResultMap);

            genericLoggingSystem(response.statusLine, headers, response.body);

            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        new PoloHTTPServer();
    }
}
