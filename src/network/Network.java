package network;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import Veriflow.ForwardingGraph;
import trie.Interval;
import trie.Trie;
import trie.TrieNode;

public class Network {

	private HashMap<String, Switch> switches = new HashMap<String, Switch>();
	private HashMap<String, Host> hosts = new HashMap<String, Host>();
	private LinkedList<String> ecs = new LinkedList<>();
	private List<NetworkError> networkErrors = new LinkedList<NetworkError>();
	private HashMap<String, Integer> rulePrefixes = new HashMap<>();
	private Trie trie = new Trie();

	public void parseNetworkFromFile(String fileName) throws IOException {
		RandomAccessFile f = new RandomAccessFile(fileName, "r");
		try {
			f.readLine();
			addSwitchesFromFileFormat(f);
			addHostsFromFileFormat(f);
			addRulesFromFileFormat(f);
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
			f.close();
			return;
		}

	}

	public int getNumberOfECs() {
		return trie.numberOfECs();
	}

//	public void createECs() {
//		ecs.clear();
//		TrieNode root = getTrie().getRoot();
//		createECs(root, "");
//		if (root.getPossibleRightEC() != null)
//			getEcs().add(root.getPossibleRightEC());
//		if (root.getPossibleLeftEC() != null)
//			getEcs().add(root.getPossibleLeftEC());
//	}
//
//	private void createECs(TrieNode trieNode, String pr) {
//		switch (trieNode.numberOfNonStarChildren()) {
//		case 2:
//			createECs(trieNode.getLeftChild(), pr + "0");
//			createECs(trieNode.getRightChild(), pr + "1");
//			String possibleRightEC = trieNode.getLeftChild().getPossibleRightEC();
//			if (possibleRightEC != null)
//				ecs.add(possibleRightEC);
//			else {
//				String possibleLeftEC = trieNode.getRightChild().getPossibleLeftEC();
//				if (possibleLeftEC != null)
//					ecs.add(possibleLeftEC);
//			}
//			trieNode.setPossibleLeftEC(trieNode.getLeftChild().getPossibleLeftEC());
//			trieNode.setPossibleRightEC(trieNode.getRightChild().getPossibleRightEC());
//			break;
//		case 1:
//			if (trieNode.getLeftChild() != null) {
//				createECs(trieNode.getLeftChild(), pr + "0");
//				if (trieNode.getStarChild() != null)
//					ecs.add(pr + "1");
//				else
//					trieNode.setPossibleRightEC(pr + "1");
//				trieNode.setPossibleLeftEC(trieNode.getLeftChild().getPossibleLeftEC());
//
//			} else {
//				createECs(trieNode.getRightChild(), pr + "1");
//				if (trieNode.getStarChild() != null)
//					ecs.add(pr + "0");
//				else
//					trieNode.setPossibleLeftEC(pr + "0");
//				trieNode.setPossibleRightEC(trieNode.getRightChild().getPossibleRightEC());
//
//			}
//			break;
//		case 0:
//			ecs.add(pr);
//			break;
//		default:
//			break;
//		}
//	}

	private void addRulesFromFileFormat(RandomAccessFile f) throws IOException {
		String currentLine = f.readLine();
//		System.out.println("Begin");
		while (!currentLine.startsWith("E")) {
			Set<Interval> ecs = addRuleFromString(currentLine);
//			System.out.println("current line: " + currentLine + "\t updatedECs: " + ecs );
			currentLine = f.readLine();
		}
//		System.out.println("End");
	}

	public Set<Interval> addRuleFromString(String ruleString) {
		String[] split1 = ruleString.split("-");
		Rule rule = new Rule();
		rule.setSwitchId(split1[0]);
		rule.setPrefix(split1[1]);
		rule.setNextHopId(split1[2]);
		return addRule(rule);
	}

	private void addHostsFromFileFormat(RandomAccessFile fh) throws IOException {
		String currentLine = fh.readLine();
		while (!currentLine.startsWith("R")) {
			Host ts = new Host();
			String[] split1 = currentLine.split(":");
			ts.setId(split1[0]);
			ts.setSwitchId(split1[1]);
			Switch switch1 = switches.get(ts.getSwitchId());
			switch1.addConnectedHost(ts.getId());
			hosts.put(ts.getId(), ts);
			currentLine = fh.readLine();
		}
	}

	private void addSwitchesFromFileFormat(RandomAccessFile fs) throws IOException {
		String currentLine = fs.readLine();
		while (!currentLine.startsWith("H")) {
			Switch ts = new Switch();
			String[] split1 = currentLine.split(":");
			ts.setId(split1[0]);
			String[] nhops = split1[1].split(",");
			for (String nh : nhops) {
				ts.addNextHop(nh);
			}
			switches.put(ts.getId(), ts);
			currentLine = fs.readLine();
		}
	}

