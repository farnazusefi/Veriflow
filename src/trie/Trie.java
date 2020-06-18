package trie;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

	public Set<Interval> deleteFromTrie(String prefix) {
		return deleteFromTrie(prefix + "*", root, "");
	}

	private Set<Interval> deleteFromTrie(String ip, TrieNode node, String path) {
		Set<Interval> generatedEcs = new HashSet<Interval>();
		char currentChar = ip.charAt(0);
		if (currentChar == '*') {
			if (node.getStarChild() == null) {
				System.err.println("No such rule to be deleted!");
				return new HashSet<Interval>();
			}
			node.setStarChild(null);
			if (node.numberOfNonStarChildren() == 0) {
				node.setNodeValue(0);
				return new HashSet<Interval>();
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
				return new HashSet<Interval>();
			}
			Set<Interval> ecLeft = deleteFromTrie(ip.substring(1), node.getLeftChild(), path + "0");
			generatedEcs.addAll(ecLeft);
		} else {
			boolean isNew = node.getRightChild() == null;
			if (isNew) {
				System.err.println("No such rule to be deleted!");
				return new HashSet<Interval>();
			}
			Set<Interval> ecRight = deleteFromTrie(ip.substring(1), node.getRightChild(), path + "1");
			generatedEcs.addAll(ecRight);
		}
		generatedEcs.addAll(updateNode(node, path));
		return generatedEcs;

	}

	private Set<Interval> insertIntoTrie(String ip, TrieNode node, String path) {
		Set<Interval> ecSet = new HashSet<Interval>();
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
			Set<Interval> ecS1 = insertIntoTrie(ip.substring(1), node.getLeftChild(), path + "0");
			ecSet.addAll(ecS1);
		} else {
			boolean isNew = node.getRightChild() == null;
			if (isNew) {
				node.setRightChild(new TrieNode());
			}
			Set<Interval> ecS2 = insertIntoTrie(ip.substring(1), node.getRightChild(), path + "1");
			ecSet.addAll(ecS2);
		}

		ecSet.addAll(updateNode(node, path));
		return ecSet;
	}

	private Set<Interval> updateNode(TrieNode node, String path) {
		int nodeValue = 0;
		HashSet<Interval> newEcs = new HashSet<Interval>();
		HashSet<Interval> generatedEcs = new HashSet<Interval>();
		if (node.getLeftChild() != null)
			if (node.getLeftChild().getNodeValue() == 0)
				node.setLeftChild(null);
		if (node.getRightChild() != null)
			if (node.getRightChild().getNodeValue() == 0)
				node.setRightChild(null);
		switch (node.numberOfNonStarChildren()) {
		case 2:
			if (node.getLeftChild().isOpenRight() || node.getRightChild().isOpenLeft())
				generatedEcs.add(new Interval(node.getLeftChild().getAdvertisedInterval().getRight(),
						node.getRightChild().getAdvertisedInterval().getLeft()));
			nodeValue = node.getLeftChild().getNodeValue() + node.getRightChild().getNodeValue();
			if (node.getStarChild() != null) {
				if (node.getLeftChild().isOpenLeft()) {
					generatedEcs.add(new Interval(path, node.getLeftChild().getAdvertisedInterval().getLeft()));
					nodeValue++;
				}
				if (node.getRightChild().isOpenRight()) {
					generatedEcs.add(
							new Interval(node.getRightChild().getAdvertisedInterval().getRight(), binaryAdd(path, 1)));
					nodeValue++;
				}
				node.setAdvertisedInterval(new Interval(path, binaryAdd(path, 1)));

			} else {
				node.setAdvertisedInterval(new Interval(node.getLeftChild().getAdvertisedInterval().getLeft(),
						node.getRightChild().getAdvertisedInterval().getRight()));
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
				if (node.getStarChild() != null) {
					generatedEcs.add(
							new Interval(node.getLeftChild().getAdvertisedInterval().getRight(), binaryAdd(path, 1)));
					nodeValue++;
					if (node.isOpenLeft()) {
						generatedEcs.add(new Interval(path, node.getLeftChild().getAdvertisedInterval().getLeft()));
						nodeValue++;
					}
					node.setAdvertisedInterval(new Interval(path, binaryAdd(path, 1)));
					node.setOpenLeft(false);
					node.setOpenRight(false);
				} else {
					node.setAdvertisedInterval(node.getLeftChild().getAdvertisedInterval());
				}

			} else {
				nodeValue = node.getRightChild().getNodeValue();
				node.setOpenRight(node.getRightChild().isOpenRight());
				node.setOpenLeft(true);
				if (node.getStarChild() != null) {
					generatedEcs.add(new Interval(path, node.getRightChild().getAdvertisedInterval().getLeft()));
					nodeValue++;
					if (node.isOpenRight()) {
						generatedEcs.add(new Interval(node.getRightChild().getAdvertisedInterval().getRight(),
								binaryAdd(path, 1)));
						nodeValue++;
					}
					node.setAdvertisedInterval(new Interval(path, binaryAdd(path, 1)));
					node.setOpenLeft(false);
					node.setOpenRight(false);
				} else {
					node.setAdvertisedInterval(node.getRightChild().getAdvertisedInterval());
				}

			}

			break;

		case 0:
			nodeValue = 1;
			node.setOpenLeft(false);
			node.setOpenRight(false);
			node.setAdvertisedInterval(new Interval(path, binaryAdd(path, 1)));
			generatedEcs.add(node.getAdvertisedInterval());
			break;
		}
		Set<Interval> oldECs = node.getEcs();
		for (Interval i : generatedEcs) {
			if (!oldECs.contains(i)) {
				newEcs.add(i);
			}
		}
		node.setEcs(generatedEcs);
		node.setNodeValue(nodeValue);
		return newEcs;
	}

	public Set<Interval> getECListFromTrie() {
		return getECListFromTrie(root);
	}

	private Set<Interval> getECListFromTrie(TrieNode node) {
		Set<Interval> ecs = new HashSet<Interval>();
		if (node.getRightChild() != null)
			ecs.addAll(getECListFromTrie(node.getRightChild()));
		if (node.getLeftChild() != null)
			ecs.addAll(getECListFromTrie(node.getLeftChild()));
		ecs.addAll(node.getEcs());
		return ecs;
	}

	public Set<Interval> insertIntoTrie(String ip) {
		return insertIntoTrie(ip, root, "");
	}

	public int numberOfECs() {

		return root.getNodeValue();
	}

	public Set<Interval> addRule(Rule rule) {
		return insertIntoTrie(rule.getPrefix() + "*");
	}

	public List<Interval> getEcs() {
		return ecs;
	}

	public void setEcs(List<Interval> ecs) {
		this.ecs = ecs;
	}

	public List<Interval> generateECs() {
		ecs.clear();
		generateECs(root, "");
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
				node.setAdvertisedInterval(new Interval(path, binaryAdd(path, 1)));
				return node.getAdvertisedInterval();
			} else {
				node.setAdvertisedInterval(new Interval(ecLeft.getLeft(), ecRight.getRight()));
				return node.getAdvertisedInterval();
			}
		case 1:
			if (node.getLeftChild() != null) {
				ecLeft = generateECs(node.getLeftChild(), path + "0");
				if (node.getStarChild() != null) {
					ecs.add(new Interval(ecLeft.getRight(), binaryAdd(path, 1)));
					if (node.getLeftChild().isOpenLeft())
						ecs.add(new Interval(path, ecLeft.getLeft()));
					node.setAdvertisedInterval(new Interval(path, binaryAdd(path, 1)));
					return node.getAdvertisedInterval();
				}

				node.setAdvertisedInterval(ecLeft);
				return node.getAdvertisedInterval();

			} else {
				ecRight = generateECs(node.getRightChild(), path + "1");
				if (node.getStarChild() != null) {
					ecs.add(new Interval(path, ecRight.getLeft()));
					if (node.getRightChild().isOpenRight())
						ecs.add(new Interval(ecRight.getRight(), binaryAdd(path, 1)));
					node.setAdvertisedInterval(new Interval(path, binaryAdd(path, 1)));
					return node.getAdvertisedInterval();
				}
				node.setAdvertisedInterval(ecRight);
				return ecRight;
			}
		case 0:
			Interval e = new Interval(path, binaryAdd(path, 1));
			ecs.add(e);
			node.setAdvertisedInterval(e);
			return e;
		}
		assert (false);
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
