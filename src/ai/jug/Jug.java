package ai.jug;

import java.util.LinkedList;

import ai.Algorithms;
import ai.BaseState;

public final class Jug
{
	public static void open()
	{
		State.g = 4;
		State.m = new int[] {3, 5, 10000};
		
		final State start = new State();
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