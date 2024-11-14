import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        int serverPort;
        int serverIndex;
        boolean hasServer = true;
        try {
            Socket mainSocket = new Socket("localhost", 4242); // Connects to main server
            BufferedReader reader = new BufferedReader(new InputStreamReader(mainSocket.getInputStream()));
            PrintWriter out = new PrintWriter(mainSocket.getOutputStream());
            String subserverPort = reader.readLine(); // Recieves port number and index of an open subserver from the main server
            if (subserverPort.equals("No Open Servers")) {
                hasServer = false;
                out.write("NoServerFound");
                out.println();
                out.flush();
                mainSocket.close();
                reader.close();
                out.close();
                System.out.println("No Open Servers found");
            } else {
                serverPort = Integer.parseInt(subserverPort.substring(0, subserverPort.indexOf(";")));
                serverIndex = Integer.parseInt(subserverPort.substring(subserverPort.indexOf(";") + 1));
                Socket subSocket = new Socket("localhost", serverPort);
                out.write("ReceivedServer");
                out.println();
                out.flush();
                mainSocket.close();
                reader.close();
                out.close();
                System.out.println("Server Port : " +  serverPort);
                System.out.println("Server Index : " + serverIndex);
                System.out.println("Welcome!");
                System.out.println("Please enter username");
                Scanner scan = new Scanner(System.in);
                boolean continueLogin = true;
                String username;
                while (continueLogin) {
                    username = scan.nextLine();
                    writeServer(username, subSocket);
                    System.out.println("Please enter password");
                    writeServer(scan.nextLine(), subSocket);
                    String loginCheck = readServer(subSocket);
                    if (loginCheck.equals("goodInfo")) {
                        System.out.println("Login Successful, Welcome " + username);
                        continueLogin = false;
                    } else {
                        System.out.println("Login Failed, please try again");
                    }
                }
            }
            mainSocket.close(); //closes connection to main server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method to read the message the server sends
     * @param socket the server to receive from
     * @return returns the received message from the server
     */
    public static String readServer(Socket socket) {
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
     * A method that sends the inputted message to the server
     * @param msg the message to be sent
     * @param socket the server socket that the message will be sent to
     * @return return true if sent successfully and false otherwise
     */
    public static boolean writeServer(String msg, Socket socket) {
        // makes sure the socket is valid
        try (Socket sckt = socket) {
            // creates a print writer object to write to the server
            PrintWriter writer = new PrintWriter(sckt.getOutputStream());
            // writes the inputted msg to the server, then flushes the writer
            writer.write(msg);
            writer.println();
            writer.flush();
            // returns true if all things go smoothly
            return true;
        } catch (IOException e) {
            // if the socket is invalid return false
            return false;
        }
    }
}
