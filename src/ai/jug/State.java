package ai.jug;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ai.BaseState;

public final class State implements BaseState
{
	public static int g;
	public static int[] m;
	
	private final State parent;
	
	public final int[] c = new int[m.length];
	
	public State()
	{
		this.parent = null;
		
		c[c.length - 1] = m[m.length - 1];
	}
	
	public State(final int i, final int j, final int newIAmount, final int newJAmount, final State parent)
	{
		this.parent = parent;
		
		for (int x = 0;x < c.length;x++)
			c[x] = parent.c[x];
		c[i] = newIAmount;
		c[j] = newJAmount;
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
		for (int i = 0;i < c.length;i++)
		{
			for (int j = 0;j < c.length;j++)
			{
				if (i != j)
				{
					final int newIAmount, newJAmount;
					if (m[j] - c[j] >= c[i])
					{
						newIAmount = 0;
						newJAmount = c[j] + c[i];
					}
					else
					{
						newIAmount = c[i] - (m[j] - c[j]);
						newJAmount = m[j];
					}
					states.add(new State(i, j, newIAmount, newJAmount, this));
				}
			}
		}
		return states;
	}
	
	@Override
	public boolean isGoal()
	{
		for (int i = 0;i < c.length;i++)
			if (c[i] == g)
				return true;
		
		return false;
	}
	
	@Override
	public String toString()
	{
		return Arrays.toString(c);
	}
	
	@Override
	public boolean equals(final Object o)
	{
		return o instanceof State && Arrays.equals(c, ((State) o).c);
	}
	
	@Override
	public int hashCode()
	{
		return Arrays.hashCode(c);
	}
}