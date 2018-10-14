package ai.puzzle;

import java.util.LinkedList;

import ai.Algorithms;
import ai.BaseState;

public final class Puzzle
{
	public static void open()
	{
		final State start = new State(new int[][] {{5, 1, 3}, {4, 0, 6}, {7, 2, 8}});
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