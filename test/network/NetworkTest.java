package network;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import trie.Interval;

public class NetworkTest {

	@Test
	public void test1() throws IOException {
		Network network = new Network();
		network.parseNetworkFromFile("T1.txt");
		List<Interval> generatedECs = network.generateECs();
		Set<Interval> ecs = new HashSet<>(generatedECs);
		Assert.assertEquals(ecs, network.getECsFromTrie());
		Assert.assertEquals(9, generatedECs.size());
		network.checkWellformedness();
		Assert.assertEquals(0, network.getNetworkErrors().size());

	}

	@Test
	public void test2() throws IOException {

		Network network = new Network();
		network.parseNetworkFromFile("T2.txt");
		List<Interval> generatedECs = network.generateECs();
		Set<Interval> ecs = new HashSet<>(generatedECs);
		Assert.assertEquals(ecs, network.getECsFromTrie());
		Assert.assertEquals(9, generatedECs.size());
		network.checkWellformedness();
		Assert.assertEquals(1, network.getNetworkErrors().size());

	}

	@Test
	public void test3() throws IOException {

		Network network = new Network();
		network.parseNetworkFromFile("T3.txt");
		List<Interval> generatedECs = network.generateECs();
		Set<Interval> ecs = new HashSet<>(generatedECs);
		Assert.assertEquals(ecs, network.getECsFromTrie());
//		System.out.println(generatedECs);
		Assert.assertEquals(10, generatedECs.size());
		network.checkWellformedness();
		Assert.assertEquals(0, network.getNetworkErrors().size());

	}

	@Test
	public void test4() throws IOException {

		Network network = new Network();
		network.parseNetworkFromFile("T4.txt");
		List<Interval> generatedECs = network.generateECs();
		Set<Interval> ecs = new HashSet<>(generatedECs);
		Assert.assertEquals(ecs, network.getECsFromTrie());
		Assert.assertEquals(10, generatedECs.size());
		network.checkWellformedness();
		Assert.assertEquals(1, network.getNetworkErrors().size());

	}

	@Test
	public void test5() throws IOException {

		Network network = new Network();
		network.parseNetworkFromFile("T5.txt");
		List<Interval> generatedECs = network.generateECs();
		Set<Interval> ecs = new HashSet<>(generatedECs);
		Assert.assertEquals(ecs, network.getECsFromTrie());
		Assert.assertEquals(10, generatedECs.size());
		network.checkWellformedness();
		Assert.assertEquals(3, network.getNetworkErrors().size());

	}

	@Test
	public void test6() throws IOException {

		Network network = new Network();
		network.parseNetworkFromFile("T4.txt");
		List<Interval> generatedECs = network.generateECs();
		Set<Interval> ecs = new HashSet<>(generatedECs);
		Assert.assertEquals(ecs, network.getECsFromTrie());
		Assert.assertEquals(10, generatedECs.size());
		Rule newRule = new Rule();
		newRule.setPrefix("128.0.0.0/2");
		newRule.setNextHopId("127.0.0.2");
		newRule.setSwitchId("127.0.0.1");
		Set<Interval> newEcs = network.addRule(newRule);
		network.checkWellformedness(newEcs);
		Assert.assertEquals(10, generatedECs.size());
		generatedECs = network.generateECs();
		ecs = new HashSet<>(generatedECs);
		Assert.assertEquals(ecs, network.getECsFromTrie());
		network.deleteRule(newRule);
		Assert.assertEquals(10, generatedECs.size());
	}

	@Test
	public void addDynamicRule() throws IOException {

		Network network = new Network();
		network.parseNetworkFromFile("T5.txt");
		List<Interval> generatedECs = network.generateECs();
		Set<Interval> ecs = new HashSet<>(generatedECs);
		Assert.assertEquals(ecs, network.getECsFromTrie());
		Assert.assertEquals(10, generatedECs.size());
		network.checkWellformedness();
		Assert.assertEquals(3, network.getNetworkErrors().size());
		Rule newRule = new Rule();
		newRule.setPrefix("80.0.0.0/4");
		newRule.setNextHopId("127.0.0.7");
		newRule.setSwitchId("127.0.0.11");
		network.addRule(newRule);
		generatedECs = network.generateECs();
		ecs = new HashSet<>(generatedECs);
		Assert.assertEquals(ecs, network.getECsFromTrie());
//		System.out.println(generatedECs);
		Assert.assertEquals(11, network.getNumberOfECs());
		Assert.assertEquals(11, generatedECs.size());

	}

	@Test
	public void deleteRule() throws IOException {

		Network network = new Network();
		network.parseNetworkFromFile("T5.txt");
		List<Interval> generatedECs = network.generateECs();
		Set<Interval> ecs = new HashSet<>(generatedECs);
		Assert.assertEquals(ecs, network.getECsFromTrie());
		Assert.assertEquals(10, generatedECs.size());
		network.checkWellformedness();
		Assert.assertEquals(3, network.getNetworkErrors().size());
		Rule newRule = new Rule();
		newRule.setPrefix("80.0.0.0/4");
		newRule.setNextHopId("127.0.0.7");
		newRule.setSwitchId("127.0.0.11");
		network.addRule(newRule);
		generatedECs = network.generateECs();
		ecs = new HashSet<>(generatedECs);
		Assert.assertEquals(ecs, network.getECsFromTrie());
		Assert.assertEquals(11, generatedECs.size());
		network.deleteRule(newRule);
		generatedECs = network.generateECs();
		ecs = new HashSet<>(generatedECs);
		Assert.assertEquals(ecs, network.getECsFromTrie());
		Assert.assertEquals(10, generatedECs.size());

	}
}