	public HashMap<String, Switch> getSwitches() {
		return switches;
	}

	public Host getHostWithId(String id) {
		return hosts.get(id);
	}

	public HashMap<String, Host> getHosts() {
		return hosts;
	}

	public void addEc(String ec) {
		getEcs().add(ec);
	}

	public void checkWellformedness() {
		checkWellformedness(getECsFromTrie());
	}
	public void checkWellformedness(Set<Interval> ecs) {
		// if (hosts.size() == 0) {
		// System.err.println("Network has no Hosts!");
		// return;
		// }
		getNetworkErrors().clear();
		for (Interval ec : ecs) {
			for (Host host : hosts.values()) {
				String connectedSwitchId = host.getSwitchId();
				Switch currentSwitch = switches.get(connectedSwitchId);
				ForwardingGraph forwardingGraph = new ForwardingGraph();
				forwardingGraph.addToGraph(connectedSwitchId);
				while (true) {
					Rule associatedRule = currentSwitch.getAssociatedRule(ec.getLeft());
					if (associatedRule == null) {
						// NetworkError networkError = new NetworkError();
						// networkError.setErrorType(ErrorType.BLACK_HOLE);
						// networkError.setEc(ec);
						// networkError.setStartingPoint(switches.get(connectedSwitchId));
						// getNetworkErrors().add(networkError);
						break;
					}
					String nextHopId = associatedRule.getNextHopId();
					if (hosts.containsKey(nextHopId)) {
						break;
					}
					if (forwardingGraph.contains(nextHopId)) {
						NetworkError networkError = new NetworkError();
						networkError.setErrorType(ErrorType.LOOP);
						networkError.setEc(ec);
						networkError.setStartingPoint(switches.get(connectedSwitchId));
						getNetworkErrors().add(networkError);
						break;
					}
					currentSwitch = switches.get(nextHopId);
					forwardingGraph.addToGraph(nextHopId);
				}
			}
		}
	}

	public LinkedList<String> getEcs() {
		return ecs;
	}

	public void setEcs(LinkedList<String> ecs) {
		this.ecs = ecs;
	}

	public List<NetworkError> getNetworkErrors() {
		return networkErrors;
	}

	public void setNetworkErrors(List<NetworkError> networkErrors) {
		this.networkErrors = networkErrors;
	}

	public Trie getTrie() {
		return trie;
	}

	public void setTrie(Trie trie) {
		this.trie = trie;
	}

	public Set<Interval> addRule(Rule rule) {
		Switch switch1 = switches.get(rule.getSwitchId());
		switch1.addRule(rule);
		if (!rulePrefixes.containsKey(rule.getPrefix())) {
			Set<Interval> updateEcs = getTrie().addRule(rule);
			rulePrefixes.put(rule.getPrefix(), 1);
			return updateEcs;
		} else {
			rulePrefixes.put(rule.getPrefix(), rulePrefixes.get(rule.getPrefix()) + 1);
			return new HashSet<Interval>();
		}
	}

	public Set<Interval> deleteRule(Rule rule) {
		String switchId = rule.getSwitchId();
		Integer count = rulePrefixes.get(rule.getPrefix());
		rulePrefixes.remove(rule.getPrefix());
		Set<Interval> affectedECs = trie.deleteFromTrie(rule.getPrefix());
		if (count != 1)
			rulePrefixes.put(rule.getPrefix(), count - 1);
		Switch switch1 = switches.get(switchId);
		LinkedList<Rule> rules = switch1.getRules();
		for (Rule r : rules) {
			if (r.equals(rule)) {
				rules.remove(r);
				break;
			}
		}
		return affectedECs;
	}

	public void log(Set<Interval> affectedEcs) {
		System.out.println("Number of ECs: " + getNumberOfECs());
//		System.out.println("ECs are: " + generatedECs);
			System.out.println("Number of affected ECs: " + affectedEcs.size());
		if (getNetworkErrors().size() == 0) {
			System.out.println("Network is well-formed (No property violations)");
		} else {
			for (NetworkError netErr : getNetworkErrors()) {
				if (netErr.getErrorType() == ErrorType.LOOP)
					System.out.println("There is a loop starting from switch " + netErr.getStartingPoint().getId()
							+ " for EC " + netErr.getEc() + "!");
			}
		}

	}

	public Set<Interval> deleteRuleFromString(String ruleString) {
		String[] split1 = ruleString.split("-");
		Rule rule = new Rule();
		rule.setSwitchId(split1[0]);
		rule.setPrefix(split1[1]);
		rule.setNextHopId(split1[2]);
		return deleteRule(rule);

	}

	public List<Interval> generateECs() {
		return trie.generateECs();
	}
	public Set<Interval> getECsFromTrie(){
		return trie.getECListFromTrie();
	}

}
