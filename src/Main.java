import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import network.Network;
import trie.Interval;

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
		Scanner consoleLineScanner = new Scanner(System.in); // Create a Scanner object
		System.out.println("Enter network configuration file name (eg.: file.txt):");
		String fileName = consoleLineScanner.nextLine();
		Network network = new Network();
		network.parseNetworkFromFile(fileName);
		Set<Interval> generatedECs = network.getECsFromTrie();
		network.checkWellformedness();
		network.log(generatedECs);
		while (true) {
			Set<Interval> affectedEcs = new HashSet<Interval>();
			System.out.println(" ");
			System.out.println(
					"Add rule by entering A#switchIP-rulePrefix-nextHopIP (eg.A#127.0.0.1-128.0.0.0/2-127.0.0.2)");
			System.out.println(
					"Remove rule by entering R#switchIP-rulePrefix-nextHopIP (eg.R#127.0.0.1-128.0.0.0/2-127.0.0.2)");
			System.out.println("Exit by entering E");
			String input = consoleLineScanner.nextLine();
			if (input.startsWith("A")) {
				affectedEcs = network.addRuleFromString(input.substring(2));
				network.checkWellformedness(affectedEcs);
			} else if (input.startsWith("R")) {
				affectedEcs = network.deleteRuleFromString(input.substring(2));
				network.checkWellformedness(affectedEcs);
			} else if (input.equals("E")) {
				break;
			} else {
				System.out.println("Wrong input format!");
				continue;
			}
			network.log(affectedEcs);
		}
		consoleLineScanner.close();
	}
}
