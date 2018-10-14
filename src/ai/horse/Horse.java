package ai.horse;

import java.util.LinkedList;

import ai.BaseState;

public final class Horse
{
	public static void open()
	{
		final State start = new State();
		BaseState result = backtrackingSearch(start);
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
	
	private static State backtrackingSearch(final State state)
	{
		if (state.isGoal())
			return state;
		
		for (final Operation o : Operation.values())
		{
			if (o.getDomain().test(state))
			{
				final State goal = backtrackingSearch(o.getAction().apply(state));
				if (goal != null)
					return goal;
			}
		}
		return null;
	}
}