import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import readers.BackBoneReader;
import readers.AbstractReader;
import readers.RouteViewReader;
import trie.Trie;

public class Main {
	public static final int ROUTE_VIEW = 1;
	public static final int BIT_BUCKET = 2;

	public static String decodeIpMask(String ipMask) {
		String[] splitMask = ipMask.split("/");
		String[] splitIp = splitMask[0].split("\\.");
		String output = "";
		for (String s : splitIp) {
			int intValue = Integer.parseInt(s);
			output += toEightBitBinary(intValue);
		}
		return output.substring(0, Integer.parseInt(splitMask[1]));
	}

	private static String toEightBitBinary(int intValue) {
		String binaryString = Integer.toBinaryString(intValue);
		int length = binaryString.length();
		for (int i = length; i < 8; i++)
			binaryString = "0" + binaryString;
		return binaryString;
	}

	// public static String returnIpMaskPart(int inputType, String line) {
	// String ipMask = null;
	// if (inputType == ROUTE_VIEW) {
	// String[] splitLine = line.split("\\|");
	// if (splitLine[1].equals("E"))
	// return null;
	// ipMask = splitLine[7];
	// } else if (inputType == BIT_BUCKET) {
	// String[] splitLine = line.split("\\s");
	// ipMask = splitLine[0];
	// // System.out.println(ipMask);
	// }
	// return ipMask;
	// }

	public static void main(String[] args) throws IOException {
		while (true) {
			Scanner myObj = new Scanner(System.in); // Create a Scanner object
			System.out.println("Enter file name (eg.: file.txt):");
			String fileName = myObj.nextLine();
			System.out.println("Enter 1 for routeView /2 for backbone:");
			int inputType = myObj.nextInt();
			
			AbstractReader reader;
			if (inputType == ROUTE_VIEW) {
				reader = new RouteViewReader(fileName);
			} else {
				reader = new BackBoneReader(fileName);
			}

			Trie trie = new Trie();
			Set<String> rulesSet = new HashSet<String>();
			int lineCount = 0;

			while (true) {

				String ipMask = reader.getNextIpMask();
				if (ipMask == null)
					break;
				lineCount++;

				String decodedString = decodeIpMask(ipMask);
				trie.insertIntoTrie(decodedString + "*");
				rulesSet.add(decodedString);
			}
			System.out.println("ECs Count: " + trie.numberOfECs());
			System.out.println("Number of Unique rules: " + rulesSet.size());
			System.out.println("Number of Lines: " + lineCount);
			reader.closeFile();
		}
	}
}
