package ai;

import java.util.Set;

public interface BaseState
{
	public BaseState parent();
	
	public Set<BaseState> expand();
	
	default int cost()
	{
		throw new RuntimeException("A cost function is not implemenated for this problem.");
	}
	
	public boolean isGoal();
}