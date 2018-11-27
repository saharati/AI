package ai;

public interface IOperation
{
	public boolean test(final BaseState state);
	
	public BaseState apply(final BaseState state);
}