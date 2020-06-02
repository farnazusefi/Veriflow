package Veriflow;

import java.util.LinkedList;

public class ECs {
	
	private LinkedList <String> ecs = new LinkedList<>();
	
	public void addEC (String i) {
		ecs.add(i);
	}
	public LinkedList <String> getEcs() {
		return ecs;
	}

	public void setECs(LinkedList <String> ecs) {
		this.ecs = ecs;
	}
}
