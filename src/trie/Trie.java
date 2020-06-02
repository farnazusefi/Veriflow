package trie;

import java.util.LinkedList;
import java.util.List;

import javafx.util.Pair;
import network.Rule;

public class Trie {
	private TrieNode root;
	private List<Interval> ecs;

	public Trie() {
		root = new TrieNode();
		root.setStarChild(new TrieNode());
		ecs = new LinkedList<Interval>();
	}

	public TrieNode getRoot() {
		return root;
	}

	public void setRoot(TrieNode root) {
		this.root = root;
	}

	public void deleteFromTrie(String prefix) {
		deleteFromTrie(prefix + "*", root);
	}

	private void deleteFromTrie(String ip, TrieNode node) {
		char currentChar = ip.charAt(0);
		if (currentChar == '*') {
			if (node.getStarChild() == null) {
				System.err.println("No such rule to be deleted!");
				return;
			}
			node.setStarChild(null);
			if (node.numberOfNonStarChildren() == 0) {
				node.setNodeValue(0);
				return;
			}
			int nodeValue = 0;
			if (node.numberOfNonStarChildren() == 2) {
				nodeValue = node.getRightChild().getNodeValue() + node.getLeftChild().getNodeValue();
				if (node.getLeftChild().isOpenRight() || node.getRightChild().isOpenLeft())
					nodeValue++;
			} else if (node.getLeftChild() != null) {
				node.setOpenLeft(node.getLeftChild().isOpenLeft());
				nodeValue = node.getLeftChild().getNodeValue();
			} else {
				node.setOpenRight(node.getRightChild().isOpenRight());
				nodeValue = node.getRightChild().getNodeValue();
			}
			node.setNodeValue(nodeValue);
		} else if (currentChar == '0') {
			boolean isNew = node.getLeftChild() == null;
			if (isNew) {
				System.err.println("No such rule to be deleted!");
				return;
			}
			deleteFromTrie(ip.substring(1), node.getLeftChild());
		} else {
			boolean isNew = node.getRightChild() == null;
			if (isNew) {
				System.err.println("No such rule to be deleted!");
				return;
			}
			deleteFromTrie(ip.substring(1), node.getRightChild());
		}
		updateNode(node);

	}

	private void insertIntoTrie(String ip, TrieNode node, String path) {
		char currentChar = ip.charAt(0);
		if (currentChar == '*') {
			node.setStarChild(new TrieNode());
			node.setOpenLeft(false);
			node.setOpenRight(false);
		} else if (currentChar == '0')

		{
			boolean isNew = node.getLeftChild() == null;
			if (isNew) {
				node.setLeftChild(new TrieNode());
			}
			insertIntoTrie(ip.substring(1), node.getLeftChild(), path + "0");

		} else {
			boolean isNew = node.getRightChild() == null;
			if (isNew) {
				node.setRightChild(new TrieNode());
			}
			insertIntoTrie(ip.substring(1), node.getRightChild(), path + "1");
		}

		updateNode(node);
	}

	private void updateNode(TrieNode node) {
		int nodeValue = 0;
		if (node.getLeftChild() != null)
			if (node.getLeftChild().getNodeValue() == 0)
				node.setLeftChild(null);
		if (node.getRightChild() != null)
			if (node.getRightChild().getNodeValue() == 0)
				node.setRightChild(null);
		switch (node.numberOfNonStarChildren()) {
		case 2:
			nodeValue = node.getLeftChild().getNodeValue() + node.getRightChild().getNodeValue();
			if (node.getStarChild() != null) {
				if (node.getLeftChild().isOpenLeft())
					nodeValue++;
				if (node.getRightChild().isOpenRight())
					nodeValue++;
			} else {
				node.setOpenLeft(node.getLeftChild().isOpenLeft());
				node.setOpenRight(node.getRightChild().isOpenRight());
			}
			if (node.getRightChild().isOpenLeft() || node.getLeftChild().isOpenRight())
				nodeValue++;
			break;
		case 1:
			if (node.getLeftChild() != null) {
				nodeValue = node.getLeftChild().getNodeValue();
				node.setOpenLeft(node.getLeftChild().isOpenLeft());
				node.setOpenRight(true);
			} else {
				nodeValue = node.getRightChild().getNodeValue();
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
		node.setNodeValue(nodeValue);
	}

	public void insertIntoTrie(String ip) {
		insertIntoTrie(ip, root, "");
	}

	public int numberOfECs() {

		return root.getNodeValue();
	}

	public void addRule(Rule rule) {
		insertIntoTrie(rule.getPrefix() + "*");
	}

	public List<Interval> getEcs() {
		return ecs;
	}

	public void setEcs(List<Interval> ecs) {
		this.ecs = ecs;
	}
	public List<Interval> generateECs() {
		ecs.clear();
		generateECs(root,"");
		return ecs;
	}
	private Interval generateECs(TrieNode node, String path) {

		switch (node.numberOfNonStarChildren()) {
		case 2:
			Interval ecLeft = generateECs(node.getLeftChild(), path + "0");
			Interval ecRight = generateECs(node.getRightChild(), path + "1");
			if (node.getLeftChild().isOpenRight() || node.getRightChild().isOpenLeft())
				ecs.add(new Interval(ecLeft.getRight(), ecRight.getLeft()));
			if (node.getStarChild() != null) {
				if (node.getLeftChild().isOpenLeft())
					ecs.add(new Interval(path, ecLeft.getLeft()));
				if (node.getRightChild().isOpenRight())
					ecs.add(new Interval(ecRight.getRight(), binaryAdd(path, 1)));
				return new Interval(path, binaryAdd(path, 1));
			} else {
				return new Interval(ecLeft.getLeft(), ecRight.getRight());
			}
		case 1:
			if (node.getLeftChild() != null) {
				ecLeft = generateECs(node.getLeftChild(), path + "0");
				if (node.getStarChild() != null) {
					ecs.add(new Interval(ecLeft.getRight(), binaryAdd(path, 1)));
					if (node.getLeftChild().isOpenLeft())
						ecs.add(new Interval(path, ecLeft.getLeft()));
					return (new Interval(path, binaryAdd(path, 1)));
				}
				return ecLeft;

			} else {
				ecRight = generateECs(node.getRightChild(), path + "1");
				if (node.getStarChild() != null) {
					ecs.add(new Interval(path, ecRight.getLeft()));
					if (node.getRightChild().isOpenRight())
						ecs.add(new Interval(ecRight.getRight(), binaryAdd(path, 1)));
					return new Interval(path, binaryAdd(path, 1));
				}
				return ecRight;
			}
		case 0:
			Interval e = new Interval(path, binaryAdd(path, 1));
			ecs.add(e);
			return e;
		}
		return null;
	}

	private static String intToBinaryString(int intValue, int numDigits) {
		String binaryString = Integer.toBinaryString(intValue);
		int length = binaryString.length();
		for (int i = length; i < numDigits; i++)
			binaryString = "0" + binaryString;
		return binaryString;
	}

	private String binaryAdd(String path, int i) {
		if (path.equals(""))
			return "End";
		int decimal = Integer.parseInt(path, 2);
		decimal += i;
		return intToBinaryString(decimal, path.length());
	}

}
