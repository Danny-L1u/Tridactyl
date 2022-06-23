package Grade_11;
import java.io.*;
import java.util.Scanner;

/**
 * This program takes in a key and a encoded message from a file then outputs the decoded message.
 * First the program does a bunch of formatting such as putting the key and message in upper case
 * and getting rid of the start and end square brackets so everything runs smoothly. Second,
 * the program finds the key for the cipher and the key determines what base 10 number each
 * letter of the alphabet (and space) will be corresponded to. Then the program finds converts the
 * base 10 number each letter in the message is corresponded to and turns it into base 3. The digits 
 * of the base 3 numbers are then switched around with other digits from base 3 numbers. Finally
 * the switched base 3 numbers are converted back into base 10, and the letter corresponding
 * the each base 10 value is outputted giving the user a decoded message.
 * 
 * @author dliu & jlefebvre
 */
public class Tridactyl {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner inFile = new Scanner(new File("input.txt"));

		//String phrase is the key I get from first line of input in the input file
		String key = inFile.nextLine().toUpperCase(); 
		//Get the key
		key = getKey(key);
		
		System.out.println("DECODED MESSAGE:");

		//Keep going until there is no input in the next line in the input file
		while(inFile.hasNextLine()){
			System.out.print("\n");
			//Receive coded message from input file
			StringBuffer message = new StringBuffer(inFile.nextLine().toUpperCase());

			//Gets rid of all square brackets in the input
			while (message.indexOf("[") != -1 && message.indexOf("]") != -1) {
				message.deleteCharAt(message.indexOf("["));
				message.deleteCharAt(message.indexOf("]"));
			}

			//Formatting inputs and outputs end, REAL CODE STARTS
			//Initialize an array for each letter in the message that will be converted to base 3
			int[] encoded = new int[message.length()];

			//Loop through each character in the message 
			for (int i = 0; i < message.length(); i++) {
				//Loop through the key 
				for (int k = 0; k < key.length(); k++){
					//Check what index the letter chosen in the message is in the key then add the 
					//base 3 value of that index to an array
					if (key.charAt(k) == message.charAt(i)) {
						encoded[i] = toBase3(k);
					}
				}
			}

			//Get the decoded base 3 values
			String[] decoded = ABC(encoded);

			//Output the letter in the key that is associated to the base 10 number 
			for (int k = 0; k < decoded.length; k++) {
				System.out.print(key.charAt(toBase10(Integer.parseInt(decoded[k]))));
			}

			//Extra line because doc says to leave a blank line after every decoded line
			System.out.print("\n");
		}
	}


	/**
	 * Takes in a phrase then goes through each letter (including spaces), the letter (or space) will be added to a
	 * "key" and the rest of that letters in that phrase will be removed from the phrase. By the end of the 
	 * phrase, the letters that aren't added to the key are all added to the end of they key in alphabetical
	 * order. 
	 * 
	 * @param phrase
	 * @return key
	 */
	public static String getKey(String phrase) {
		//Key will be the empty string we add the code to
		String key = "";
		StringBuffer s = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ ");

		//Loop through each letter of the phrase 
		for (int i = 0; i < phrase.length(); i++) {
			//Loop through alphabet 
			for (int k = 0; k < s.length(); k++) {
				//If phrase letter is found in alphabet add letter to the key and delete that character from the alphabet
				if (phrase.charAt(i) == s.charAt(k)) {
					key = key + s.charAt(k);
					s.deleteCharAt(k);
				}
			}

		}
		//Add the rest of the alphabet left to the end of the key
		key = key + s;
		return key;
	}


	/**
	 * Decodes the ABC switching. It takes in an int array of base 3 numbers and does three
	 * numbers in the array at a time. First the method pads zeros to the front we need to do this
	 * so there are no out of bounds errors when switching and when casting a string to an int
	 * it gets rid of the leading zeros i.e "001" casted to an int would be 1. Then the method
	 * uses StringBuffer to do the digit swaps between the base 3 numbers, order doesn't really 
	 * matter as there a digit is not switched more than one time. Then the decoded base 3 numbers
	 * are added to a String array then the entire array is returned.
	 * 
	 * @param Base3
	 * @return decoded
	 */
	public static String[] ABC(int[] Base3) {

		//Create String array for the decoded base 3 numbers
		String decoded[] = new String[Base3.length];

		//Loop through array three numbers at a time, i.e works on indexes 0, 1, 2 then indexes 3, 4, 5
		for (int i = 0; i < Base3.length; i+=3) {
			//Format each String so if there are only 2 or 1 digits, zeros will be padded to the front to
			//make it three digits
			StringBuffer S1 = new StringBuffer(String.format("%03d", Base3[i]));
			StringBuffer S2 = new StringBuffer(String.format("%03d",Base3[i + 1]));
			StringBuffer S3 = new StringBuffer(String.format("%03d",Base3[i + 2]));
			StringBuffer A = new StringBuffer(S1);
			StringBuffer B = new StringBuffer(S2);
			StringBuffer C = new StringBuffer(S3);

			//Between A & B exchange the least significant digit
			A.replace(2, 3, String.valueOf(S2.charAt(2)));
			B.replace(2, 3, String.valueOf(S1.charAt(2)));

			//Between A & C exchange the middle digit
			A.replace(1, 2, String.valueOf(S3.charAt(1)));
			C.replace(1, 2, String.valueOf(S1.charAt(1)));

			//Between B & C exchange the most significant digit
			B.replace(0, 1, String.valueOf(S3.charAt(0)));
			C.replace(0, 1, String.valueOf(S2.charAt(0)));

			//Add decoded base 3 values to an array
			decoded[i] = A.toString();
			decoded[i + 1] = B.toString();
			decoded[i + 2] = C.toString();
		}

		return decoded;
	}


	/**
	 * Takes in a base 10 number then turns it into a base 3 number.
	 * 
	 * @param Num
	 * @return Base3
	 */
	public static int toBase3(int Num) {

		//Divide Num by 9 to find the left digit in base 3
		int Power3 = Num / 9;
		//Mod Num by 9 then divide by 3 to find the middle digit in base 3
		int Power2 = Num % 9 / 3;
		//Mod Num by 3 to find the right digit in base 3
		int Power1 = Num % 3;

		//Put all the digits together as a string so the number in base 3 concatenates properly
		String Base3 = "" + Power3 + Power2 + Power1;

		//Return the string as an Int
		return Integer.parseInt(Base3);
	}


	/**
	 * Takes in a base 3 number and turns it into base 10.
	 * 
	 * @param Num
	 * @return Power1 + Power2 + Power3
	 */
	public static int toBase10(int Num) {

		//Divide Num by 100 and multiply it by 9 to find the value of the left base 3 number
		int Power3 = (Num / 100) * 9;
		//Mod Num by 100 then divide by 10 then multiply by 3 to find the value of the middle base 3 number
		int Power2 = ((Num % 100) / 10) * 3;
		//Mod Num by 100 then mod by 10 to find the value of the right base 3 number
		int Power1 = (Num % 100) % 10;

		//Add up all the base 3 values to get the base 10 number
		return Power1 + Power2 + Power3;
	}


}
