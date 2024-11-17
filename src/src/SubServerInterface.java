import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;

public interface SubServerInterface {
    public boolean portOpen();
    /**
     * A method to read the message the client sends
     *
     * @param in the reader to use
     * @return returns the received message from the server
     */
    public static String readClient(BufferedReader in) {
        try {
            String s = in.readLine();
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return "Socket is not connected1";
        }
    }

    /**
     * a method to send data to the client
     * @param msg the message to be sent
     * @param out the printwriter to use
     * @return returns true if sent successfully and false otherwise
     */
    public static void writeClient(String msg, PrintWriter out) {
        // writes and sends the msg to the client, then flushes the writer
        out.write(msg);
        out.println();
        out.flush();
    }
}
