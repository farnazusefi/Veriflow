package trie;

public class Interval {
	private String left;
	private String right;

	public Interval(String left, String right) {
		super();
		this.left = left;
		this.right = right;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	@Override
	public String toString() {
		return "[" + left + "," + right + ")";
	}
}
