package ServerSecondDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static ServerSecondDemo.ServerSecondDemo.getConnection;

/**
 * Created by PC on 15.10.2016 г..
 */
public class DatabaseAddPhotoRequest implements DatabaseRequest{
    private BufferedReader reader;
    private PrintStream printout;
    private String username;


    public DatabaseAddPhotoRequest(BufferedReader reader, PrintStream printout, String username) {
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
            addPhoto();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addPhoto() throws IOException {
        System.out.println("adding photo...");
        String newLine;
        StringBuilder builder = new StringBuilder();
        while((newLine=reader.readLine())!=null) {
            builder.append(newLine);
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = getConnection();
            System.out.println("adding started");

            PreparedStatement checkDataStatement = conn.prepareStatement("UPDATE users SET profile=CONCAT(profile,'"+builder.toString()+"') WHERE username LIKE '"+getUsername()+"'");
            checkDataStatement.executeUpdate();

            printout.println("Added!");
            System.out.println("photo added...");
            System.out.println(builder.toString());
         } catch(Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("function ended");
        }
    }
}
