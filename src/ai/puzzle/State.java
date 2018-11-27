package ai.puzzle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ai.BaseState;

public final class State implements BaseState
{
	private static final int[][] GOAL =
	{
		{1, 2, 3},
		{4, 5, 6},
		{7, 8, 0}
	};
	
	private final State parent;
	
	public final int[][] t;
	public int i0;
	public int j0;
	
	public State(final int[][] t)
	{
		this.parent = null;
		this.t = t;
		
		boolean found = false;
		for (int i = 0;i < t.length && !found;i++)
		{
			for (int j = 0;j < t.length && !found;j++)
			{
				if (t[i][j] == 0)
				{
					i0 = i;
					j0 = j;
					found = true;
				}
			}
		}
	}
	
	public State(final int newi0, final int newj0, final State parent)
	{
		this.parent = parent;
		
		t = new int[parent.t.length][parent.t.length];
		for (int i = 0;i < t.length;i++)
			for (int j = 0;j < t.length;j++)
				t[i][j] = parent.t[i][j];
		t[parent.i0][parent.j0] = t[newi0][newj0];
		t[newi0][newj0] = 0;
		
		i0 = newi0;
		j0 = newj0;
	}
	
	@Override
	public BaseState parent()
	{
		return parent;
	}
	
	@Override
	public Set<BaseState> expand()
	{
		final Set<BaseState> states = new HashSet<>();
		for (final Operation o : Operation.values())
			if (o.test(this))
				states.add(o.apply(this));
		return states;
	}
	
	@Override
	public int cost()
	{
		int f = 0;
		BaseState parent = this.parent;
		while (parent != null)
		{
			f++;
			parent = parent.parent();
		}
		
		int h = 0;
		for (int i = 0;i < t.length;i++)
		{
			for (int j = 0;j < t.length;j++)
			{
				if (t[i][j] == 0)
					continue;
				
				boolean found = false;
				for (int x = 0;x < t.length && !found;x++)
				{
					for (int y = 0;y < t.length && !found;y++)
					{
						if (t[i][j] != GOAL[x][y])
						{
							h++;
							found = true;
						}
					}
				}
			}
		}
		
		return f + h;
	}
	
	@Override
	public boolean isGoal()
	{
		return Arrays.deepEquals(t, GOAL);
	}
	
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		for (final int[] line : t)
		{
			sb.append(Arrays.toString(line));
			sb.append("\n");
		}
		return sb.toString();
	}
	
	@Override
	public boolean equals(final Object o)
	{
		return o instanceof State && Arrays.deepEquals(t, ((State) o).t);
	}
	
	@Override
	public int hashCode()
	{
		return Arrays.deepHashCode(t);
	}
}