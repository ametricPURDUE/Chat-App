import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * The interface for the ChatMessageScreen File
 * Purdue University -- Fall 2024 -- CS18000 -- Team Project -- Chat App
 *
 * @author Nolan Shultz, AJ Metrick, Parshawn Haynes, Pranav Sangani, Edward Ju
 * @version December 8, 2024
 */
public interface ChatMessageScreenInterface {
    public void createMessagesScreen(JFrame frame, Color background, PrintWriter out, BufferedReader in);
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
}
