package network;

public class Rule {
	private String prefix;
	private String switchId;
	private String nextHopId;

	public Rule() {
	
		}	
	public Rule(String prefix, String switchId, String nextHopId) {
		super();
		this.prefix = prefix;
		this.switchId = switchId;
		this.nextHopId = nextHopId;
	}
	
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

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = decodeIpMask(prefix);
	}

	public String getSwitchId() {
		return switchId;
	}

	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}

	public String getNextHopId() {
		return nextHopId;
	}

	public void setNextHopId(String nextHopId) {
		this.nextHopId = nextHopId;
	}
	public boolean matches(String rulePrefix) {
		return rulePrefix.startsWith(prefix);
	}
}
