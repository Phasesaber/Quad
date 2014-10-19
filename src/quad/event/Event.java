package quad.event;

import java.util.ArrayList;
import java.util.List;
/**
 * Quad:Event.java
 * 
 * @author Phasesaber on Oct 18, 2014
 * 
 * Event system that <i>may</i> be used.
 */
public class Event {

	private boolean combo;
	private Runnable outcome;

	static List<Event> events = new ArrayList<Event>();

	public Event(boolean combo, Runnable outcome) {
		this.combo = combo;
		this.outcome = outcome;
		events.add(this);
	}

	public boolean getCombo() {
		return combo;
	}

	public Runnable getOutcome() {
		return outcome;
	}

	public void runOutcome() {
		outcome.run();
	}

	public static void runEvents(){
		for(Event e : events)
			if(e.getCombo())
				e.runOutcome();
	}
}
