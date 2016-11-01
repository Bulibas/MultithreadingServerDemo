package ServerSecondDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static ServerSecondDemo.DatabaseFriendsRequest.friendsString;
import static ServerSecondDemo.ServerSecondDemo.getConnection;

/**
 * Created by PC on 15.10.2016 г..
 */
public class DatabasePhotoRequest implements DatabaseRequest{
    private BufferedReader reader;
    private PrintStream printout;
    private String username;

    public DatabasePhotoRequest(BufferedReader reader, PrintStream printout, String username) {
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

        String username = reader.readLine();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = getConnection();

            PreparedStatement checkDataStatement = conn.prepareStatement("SELECT * FROM users WHERE username LIKE '"+username+"' ");
            ResultSet searchResultSet = checkDataStatement.executeQuery();

            //checking the db resultset
            if(searchResultSet.next()) {
                if((searchResultSet.getString("profile").length()>100)) {
                    String photoString = searchResultSet.getString("profile");
                    printout.println(photoString);
                    printout.flush();
                    System.out.println("photo found");
                }
                else {
                    printout.println("no picture");
                    printout.flush();
                    System.out.println("no photo...");
                }
            }

            else {
                printout.println("no picture");
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
