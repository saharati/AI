package ai.mc;

import java.util.function.Function;
import java.util.function.Predicate;

import ai.BaseState;
import ai.IOperation;

public enum Operation implements IOperation
{
	MML(s -> s.MR >= 2 && (s.MR - 2 >= s.CR || s.MR - 2 == 0) && s.ML + 2 >= s.CL && s.B == Location.RIGHT, s -> new State(s.CL, s.ML + 2, s.CR, s.MR - 2, Location.LEFT, s)),
	CCL(s -> s.CR >= 2 && (s.CL + 2 <= s.ML || s.ML == 0) && s.B == Location.RIGHT, s -> new State(s.CL + 2, s.ML, s.CR - 2, s.MR, Location.LEFT, s)),
	MCL(s -> s.MR >= 1 && s.CR >= 1 && s.ML + 1 >= s.CL + 1 && s.B == Location.RIGHT, s -> new State(s.CL + 1, s.ML + 1, s.CR - 1, s.MR - 1, Location.LEFT, s)),
	ML(s -> s.MR >= 1 && (s.MR - 1 >= s.CR || s.MR - 1 == 0) && s.ML + 1 >= s.CL && s.B == Location.RIGHT, s -> new State(s.CL, s.ML + 1, s.CR, s.MR - 1, Location.LEFT, s)),
	CL(s -> s.CR >= 1 && (s.CL + 1 <= s.ML || s.ML == 0) && s.B == Location.RIGHT, s -> new State(s.CL + 1, s.ML, s.CR - 1, s.MR, Location.LEFT, s)),
	MMR(s -> s.ML >= 2 && (s.ML - 2 >= s.CL || s.ML - 2 == 0) && s.MR + 2 >= s.CR && s.B == Location.LEFT, s -> new State(s.CL, s.ML - 2, s.CR, s.MR + 2, Location.RIGHT, s)),
	CCR(s -> s.CL >= 2 && (s.CR + 2 <= s.MR || s.MR == 0) && s.B == Location.LEFT, s -> new State(s.CL - 2, s.ML, s.CR + 2, s.MR, Location.RIGHT, s)),
	MCR(s -> s.ML >= 1 && s.CL >= 1 && s.MR + 1 >= s.CR + 1 && s.B == Location.LEFT, s -> new State(s.CL - 1, s.ML - 1, s.CR + 1, s.MR + 1, Location.RIGHT, s)),
	MR(s -> s.ML >= 1 && (s.ML - 1 >= s.CL || s.ML - 1 == 0) && s.MR + 1 >= s.CR && s.B == Location.LEFT, s -> new State(s.CL, s.ML - 1, s.CR, s.MR + 1, Location.RIGHT, s)),
	CR(s -> s.CL >= 1 && (s.CR + 1 <= s.MR || s.MR == 0) && s.B == Location.LEFT, s -> new State(s.CL - 1, s.ML, s.CR + 1, s.MR, Location.RIGHT, s));
	
	private final Predicate<State> _domain;
	private final Function<State, State> _action;
	
	private Operation(final Predicate<State> domain, final Function<State, State> action)
	{
		_domain = domain;
		_action = action;
	}
	
	@Override
	public boolean test(BaseState state)
	{
		return _domain.test((State) state);
	}
	
	@Override
	public BaseState apply(BaseState state)
	{
		return _action.apply((State) state);
	}
}