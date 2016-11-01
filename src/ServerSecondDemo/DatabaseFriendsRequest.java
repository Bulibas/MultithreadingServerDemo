package ServerSecondDemo;

import ServerSecondDemo.DatabaseRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static ServerSecondDemo.ServerSecondDemo.getConnection;

/**
 * Created by PC on 12.10.2016 г..
 */
public class DatabaseFriendsRequest implements DatabaseRequest{
    private BufferedReader reader;
    private PrintStream printout;
    private String username;
    public static String friendsString;


    public DatabaseFriendsRequest(BufferedReader reader, PrintStream printout, String username) {
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
            search();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void search() throws IOException {
        System.out.println("browsing...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = getConnection();
            System.out.println("browse started");

            PreparedStatement checkDataStatement = conn.prepareStatement("SELECT * FROM users WHERE username LIKE '"+getUsername()+"' ");
            ResultSet searchResultSet = checkDataStatement.executeQuery();

            //checking the db resultset
            if(searchResultSet.next()) {
                if((searchResultSet.getString("friends"))!=null) {
                    friendsString = searchResultSet.getString("friends");
                    printout.println(friendsString);
                    printout.flush();
                    System.out.println("friends found:" + friendsString);
                }
                else {
                    printout.println("not found");
                    printout.flush();
                    System.out.println("no results...");
                }
            }

            else {
                printout.println("not found");
                printout.flush();
                System.out.println("no results...");
            }

        } catch(Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("function ended");
        }
    }
}
