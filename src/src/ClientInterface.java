import java.io.*;
import java.lang.*;
public interface ClientInterface {
    /**
     * A method to read the message the server sends
     *
     * @param in the reader to use
     * @return returns the received message from the server
     */
    public static String readServer(BufferedReader in) {
        try {
            String s = in.readLine();
            return s;
        } catch (IOException e) {
            return "Socket is not connected";
        }
    }
    /**
     * A method that sends the inputted message to the server
     *
     * @param msg the message to be sent
     * @param out the print writer that will be used
     * @return return true if sent successfully and false otherwise
     */
    public static void writeServer(String msg, PrintWriter out) {
        out.write(msg);
        out.println();
        out.flush();
    }
}
