package pl.sggw;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            listenAndServe(serverSocket);
        }

    }

    private static void listenAndServe(ServerSocket serverSocket) throws IOException, InterruptedException {
        Socket socket = serverSocket.accept();
        Thread thread = new Thread(() -> {
            try {
                serveRequest(socket);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private static void serveRequest(Socket socket) throws IOException, InterruptedException {
        InputStream socketInputStream = socket.getInputStream();
        OutputStream socketOutputStream = socket.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketOutputStream));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socketInputStream));
        String line = null;
        while (((line = reader.readLine()) != null) && line.length() > 0) {
            System.out.println("wczytane: " + line);
        }

        writer.write("HTTP/1.1 200 OK\n");
        writer.write("Connection: close\n");
        Thread.sleep(10* 1000);
        writer.write("Content-Type: text/html\n\n");
        writer.write("<head></head><body><b>Hello</b> World." + LocalTime.now() + "</body>\n\n");
        writer.flush();
        writer.close();
    }

}
