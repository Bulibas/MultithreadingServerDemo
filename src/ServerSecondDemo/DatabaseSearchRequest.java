package ServerSecondDemo;

import ServerSecondDemo.ServerSecondDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static ServerSecondDemo.ServerSecondDemo.getConnection;

/**
 * Created by PC on 11.10.2016 г..
 */
public class DatabaseSearchRequest implements DatabaseRequest {
    private BufferedReader reader;
    private PrintStream printout;
    private String mail,username;

    public DatabaseSearchRequest(BufferedReader reader, PrintStream printout) {
        this.setReader(reader);
        this.setPrintout(printout);
    }

    @Override
    public void setReader(BufferedReader reader) {
        this.reader=reader;
    }

    public void setPrintout(PrintStream printout) {
        this.printout=printout;
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
        String input;
        System.out.println("searching...");
        input=reader.readLine();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = getConnection();
            System.out.println("search started");

            PreparedStatement checkDataStatement = conn.prepareStatement("SELECT * FROM users WHERE username LIKE '"+input+
                    "' OR mail LIKE '"+input+"'");
            ResultSet searchResultSet = checkDataStatement.executeQuery();

            //checking the db resultset
            if(searchResultSet.next()) {
                this.mail = searchResultSet.getString("mail");
                this.username = searchResultSet.getString("username");
                printout.println("found");
                printout.println(this.username);
                printout.println(this.mail);
                printout.flush();
                System.out.println("user found:"+this.username +", " + this.mail);
            }
            else {
                printout.println("not found");
                System.out.println("user not found...");
            }

        } catch(Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("function ended");
        }
    }
}
