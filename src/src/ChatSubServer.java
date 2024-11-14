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

    /**
     * a method to send data to the client
     * @param msg the message to be sent
     * @param socket the client to send to
     * @return returns true if sent successfully and false otherwise
     */
    public static boolean writeClient(String msg, Socket socket) {
        // makes sure the socket is valid
        try (Socket sckt = socket) {
            // creates the PrintWriter object to write to client
            PrintWriter writer = new PrintWriter(sckt.getOutputStream());
            // writes and sends the msg to the client, then flushes the writer
            writer.write(msg);
            writer.println();
            writer.flush();
            // returns true if everything works correctly
            return true;
        } catch (IOException e) {
            // returns false if the socket is invalid
            return false;
        }
    }
}
