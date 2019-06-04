
public class TrieNode {

	private TrieNode leftChild;
	private TrieNode rightChild;
	private TrieNode starChild;
	private boolean openLeft;
	private boolean openRight;
	
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
	
}
