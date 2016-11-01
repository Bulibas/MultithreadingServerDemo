package ServerSecondDemo;


import ServerSecondDemo.ServerSecondDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import static ServerSecondDemo.ServerSecondDemo.getConnection;

/**
 * Created by PC on 7.10.2016 г..
 */
public class DatabaseRegistrationRequest implements DatabaseRequest {
    private BufferedReader reader;
    private String response, request;
    private PrintStream printout;
    private boolean isRegistered=false;

    public void setPrintout(PrintStream printout) {
        this.printout = printout;
    }

    public DatabaseRegistrationRequest(BufferedReader reader, PrintStream printout) {
        this.setPrintout(printout);
        this.setReader(reader);
    }

    public void setReader(BufferedReader reader) {
        this.reader=reader;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public void run() {
        try {
            createUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendResponse(String response) {
        this.printout.println(response);
        this.printout.flush();
    }

    void createUser() throws IOException {
        String username, password, mail, command;

        username = reader.readLine();
        password = reader.readLine();
        mail = reader.readLine();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = ServerSecondDemo.getConnection();

            //check if username or mailAdress are already taken
            PreparedStatement checkDataStatement = conn.prepareStatement("SELECT * FROM users WHERE username LIKE '"+username+"' OR mail LIKE '"+mail+"'");
            ResultSet checkResultSet = checkDataStatement.executeQuery();

            //continue registration if username or mail arent taken
            if(!checkResultSet.next()) {
                PreparedStatement registerStatement = conn.prepareStatement("INSERT INTO users (username, password, mail) " +
                        "VALUES ('"+username+"', '"+password+"', '"+mail+"')");
                registerStatement.executeUpdate();

                isRegistered=true;
                sendResponse("registered");
                System.out.println("new user registered...");
            }

            else {
                sendResponse("not registered");
                System.out.println("registration failure...");
            }

            while(isRegistered) {
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
            System.out.println("User connection ended...");
        }
    }


}
