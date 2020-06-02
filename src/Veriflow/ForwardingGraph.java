package Veriflow;

import java.util.HashSet;
import java.util.Set;

public class ForwardingGraph {
	private Set<String> forwardingGraph = new HashSet<>();

	public void addToGraph (String nextHopId) {
		forwardingGraph.add(nextHopId);
	}
	public Set<String> getForwardingGraph() {
		return forwardingGraph;
	}

	public void setForwardingGraph(Set<String> forwardingGraph) {
		this.forwardingGraph = forwardingGraph;
	}
	public boolean contains (String id) {
		return forwardingGraph.contains(id);
	}

}
