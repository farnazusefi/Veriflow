package trie;

public class TrieNode {

	private TrieNode leftChild;
	private TrieNode rightChild;
	private TrieNode starChild;
	private boolean openLeft;
	private boolean openRight;
	private String possibleLeftEC;
	private String possibleRightEC;
	private String addedEC;
	private int nodeValue;
	
	public TrieNode() {
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

	public String getAddedEC() {
		return addedEC;
	}

	public void setAddedEC(String addedEC) {
		this.addedEC = addedEC;
	}
	
}
