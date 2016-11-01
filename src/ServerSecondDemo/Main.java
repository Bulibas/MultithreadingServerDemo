package ServerSecondDemo;

import java.util.Scanner;

/**
 * Created by PC on 7.10.2016 г..
 */
public class Main {
    public static void main(String[] args) throws Exception {

        ServerSecondDemo server = new ServerSecondDemo();
        Thread serverThread = new Thread(server);
        serverThread.start();

    }
}
