package ServerSecondDemo;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Created by PC on 7.10.2016 г..
 */
public interface DatabaseRequest extends Runnable {
    void setReader(BufferedReader reader);
    void setPrintout(PrintStream printout);
}
