import java.io.*;
import java.net.*;
import java.util.Arrays;

public class MainChatServer{
    private static boolean[] emptyPorts = new boolean[10];
    public static void main(String[] args){
        try {
            Thread[] servers = new Thread[10];
            // Creates 10 subservers for clients to connect to
            for (int i = 0; i < servers.length; i++) {
                servers[i] = new Thread(new ChatSubServer(2000 + i));
                servers[i].start();
            }
            Arrays.fill(emptyPorts, true);
            ServerSocket serverSocket = new ServerSocket(4242); //Creating default serverSocket
            while (true) {
                String output = "";
                boolean openPort = false;
                int serverNumber = 2000;
                Socket socket = serverSocket.accept(); //Accepts one client at a time
                PrintWriter out = new PrintWriter(socket.getOutputStream()); // Creates output stream for the client
                for (int i = 0; i < emptyPorts.length; i++) {
                    if(emptyPorts[i]){
                        serverNumber += i;
                        openPort = true;
                        output = serverNumber + ";" + i;
                        emptyPorts[i] = false;
                        break;
                    }
                }
                if (openPort) { //Sends client the port number of an open subserver and index of the subserver
                    out.write(output);
                    out.println();
                    out.flush();
                } else { //Tells client that there are no open servers
                    out.write("No Open Servers");
                    out.println();
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateEmptyServer(boolean b, int i) {
        emptyPorts[i] = b;
    }
}
