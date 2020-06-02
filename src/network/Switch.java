package network;

import java.util.ArrayList;
import java.util.LinkedList;

public class Switch {

	private String id;
	private ArrayList<String> nextHops;
	private ArrayList<String> connectedHosts;
	private LinkedList<Rule> rules;

	public Switch() {
		nextHops = new ArrayList<String>();
		connectedHosts = new ArrayList<String>();
		rules = new LinkedList<Rule>();
	}

	public Rule getAssociatedRule(String rulePrefix) {
		for (Rule r : rules) {
			if (r.matches(rulePrefix))
				return r;
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void addNextHop(String nextHop) {
		nextHops.add(nextHop);
	}

	public void addConnectedHost(String id) {
		connectedHosts.add(id);
	}

	public ArrayList<String> getNextHops() {
		return nextHops;
	}

	public void setNextHops(ArrayList<String> nextHops) {
		this.nextHops = nextHops;
	}

	public void addRule(Rule newRule) {
		rules.addFirst(newRule);
	}

	public LinkedList<Rule> getRules() {
		return rules;
	}

	public ArrayList<String> getConnectedHosts() {
		return connectedHosts;
	}

	public void setConnectedHosts(ArrayList<String> connectedHosts) {
		this.connectedHosts = connectedHosts;
	}

}
