package ai.queens;

import java.util.LinkedList;

import ai.Algorithms;
import ai.BaseState;

public final class Queens
{
	public static void main(final String[] args)
	{
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