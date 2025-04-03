/*
 * @BadConfigFormatException: This is our exception which will be thrown
 * if there is an error with the game configuration files
 * @Authors: Matthew Nielsen and Levi Sprung
 * @Date: 2/28/2023
 */

package clueGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BadConfigFormatException extends Exception {
	// both constructors call the parent constructor with a string as the input
	// default constructor has a default string
	// exception is written to exception_log.txt
	public BadConfigFormatException() {
		super("Improper formatting of layout or/and setup files");
		// throw exception if file is not found (should not happen)
		try {
			PrintWriter logfile = new PrintWriter("exception_log.txt");
			logfile.println("Improper formatting of layout or/and setup files");
			logfile.close();
		} catch (FileNotFoundException e) {
			System.out.println("log file not found");
		}
	}
	
	public BadConfigFormatException(String s) {
		super(s);
		// throw exception if file is not found (should not happen)
		try {
			PrintWriter logfile = new PrintWriter("exception_log.txt");
			logfile.println("s");
			logfile.close();
		} catch (FileNotFoundException e) {
			System.out.println("log file not found");
		}
	}
}
