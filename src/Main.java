import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

public class Main {

	public static String decode (String ipMask) {
		String[] splitMask = ipMask.split("/");
		String[] splitIp = splitMask[0].split("\\.");
		String output = "";
		for (String s : splitIp) {
			int intValue = Integer.parseInt(s);
			output+= toEightBitBinary(intValue);	 
		}
		return output.substring(0, Integer.parseInt(splitMask[1]));
	}
	private static String toEightBitBinary(int intValue) {
		String binaryString = Integer.toBinaryString(intValue);
		int length = binaryString.length();
		for (int i = length ; i < 8 ; i++)
			binaryString = "0" + binaryString;
		return binaryString;
	}
	public static void main(String[] args) throws IOException {

		RandomAccessFile raf = new RandomAccessFile("shortOut.txt", "r");
		raf.readLine();
		Trie trie = new Trie();
		while (true) {
			String currentLine = raf.readLine();
			String[] splitLine = currentLine.split("\\|");
			if (splitLine[1].equals("E"))
				break;
			String ipMask = splitLine[7];
			System.out.println(ipMask + "\t : \t"+ decode (ipMask));
			trie.insertIntoTrie(decode (ipMask) + "*");
		}
		System.out.println(trie.numberOfECs());
		raf.close();
	}
}
