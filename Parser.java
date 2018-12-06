import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



/**
 * This files take in genes from given file and converts them to binary 
 * and then appends them together into a long data type. This value
 * is then stores in a b-tree.
 * 
 * @author Spencer, Justin, Binod, Akhilesh
 *
 */
public class Parser {

	private Scanner ScanF;
	private String currentLine, nextLine, subString;
	private int startingString, windowSize;
	private boolean startF = false;

	public Parser(File file, int x) throws FileNotFoundException {

		ScanF = new Scanner(file);
		windowSize = x;
		startingString = 9;

	}
	  /**
	   * Checks for keywords origin to begin parsing data and passing it into the scanners to be converted
	   * 
	   * @return true if ScanF finds valid data
	   */
	private boolean startParsing() {
		while (ScanF.hasNextLine() && !startF) {
			currentLine = ScanF.nextLine();
			if (currentLine.trim().equals("ORIGIN")) {
				startF = true;
				currentLine = ScanF.nextLine();
				nextLine = ScanF.nextLine();
			}
		}
		if (ScanF.hasNextLine()) {
			startingString = 0;
			return true;
		} else
			return false;
	}
			/**
			 * increments the string line and converts current line to DNA
			 * 
			 * @return String converted to DNA notation
			 */
	public long incrementStartingString() {
		startingString++;
		return convert();
	}
		/**
		 * Converts current string of DNA to Long
		 * 
		 * @return converted string of DNA notation to Long
		 */
	private long convert() {
		int i = 0;
		long returnConversion = 1;
		String[] split = subString.split("");
		
		while (i < subString.length()) {
			returnConversion = returnConversion << 2;
			
			if (split[i].equals("A") || split[i].equals("a"))
				returnConversion = returnConversion | 0;
			else if (split[i].equals("T") || split[i].equals("t"))
				returnConversion = returnConversion | 3;
			else if (split[i].equals("C") || split[i].equals("c"))
				returnConversion = returnConversion | 1;
			else if (split[i].equals("G") || split[i].equals("g"))
				returnConversion = returnConversion | 2;
			else 
				return -1;
			i++;
		}
		return returnConversion;
	}
	
	/**
	 * Saves the the string of genetics only if found. returns false if it does
	 * not exist
	 * 
	 * @return true if string already exists
	 */
	public boolean nextStringExists() {
		subString = "";
		while (ScanF.hasNextLine() && !startF) {
			currentLine = ScanF.nextLine();
			if (currentLine.trim().equals("ORIGIN")) {
				startF = true;
				currentLine = ScanF.nextLine();
				nextLine = ScanF.nextLine();
			}
		}

		if (startingString >= currentLine.length() && !nextLine.equals("//")) {
			startingString = 9;
			currentLine = nextLine;
			if (ScanF.hasNextLine())
				nextLine = ScanF.nextLine();
			else
				return false;
		}

		int location = startingString;
		int counter = 0;
		boolean lengthCheck = false;
		char currentChar;
		while (counter < windowSize) {
			if (!lengthCheck && location >= currentLine.length()) {
				if (nextLine.equals("//")) {
					startF = false;
					if (startParsing()) {
						subString = "";
						counter = 0;
						location = 9;
						startingString = 9;
						lengthCheck = false;
						continue;
					} else
						return false;
				}

				lengthCheck = true;
				location = 9;
			}

			if (lengthCheck && location >= nextLine.length()) {
				startF = false;
				if (startParsing()) {
					subString = "";
					counter = 0;
					startingString = 9;
					location = startingString;
					lengthCheck = false;
					continue;
				} else
					return false;
			}

			if (lengthCheck)
				currentChar = nextLine.charAt(location);
			else
				currentChar = currentLine.charAt(location);

			if (currentChar == ' ' && location == startingString) {
				subString = "";
				counter = 0;
				startingString++;
				location = startingString;
				lengthCheck = false;
				continue;
			}

			if (currentChar == 'A' || currentChar == 'a' || currentChar == 'T' || currentChar == 't'
					|| currentChar == 'C' || currentChar == 'c' || currentChar == 'G' || currentChar == 'g'
					|| currentChar == 'N' || currentChar == 'n') {
				subString = subString + currentChar;
				counter++;
			}
			location++;
		}

		return true;
	}
}