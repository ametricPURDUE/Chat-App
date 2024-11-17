import java.io.*;
import java.net.*;

public class MainChatServer implements MainServerInterface{
    private static boolean[] emptyPorts = new boolean[10];
    public static void main(String[] args){
        try {
            Thread[] servers = new Thread[10];
            ChatSubServer[] subservers = new ChatSubServer[10];
            // Creates 10 subservers for clients to connect to
            for (int i = 0; i < servers.length; i++) {
                subservers[i] = new ChatSubServer(2000 + i);
                servers[i] = new Thread(subservers[i]);
                servers[i].start();
            }
            ServerSocket serverSocket = new ServerSocket(4242); //Creating default serverSocket
            while (true) {
                System.out.print("Open Ports:");
                for (int i = 0; i < emptyPorts.length; i++) {
                    emptyPorts[i] = subservers[i].portOpen();
                    System.out.print(emptyPorts[i] + ",");
                }
                System.out.println();
                String output = "";
                boolean openPort = false;
                int serverNumber = 2000;
                Socket socket = serverSocket.accept(); //Accepts one client at a time
                PrintWriter out = new PrintWriter(socket.getOutputStream()); // Creates output stream for the client
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Creates input stream to read from client
                for (int i = 0; i < emptyPorts.length; i++) {
                    if(emptyPorts[i]){
                        System.out.println(i);
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
                String clientRecieved = reader.readLine();
                if (clientRecieved.equals("NoServerFound")) {
                    System.out.println("No open servers");
                } else if (clientRecieved.equals("ReceivedServer")) {
                    System.out.println("Client Received Server");
                }
                out.close();
                reader.close();
                socket.close();
                for (int i = 0; i < emptyPorts.length; i++) {
                    System.out.print(emptyPorts[i] + ",");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
