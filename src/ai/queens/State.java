package ai.queens;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ai.BaseState;

public final class State implements BaseState
{
	private static final int SIZE = 8;
	
	private final State parent;
	
	public final boolean[][] board = new boolean[SIZE][SIZE];
	
	public State()
	{
		this.parent = null;
	}
	
	public State(final int newX, final int newY, final State parent)
	{
		this.parent = parent;
		
		for (int i = 0;i < SIZE;i++)
			for (int j = 0;j < SIZE;j++)
				board[i][j] = parent.board[i][j];
		board[newX][newY] = true;
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
		for (int i = 0;i < SIZE;i++)
		{
			boolean rowEmpty = true;
			for (int j = 0;j < SIZE && rowEmpty;j++)
				if (board[i][j])
					rowEmpty = false;
			if (rowEmpty)
			{
				for (int j = 0;j < SIZE;j++)
					if (!canBeAttacked(i, j))
						states.add(new State(i, j, this));
				break;
			}
		}
		return states;
	}
	
	@Override
	public boolean isGoal()
	{
		int count = 0;
		for (final boolean[] row : board)
			for (final boolean cell : row)
				if (cell)
					count++;
		
		return count == SIZE;
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
				sb.append(board[i][j] ? " ♛ " : " _ ");
				if (j < SIZE - 1)
					sb.append("|");
			}
			sb.append("]\n");
		}
		sb.append("\n");
		return sb.toString();
	}
	
	@Override
	public boolean equals(final Object o)
	{
		return o instanceof State && Arrays.deepEquals(board, ((State) o).board);
	}
	
	@Override
	public int hashCode()
	{
		return Arrays.deepHashCode(board);
	}
	
	private boolean canBeAttacked(final int i, final int j)
	{
		for (int x = 0;x < SIZE;x++)
			if (board[x][j])
				return true;
		for (int y = 0;y < SIZE;y++)
			if (board[i][y])
				return true;
		for (int x = i + 1, y = j + 1;x < SIZE && y < SIZE;x++, y++)
			if (board[x][y])
				return true;
		for (int x = i - 1, y = j - 1;x >= 0 && y >= 0;x--, y--)
			if (board[x][y])
				return true;
		for (int x = i + 1, y = j - 1;x < SIZE && y >= 0;x++, y--)
			if (board[x][y])
				return true;
		for (int x = i - 1, y = j + 1;x >= 0 && y < SIZE;x--, y++)
			if (board[x][y])
				return true;
		return false;
	}
}