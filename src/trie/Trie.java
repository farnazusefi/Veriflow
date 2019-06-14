package trie;
public class Trie {
	private TrieNode root;

	public Trie() {
		root = new TrieNode();
	}

	private void insertIntoTrie(String ip, TrieNode node, boolean newlyCreated) {
		char currentChar = ip.charAt(0);
		if (currentChar == '*')
			return;
		if (node.numberOfNonStarChildren() == 0 && !newlyCreated)
			node.setStarChild(new TrieNode());
		if (currentChar == '0') {
			boolean isNew = node.getLeftChild() == null;
			if (isNew) {
				node.setLeftChild(new TrieNode());
			}
			insertIntoTrie(ip.substring(1), node.getLeftChild(), isNew);
		} else {
			boolean isNew = node.getRightChild() == null;
			if (isNew) {
				node.setRightChild(new TrieNode());
			}
			insertIntoTrie(ip.substring(1), node.getRightChild(), isNew);
		}

	}

	public void insertIntoTrie(String ip) {
		insertIntoTrie(ip, root, false);
	}

	// private int numberOfECs(TrieNode node) {
	// int nodeValue = 0;
	//
	// if (node.getLeftChild() != null) {
	// nodeValue += numberOfECs(node.getLeftChild());
	// node.setOpenLeft(node.getLeftChild().isOpenLeft());
	// }
	// if (node.getRightChild() != null) {
	// nodeValue += numberOfECs(node.getRightChild());
	// node.setOpenRight(node.getRightChild().isOpenRight());
	// }
	// if (node.getStarChild() != null) {
	// node.setOpenLeft(false);
	// node.setOpenRight(false);
	// }
	// if (node.numberOfNonStarChildren() != 2 && node.getStarChild() != null)
	// nodeValue++;
	// else if (node.numberOfNonStarChildren() == 2)
	// if (node.getRightChild().isOpenLeft() && node.getLeftChild().isOpenRight())
	// nodeValue--;
	//
	// return Math.max(1, nodeValue);
	// }

	private int numberOfECs(TrieNode node) {
		int nodeValue = 0;

		switch (node.numberOfNonStarChildren()) {
		case 2:
			nodeValue = numberOfECs(node.getLeftChild()) + numberOfECs(node.getRightChild());
			if (node.getStarChild() != null) {
				if (node.getLeftChild().isOpenLeft())
					nodeValue++;
				if (node.getRightChild().isOpenRight())
					nodeValue++;
				
			}else {
				
				node.setOpenLeft(node.getLeftChild().isOpenLeft());
				node.setOpenRight(node.getRightChild().isOpenRight());
			}
			if (node.getRightChild().isOpenLeft() || node.getLeftChild().isOpenRight())
				nodeValue++;
			break;
		case 1:
			if (node.getLeftChild() != null) {
				nodeValue = numberOfECs(node.getLeftChild());
				node.setOpenLeft(node.getLeftChild().isOpenLeft());
				node.setOpenRight(true);
			} else {
				nodeValue = numberOfECs(node.getRightChild());
				node.setOpenRight(node.getRightChild().isOpenRight());
				node.setOpenLeft(true);
			}
			if (node.getStarChild() != null) {
				if (node.isOpenLeft() && node.isOpenRight())
					nodeValue += 2;
				else
					nodeValue++;
				node.setOpenLeft(false);
				node.setOpenRight(false);
			}
			break;

		case 0:
			nodeValue = 1;
			node.setOpenLeft(false);
			node.setOpenRight(false);
			break;
		}

		return nodeValue;
	}

	public static void main(String[] args) {

		Trie trie = new Trie();

//		 trie.insertIntoTrie("00*");
//		 trie.insertIntoTrie("01*");
//		 trie.insertIntoTrie("0001*");
//		 trie.insertIntoTrie("010*");

//		trie.insertIntoTrie("001*");
//		trie.insertIntoTrie("0010*");
//		trie.insertIntoTrie("10*");
//		trie.insertIntoTrie("1001*");
		
		trie.insertIntoTrie("0*");
		trie.insertIntoTrie("001*");
		trie.insertIntoTrie("0101*");
		trie.insertIntoTrie("0110*");

		trie.insertIntoTrie("10*");
		trie.insertIntoTrie("1010*");
		trie.insertIntoTrie("1101*");
		trie.insertIntoTrie("1111*");



//		trie.insertIntoTrie("010*");
//		trie.insertIntoTrie("101*");

		System.out.println(trie.numberOfECs());
		int a = 10;
	}

	public int numberOfECs() {

		return numberOfECs(root);
	}
}
