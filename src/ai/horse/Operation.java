package ai.horse;

import java.util.function.Function;
import java.util.function.Predicate;

public enum Operation
{
	RIGHTRIGHTDOWN(s -> s.iHorse + 2 < State.SIZE && s.jHorse + 1 < State.SIZE && s.board[s.iHorse + 2][s.jHorse + 1] == 0, s -> new State(s.iHorse + 2, s.jHorse + 1, s)),
	RIGHTDOWNDOWN(s -> s.iHorse + 1 < State.SIZE && s.jHorse + 2 < State.SIZE && s.board[s.iHorse + 1][s.jHorse + 2] == 0, s -> new State(s.iHorse + 1, s.jHorse + 2, s)),
	LEFTDOWNDOWN(s -> s.iHorse - 1 >= 0 && s.jHorse + 2 < State.SIZE && s.board[s.iHorse - 1][s.jHorse + 2] == 0, s -> new State(s.iHorse - 1, s.jHorse + 2, s)),
	LEFTLEFTDOWN(s -> s.iHorse - 2 >= 0 && s.jHorse + 1 < State.SIZE && s.board[s.iHorse - 2][s.jHorse + 1] == 0, s -> new State(s.iHorse - 2, s.jHorse + 1, s)),
	LEFTLEFTUP(s -> s.iHorse - 2 >= 0 && s.jHorse - 1 >= 0 && s.board[s.iHorse - 2][s.jHorse - 1] == 0, s -> new State(s.iHorse - 2, s.jHorse - 1, s)),
	LEFTUPUP(s -> s.iHorse - 1 >= 0 && s.jHorse - 2 >= 0 && s.board[s.iHorse - 1][s.jHorse - 2] == 0, s -> new State(s.iHorse - 1, s.jHorse - 2, s)),
	RIGHTUPUP(s -> s.iHorse + 1 < State.SIZE && s.jHorse - 2 >= 0 && s.board[s.iHorse + 1][s.jHorse - 2] == 0, s -> new State(s.iHorse + 1, s.jHorse - 2, s)),
	RIGHTRIGHTUP(s -> s.iHorse + 2 < State.SIZE && s.jHorse - 1 >= 0 && s.board[s.iHorse + 2][s.jHorse - 1] == 0, s -> new State(s.iHorse + 2, s.jHorse - 1, s));
	
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