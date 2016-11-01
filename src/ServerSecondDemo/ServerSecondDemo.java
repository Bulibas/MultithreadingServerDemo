package ServerSecondDemo;

import java.io.*;
import java.nio.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by PC on 6.10.2016 г..
 */
public class ServerSecondDemo implements Runnable{
    private String serverCommand;
    private static final int PORT = 8080;
    private ServerSocket server=null;
    private Thread runningThread;
    private boolean isStopped=false;

    public ServerSecondDemo() throws Exception {

    }

    public boolean isStopped(){
        return this.isStopped;
    }

    public String getServerCommand() {
        return serverCommand;
    }

    public void setServerCommand(BufferedReader reader) throws IOException {
        this.serverCommand=reader.readLine();
    }

    public static int getPORT() {
        return PORT;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(Integer port) throws IOException {
        this.server = new ServerSocket(port);
    }
    public void openServerSocket() throws IOException {
        setServer(getPORT());
    }

    public static Connection getConnection() throws Exception{
        try {
            final String USERNAME = "root";
            final String PASSWORD = "";
            String driver = "com.mysql.jdbc.Driver";
            final String URL = "jdbc:mysql://localhost:3306/users";

            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connection to database...");
            return conn;
        } catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public void run() {
        this.runningThread=Thread.currentThread();
        System.out.println("Server running.....");

        try {
            openServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!isStopped()) {
            try {
                Socket socket = this.server.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream printout = new PrintStream(socket.getOutputStream(),true);

                setServerCommand(reader);
                String command = getServerCommand();

                if(command.equals("reg")){
                    DatabaseRegistrationRequest dbRegistrationRequest = new DatabaseRegistrationRequest(reader, printout);
                    Thread registrationThread = new Thread(dbRegistrationRequest);
                    registrationThread.start();

                }
                else if(command.equals("log")) {
                    DatabaseLoginRequest dbLoginRequest = new DatabaseLoginRequest(reader, printout);
                    Thread loginThread = new Thread(dbLoginRequest);
                    loginThread.start();
                }
                else {
                    System.out.println("Wrong command from client request....");
                }


            }  catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            this.server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server stopped....");

    }

    public void stopServer() {
        this.isStopped=true;
        try {
            this.server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}