package ai.puzzle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.LinkedList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ai.Algorithms;
import ai.BaseState;

public final class Puzzle
{
	private static final int DIRECTION_SPEED = 2;
	private static final int ANIMATION_SPEED = 10;
	private static final int[][] STARTING_STATE =
	{
		{5, 1, 3},
		{4, 0, 6},
		{7, 2, 8}
	};
	
	private final ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);
	private final JLabel[][] puzzle = new JLabel[3][3];
	private final JLabel title = new JLabel("Choose an Algorithm");
	private final JPanel backgroundPanel = new JPanel(null);
	
	private ScheduledFuture<?> moveTask;
	private int index;
	
	public Puzzle()
	{
		final JFrame frame = new JFrame("AI - Sahar Atias");
		final JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
		title.setFont(new Font("Arial", Font.BOLD, 25));
		topPanel.add(title, BorderLayout.WEST);
		
		final JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 0));
		final JComboBox<String> algorithms = new JComboBox<>();
		algorithms.addItem("breadthFirstSearch");
		algorithms.addItem("iterativeDeepingSearch");
		algorithms.addItem("uniformCostSearch");
		actionPanel.add(algorithms);
		final JButton run = new JButton("Run");
		run.addActionListener(e -> run(algorithms.getItemAt(algorithms.getSelectedIndex())));
		actionPanel.add(run);
		topPanel.add(actionPanel, BorderLayout.EAST);
		frame.add(topPanel, BorderLayout.PAGE_START);
		
		for (int i = 0;i < puzzle.length;i++)
		{
			for (int j = 0;j < puzzle[i].length;j++)
			{
				puzzle[i][j] = new JLabel(String.valueOf(STARTING_STATE[i][j]));
				puzzle[i][j].setOpaque(true);
				puzzle[i][j].setBounds(200 * j, 200 * i, 200, 200);
				puzzle[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
				puzzle[i][j].setFont(new Font("Arial", Font.BOLD, 50));
				puzzle[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				puzzle[i][j].setBackground(Color.BLUE);
				puzzle[i][j].setForeground(Color.CYAN);
				
				backgroundPanel.add(puzzle[i][j]);
			}
		}
		backgroundPanel.setPreferredSize(new Dimension(600, 600));
		frame.add(backgroundPanel, BorderLayout.PAGE_END);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void run(final String algorithm)
	{
		final long startTime = System.currentTimeMillis();
		final State start = new State(STARTING_STATE);
		BaseState result;
		switch (algorithm)
		{
			case "breadthFirstSearch":
				result = Algorithms.breadthFirstSearch(start);
				break;
			case "iterativeDeepingSearch":
				result = Algorithms.iterativeDeepingSearch(start);
				break;
			default:
				result = Algorithms.uniformCostSearch(start);
				break;
		}
		if (result == null)
		{
			System.out.println("There is no solution to the given problem.");
			System.out.println(start);
			
			title.setText("There is no solution to the given problem.");
		}
		else
		{
			final LinkedList<BaseState> states = new LinkedList<>();
			while (result != null)
			{
				states.addFirst(result);
				result = result.parent();
			}
			
			System.out.println("=== Solution (" + states.size() + " nodes, " + (System.currentTimeMillis() - startTime) + "ms) ===");
			states.forEach(System.out::println);
			
			title.setText("Solution (" + states.size() + " nodes, " + (System.currentTimeMillis() - startTime) + "ms)");
			
			if (moveTask != null)
			{
				moveTask.cancel(false);
				moveTask = null;
			}
			for (int i = 0;i < puzzle.length;i++)
			{
				for (int j = 0;j < puzzle[i].length;j++)
				{
					puzzle[i][j].setBounds(200 * j, 200 * i, 200, 200);
					puzzle[i][j].setText(String.valueOf(STARTING_STATE[i][j]));
				}
			}
			index = 1;
			
			task(states);
		}
	}
	
	private void task(final LinkedList<BaseState> states)
	{
		if (states.size() == index)
			return;
		
		final State currentState = (State) states.get(index - 1);
		final State nextState = (State) states.get(index);
		final JLabel from = puzzle[currentState.i0][currentState.j0];
		final JLabel to = puzzle[nextState.i0][nextState.j0];
		puzzle[currentState.i0][currentState.j0] = to;
		puzzle[nextState.i0][nextState.j0] = from;
		if (from.getX() == to.getX())
		{
			final int direction = to.getY() > from.getY() ? DIRECTION_SPEED : -DIRECTION_SPEED;
			final int toY = to.getY();
			
			moveTask = threadPool.scheduleAtFixedRate(() ->
			{
				from.setBounds(from.getX(), from.getY() + direction, 200, 200);
				to.setBounds(to.getX(), to.getY() - direction, 200, 200);
				if (from.getY() == toY)
				{
					index++;
					moveTask.cancel(false);
					threadPool.schedule(() -> task(states), 1, TimeUnit.SECONDS);
				}
			}, ANIMATION_SPEED, ANIMATION_SPEED, TimeUnit.MILLISECONDS);
		}
		else
		{
			final int direction = to.getX() > from.getX() ? DIRECTION_SPEED : -DIRECTION_SPEED;
			final int toX = to.getX();
			
			moveTask = threadPool.scheduleAtFixedRate(() ->
			{
				from.setBounds(from.getX() + direction, from.getY(), 200, 200);
				to.setBounds(to.getX() - direction, to.getY(), 200, 200);
				if (from.getX() == toX)
				{
					index++;
					moveTask.cancel(false);
					threadPool.schedule(() -> task(states), 1, TimeUnit.SECONDS);
				}
			}, ANIMATION_SPEED, ANIMATION_SPEED, TimeUnit.MILLISECONDS);
		}
	}
}