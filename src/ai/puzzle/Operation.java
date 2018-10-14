package ai.puzzle;

import java.util.function.Function;
import java.util.function.Predicate;

public enum Operation
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
	
	public Predicate<State> getDomain()
	{
		return _domain;
	}
	
	public Function<State, State> getAction()
	{
		return _action;
	}
}