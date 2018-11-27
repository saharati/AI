package ai.horse;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import ai.BaseState;

public final class State implements BaseState
{
	public static final int SIZE = 8;
	
	private final State parent;
	
	public final int[][] board = new int[SIZE][SIZE];
	public final int iHorse;
	public final int jHorse;
	
	public State()
	{
		parent = null;
		board[0][0] = 1;
		iHorse = 0;
		jHorse = 0;
	}
	
	public State(final int newX, final int newY, final State parent)
	{
		this.parent = parent;
		for (int i = 0;i < SIZE;i++)
			for (int j = 0;j < SIZE;j++)
				this.board[i][j] = parent.board[i][j];
		this.board[newX][newY] = parent.board[parent.iHorse][parent.jHorse] + 1;
		this.iHorse = newX;
		this.jHorse = newY;
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
		return board[iHorse][jHorse] == SIZE * SIZE;
	}
	
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0;i < SIZE;i++)
		{
			sb.append("[");
			for (int j = 0;j < SIZE;j++)
			{
				sb.append(board[i][j]);
				if (j < SIZE - 1)
					sb.append(",\t");
			}
			sb.append("]\n");
		}
		return sb.toString();
	}
	
	@Override
	public boolean equals(final Object o)
	{
		if (!(o instanceof State))
			return false;
		
		final State state = (State) o;
		final boolean[][] matrix = new boolean[SIZE][SIZE];
		final boolean[][] otherMatrix = new boolean[SIZE][SIZE];
		for (int i = 0;i < SIZE;i++)
		{
			for (int j = 0;j < SIZE;j++)
			{
				if (board[i][j] != 0)
				{
					matrix[i][j] = true;
					matrix[j][i] = true;
				}
			}
		}
		for (int i = 0;i < SIZE;i++)
		{
			for (int j = 0;j < SIZE;j++)
			{
				if (state.board[i][j] != 0)
				{
					otherMatrix[i][j] = true;
					otherMatrix[j][i] = true;
				}
			}
		}
		return Arrays.deepEquals(matrix, otherMatrix);
	}
	
	@Override
	public int hashCode()
	{
		final boolean[][] matrix = new boolean[SIZE][SIZE];
		for (int i = 0;i < SIZE;i++)
		{
			for (int j = 0;j < SIZE;j++)
			{
				if (board[i][j] != 0)
				{
					matrix[i][j] = true;
					matrix[j][i] = true;
				}
			}
		}
		return Arrays.deepHashCode(matrix);
	}
}