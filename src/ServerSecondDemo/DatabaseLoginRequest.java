package ServerSecondDemo;

import ServerSecondDemo.ServerSecondDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import static ServerSecondDemo.ServerSecondDemo.getConnection;

/**
 * Created by PC on 7.10.2016 г..
 */
public class DatabaseLoginRequest implements DatabaseRequest {
    private BufferedReader reader;
    private String response=null;
    private PrintStream printout;
    private String request;
    private boolean isLogged=false;

    public DatabaseLoginRequest(BufferedReader reader, PrintStream printout) {
        this.setReader(reader);
        this.setPrintout(printout);
    }

    public PrintStream getPrintout() {
        return printout;
    }

    public void setPrintout(PrintStream printout) {
        this.printout = printout;
    }

    public void setReader(BufferedReader reader) {
        this.reader=reader;
    }

    private String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {

        this.response = response;
    }

    @Override
    public void run() {
        try {
            loginUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendResponse(String response) {
        this.printout.println(response);
        this.printout.flush();
    }

    void loginUser() throws IOException {
        String username,password;

        username = this.reader.readLine();
        password=this.reader.readLine();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = ServerSecondDemo.getConnection();
            PreparedStatement login = conn.prepareStatement("SELECT * FROM users WHERE username LIKE '"+username+"' AND password LIKE'"+password+"'");
            ResultSet resultSet = login.executeQuery();

            if(resultSet.next()) {
                sendResponse("logged");
                setResponse("A user has logged in!...");
                isLogged = true;
            }
            else {
                sendResponse("not logged");
                setResponse("Wrong username or password from login attempt!...");
            }

            System.out.println(getResponse());

            while(isLogged) {
                this.request = reader.readLine();
                if(this.request.equals("search")) {
                    this.request=null;
                    DatabaseSearchRequest dbSearchRequest = new DatabaseSearchRequest(reader, printout);
                    dbSearchRequest.run();
                }
                else if(this.request.equals("friends")) {
                    this.request=null;
                    DatabaseFriendsRequest dbFriendsRequest = new DatabaseFriendsRequest(reader,printout,username);
                    dbFriendsRequest.run();
                }
                else if(this.request.equals("add")) {
                    this.request=null;
                    DatabaseAddFriendRequest dbAddFriendRequest = new DatabaseAddFriendRequest(reader, printout, username);
                    dbAddFriendRequest.run();
                }
                else if(this.request.equals("photo")) {
                    this.request=null;
                    DatabasePhotoRequest dbPhotoRequest = new DatabasePhotoRequest(reader, printout, username);
                    dbPhotoRequest.run();
                }
                else if(this.request.equals("addPhoto")) {
                    this.request=null;
                    DatabaseAddPhotoRequest dbAddPhotoRequest = new DatabaseAddPhotoRequest(reader, printout, username);
                    dbAddPhotoRequest.run();
                }
            }


        } catch(Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("function ended");
        }

    }



}
