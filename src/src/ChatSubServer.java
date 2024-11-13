import java.io.*;
import java.net.*;

class ChatSubServer implements Runnable {
    private int portNumber;
    ServerSocket serverSocket;
    private boolean running = false;
    public ChatSubServer(int portNumber) {
        this.portNumber = portNumber;
    }
    public void run() {
        try {
            serverSocket = new ServerSocket(portNumber);
            Socket socket = serverSocket.accept();
            running = true;
            while(running) {
                running = false;
                running = true;
            }
            running = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean portOpen() {
        return !running;
    }
    public String readClient(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String s = in.readLine();
            in.close();
            return s;
        } catch (IOException e) {
            return "Socket is not connected";
        }
    }
}
