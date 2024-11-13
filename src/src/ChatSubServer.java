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
                boolean loginCorrect = false;
                while (!loginCorrect) {
                    String username = readClient(socket);
                    String password = readClient(socket);
                    //code to check if username and password are correct store in variable goodInfo
                    boolean goodInfo = true;
                    if (goodInfo) {
                        writeClient("goodInfo", socket);
                        loginCorrect = true;
                    } else {
                        writeClient("badInfo", socket);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean portOpen() {
        return !running;
    }
    public static String readClient(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String s = in.readLine();
            in.close();
            return s;
        } catch (IOException e) {
            return "Socket is not connected";
        }
    }
    public static boolean writeClient(String msg, Socket socket) {
        return true;
    }
}
