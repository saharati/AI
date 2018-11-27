package ai;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public final class Algorithms
{
	public static BaseState breadthFirstSearch(final BaseState start)
	{
		final Queue<BaseState> open = new LinkedList<>();
		final Set<BaseState> closed = new HashSet<>();
		open.add(start);
		while (!open.isEmpty())
		{
			final BaseState x = open.poll();
			if (x.isGoal())
				return x;
			
			final Set<BaseState> children = x.expand();
			closed.add(x);
			children.removeIf(child -> open.contains(child) || closed.contains(child));
			open.addAll(children);
		}
		return null;
	}
	
	public static BaseState depthFirstSearch(final BaseState start)
	{
		final Stack<BaseState> open = new Stack<>();
		final Set<BaseState> closed = new HashSet<>();
		open.push(start);
		while (!open.isEmpty())
		{
			final BaseState x = open.pop();
			if (x.isGoal())
				return x;
			
			final Set<BaseState> children = x.expand();
			closed.add(x);
			children.removeIf(child -> open.contains(child) || closed.contains(child));
			open.addAll(children);
		}
		return null;
	}
	
	public static BaseState depthLimitedSearch(final BaseState node, final int depth)
	{
		if (depth == 0 && node.isGoal())
			return node;
		if (depth > 0)
		{
			for (final BaseState child : node.expand())
			{
				final BaseState found = depthLimitedSearch(child, depth - 1);
				if (found != null)
					return found;
			}
		}
		return null;
	}
	
	public static BaseState iterativeDeepingSearch(final BaseState root)
	{
		for (int depth = 0;depth < Integer.MAX_VALUE;depth++)
		{
			final BaseState found = depthLimitedSearch(root, depth);
			if (found != null)
				return found;
		}
		return null;
	}
	
	public static BaseState uniformCostSearch(final BaseState start)
	{
		final Queue<BaseState> open = new PriorityQueue<>(Comparator.comparing(BaseState::cost));
		open.add(start);
		while (!open.isEmpty())
		{
			final BaseState x = open.poll();
			if (x.isGoal())
				return x;
			
			open.addAll(x.expand());
		}
		return null;
	}
	
	public static BaseState backtrackingSearch(final BaseState state, final IOperation[] operations)
	{
		if (state.isGoal())
			return state;
		
		for (final IOperation o : operations)
		{
			if (o.test(state))
			{
				final BaseState goal = backtrackingSearch(o.apply(state), operations);
				if (goal != null)
					return goal;
			}
		}
		return null;
	}
}