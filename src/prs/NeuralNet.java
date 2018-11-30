package prs;

public final class NeuralNet
{
	public static void copyChangeCode(final int[] newArray, final int[] oldArray)
	{
		for (int i = 0;i < 9;i++)
			newArray[i] = oldArray[i];
	}
	
	public static void copyValues(final int[][][] newArray, final int[][][] oldArray)
	{
		for (int i = 0;i < 3;i++)
			for (int j = 0;j < 3;j++)
				for (int k = 0;k < 3;k++)
					newArray[i][j][k] = oldArray[i][j][k];
	}
	
	public static void applyChange(final int[][][] values, final int[] changeCode)
	{
		for (int a = 0;a < 9;a++)
			changeCode[a]--;
		for (int i = 0;i < 3;i++)
		{
			for (int k = 0;k < 3;k++)
			{
				values[i][k][(0+k) % 3] += changeCode[(3*i) + 0];
				values[i][k][(1+k) % 3] += changeCode[(3*i) + 1];
				values[i][k][(2+k) % 3] += changeCode[(3*i) + 2];
			}
		}
		for (int a = 0;a < 9;a++)
			changeCode[a]++;
	}
	
	public static int maxIndex(final int[] array, final int length)
	{
		final int[] best = {0, array[0]};
		for (int i = 1;i < length;i++)
		{
			if (array[i] > best[1])
			{
				best[0] = i;
				best[1] = array[i];
			}
		}
		return best[0];
	}
	
	public static int makeMove(final int[][][] values, final int[] pastMoves)
	{
		final int[] decidingValues = new int[3];
		for (int i = 0;i < 3;i++)
		{
			final int pieceMoved = pastMoves[i];
			for (int j = 0;j < 3;j++)
				decidingValues[j] += values[i][pieceMoved][j];
		}
		return maxIndex(decidingValues, 3);
	}
	
	public static void updateArray(final int[] array, int newElement)
	{
		for (int i = 0;i < array.length - 1;i++)
			array[i] = array[i + 1];
		array[array.length - 1] = newElement;
	}
	
	public static int evaluateValues(final int[][][] values, final int[] data)
	{
		int score = 0;
		for (int i = 0;i < 7;i++)
		{
			final int[] tempMoves = {data[i], data[i + 1], data[i + 2]};
			final int wouldBeMove = makeMove(values, tempMoves);
			if (wouldBeMove == (data[i + 3] + 1) % 3 )
				score++;
			else
				score--;
		}
		return score;
	}
	
	public static boolean incrementBaseThree(final int[] number, final int length)
	{
		int i = 0;
		int carry = 1;
		while (carry != 0)
		{
			number[i]++;
			if (number[i] == 3)
				number[i] = 0;
			else
				carry = 0;
			i++;
			if (i == length && carry != 0)
				return false;
		}
		return true;
	}
	
	public static void hillClimb(final int[][][] values, final int[] pastData)
	{
		final int[] bestCode = new int[9];
		final int[] valueChangeCode = new int[9];
		final int[][][] valuesCopy = new int[3][3][3];
		
		int bestVal = evaluateValues(values, pastData);
		while (incrementBaseThree(valueChangeCode, 9))
		{
			copyValues(valuesCopy, values);
			applyChange(valuesCopy, valueChangeCode);
			final int evaluation = evaluateValues(valuesCopy, pastData);
			if (evaluation > bestVal)
			{
				bestVal = evaluation;
				copyChangeCode(bestCode, valueChangeCode);
			}
		}
		applyChange(values, bestCode);
	}
}