import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

public class Main {

	public static String decode(String ipMask) {
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

	public static void main(String[] args) throws IOException {

		RandomAccessFile raf = new RandomAccessFile("201901011200.txt", "r");
		raf.readLine();
		Trie trie = new Trie();
		Set<String> rulesSet = new HashSet<String>();
		int lineCount = 0;
		while (true) {
			String currentLine = raf.readLine();
			String[] splitLine = currentLine.split("\\|");
			if (splitLine[1].equals("E"))
				break;
			lineCount++;
			String ipMask = splitLine[7];
			String decodedString = decode(ipMask);
			// System.out.println(ipMask + "\t : \t"+ decodedString);
			trie.insertIntoTrie(decodedString + "*");
			rulesSet.add(decodedString);
		}
		System.out.println("ECs Count: " + trie.numberOfECs());
		System.out.println("Number of Unique rules: " + rulesSet.size());
		System.out.println("Number of Lines: " + lineCount);
		raf.close();
	}
}
