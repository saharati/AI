package ai.mc;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import ai.BaseState;

public final class State implements BaseState
{
	private final State parent;
	
	public final int CL;
	public final int ML;
	public final int CR;
	public final int MR;
	public final Location B;
	
	public State(final int CL, final int ML, final int CR, final int MR, final Location B, final State parent)
	{
		this.parent = parent;
		this.CL = CL;
		this.ML = ML;
		this.CR = CR;
		this.MR = MR;
		this.B = B;
	}
	
	@Override
	public BaseState parent()
	{
		return parent;
	}
	
	@Override
	public Set<BaseState> expand()
	{
		final Set<BaseState> states = new LinkedHashSet<>();
		for (final Operation o : Operation.values())
			if (o.test(this))
				states.add(o.apply(this));
		return states;
	}
	
	@Override
	public boolean isGoal()
	{
		return MR == 0;
	}
	
	@Override
	public String toString()
	{
		return "CL=" + CL + ", ML=" + ML + ", CR=" + CR + ", MR=" + MR + ", B=" + B;
	}
	
	@Override
	public boolean equals(final Object o)
	{
		if (!(o instanceof State))
			return false;
		
		final State state = (State) o;
		if (CL != state.CL || ML != state.ML || CR != state.CR || MR != state.MR || B != state.B)
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode()
	{
		return Arrays.hashCode(new int[] {CL, ML, CR, MR, B.ordinal()});
	}
}