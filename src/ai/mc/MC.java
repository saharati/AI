package ai.mc;

import java.util.LinkedList;

import ai.Algorithms;
import ai.BaseState;

public final class MC
{
	public static void open()
	{
		final State start = new State(0, 0, 3, 3, Location.RIGHT, null);
		BaseState result = Algorithms.breadthFirstSearch(start);
		if (result == null)
		{
			System.out.println("There is no solution to the given problem.");
			System.out.println(start);
		}
		else
		{
			final LinkedList<BaseState> states = new LinkedList<>();
			while (result != null)
			{
				states.addFirst(result);
				result = result.parent();
			}
			System.out.println("=== Solution (" + states.size() + " nodes) ===");
			states.forEach(System.out::println);
		}
	}
}