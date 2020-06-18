package trie;

import java.util.HashSet;
import java.util.Set;

public class TrieNode {

	private TrieNode leftChild;
	private TrieNode rightChild;
	private TrieNode starChild;
	private boolean openLeft;
	private boolean openRight;
	private String possibleLeftEC;
	private String possibleRightEC;
	private int nodeValue;
	private Interval advertisedInterval;
	private Set<Interval> ecs;
	
	public TrieNode() {
		ecs = new HashSet<Interval>();
	}

	public TrieNode getLeftChild() {
		return leftChild;
	}
	public void setLeftChild(TrieNode leftChild) {
		this.leftChild = leftChild;
	}
	public TrieNode getRightChild() {
		return rightChild;
	}
	public void setRightChild(TrieNode rightChild) {
		this.rightChild = rightChild;
	}
	public boolean isOpenLeft() {
		return openLeft;
	}
	public void setOpenLeft(boolean openLeft) {
		this.openLeft = openLeft;
	}
	public boolean isOpenRight() {
		return openRight;
	}
	public void setOpenRight(boolean openRight) {
		this.openRight = openRight;
	}

	public TrieNode getStarChild() {
		return starChild;
	}

	public void setStarChild(TrieNode starChild) {
		this.starChild = starChild;
	}
	
	public int numberOfNonStarChildren() {
		int num = 0;
		if (rightChild != null)
			num++;
		if (leftChild != null)
			num++;
		return num;
	}

	public String getPath() {
		return getPossibleLeftEC();
	}

	public void setPath(String path) {
		this.setPossibleLeftEC(path);
	}

	public String getPossibleRightEC() {
		return possibleRightEC;
	}

	public void setPossibleRightEC(String possibleRighttEC) {
		this.possibleRightEC = possibleRighttEC;
	}

	public String getPossibleLeftEC() {
		return possibleLeftEC;
	}

	public void setPossibleLeftEC(String possibleLeftEC) {
		this.possibleLeftEC = possibleLeftEC;
	}

	public int getNodeValue() {
		return nodeValue;
	}

	public void setNodeValue(int nodeValue) {
		this.nodeValue = nodeValue;
	}



	public Set<Interval> getEcs() {
		return ecs;
	}

	public void setEcs(HashSet<Interval> ecs) {
		this.ecs = ecs;
	}
	public void addEC (Interval ec) {
		this.ecs.add(ec);
	}

	public Interval getAdvertisedInterval() {
		return advertisedInterval;
	}

	public void setAdvertisedInterval(Interval advertisedInterval) {
		this.advertisedInterval = advertisedInterval;
	}
	
}
