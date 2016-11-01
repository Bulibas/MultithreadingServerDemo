package ServerSecondDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static ServerSecondDemo.ServerSecondDemo.getConnection;

/**
 * Created by PC on 13.10.2016 г..
 */
public class DatabaseAddFriendRequest implements DatabaseRequest{
    private BufferedReader reader;
    private PrintStream printout;
    private String username;


    public DatabaseAddFriendRequest(BufferedReader reader, PrintStream printout, String username) {
        this.setReader(reader);
        this.setPrintout(printout);
        this.setUsername(username);
    }

    @Override
    public void setReader(BufferedReader reader) {
        this.reader=reader;
    }

    public void setPrintout(PrintStream printout) {
        this.printout=printout;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        try {
            addFriend();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFriend() throws IOException {
        String input;

        System.out.println("adding...");
        input=reader.readLine();

        if (DatabaseFriendsRequest.friendsString.contains(input)) {
            printout.println("The user is already in your friends list!");
        }
        else {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = getConnection();
                System.out.println("adding started");

                PreparedStatement checkDataStatement = conn.prepareStatement("UPDATE users SET friends=CONCAT(friends,'"+input+"') WHERE username LIKE '"+getUsername()+"'");
                checkDataStatement.executeUpdate();

                printout.println("Added!");
                System.out.println("friend added...");

            } catch(Exception e) {
                System.out.println(e);
            } finally {
                System.out.println("function ended");
            }
        }


    }
}
