package ai.puzzle;

import java.util.function.Function;
import java.util.function.Predicate;

import ai.BaseState;
import ai.IOperation;

public enum Operation implements IOperation
{
	UP(s -> s.i0 > 0, s -> new State(s.i0 - 1, s.j0, s)),
	DOWN(s -> s.i0 < s.t.length - 1, s -> new State(s.i0 + 1, s.j0, s)),
	LEFT(s -> s.j0 > 0, s -> new State(s.i0, s.j0 - 1, s)),
	RIGHT(s -> s.j0 < s.t.length - 1, s -> new State(s.i0, s.j0 + 1, s));
	
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