package ai.horse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import ai.Main;

public final class Horse
{
	private final ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);
	private final JLabel[][] board = new JLabel[8][8];
	private final JLabel title = new JLabel("Choose an Algorithm");
	private final JPanel backgroundPanel = new JPanel(new GridBagLayout());
	
	private ScheduledFuture<?> moveTask;
	private int index;
	
	public Horse()
	{
		final JFrame frame = new JFrame("AI - Sahar Atias");
		final JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
		title.setFont(new Font("Arial", Font.BOLD, 25));
		topPanel.add(title, BorderLayout.WEST);
		
		final JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 0));
		final JComboBox<String> algorithms = new JComboBox<>();
		algorithms.addItem("backtrackingSearch");
		actionPanel.add(algorithms);
		final JButton run = new JButton("Run");
		run.addActionListener(e -> run(algorithms.getItemAt(algorithms.getSelectedIndex())));
		actionPanel.add(run);
		topPanel.add(actionPanel, BorderLayout.EAST);
		frame.add(topPanel, BorderLayout.PAGE_START);
		
		final GridBagConstraints gc = new GridBagConstraints();
		for (int i = 0;i < board.length;i++)
		{
			for (int j = 0;j < board[i].length;j++)
			{
				gc.gridx = i + 1;
				gc.gridy = j + 1;
				
				board[i][j] = new JLabel(" ");
				board[i][j].setOpaque(true);
				board[i][j].setPreferredSize(new Dimension(80, 80));
				board[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
				board[i][j].setFont(new Font("Arial", Font.BOLD, 50));
				board[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				board[i][j].setBackground(Color.BLUE);
				board[i][j].setForeground(Color.CYAN);
				
				backgroundPanel.add(board[i][j], gc);
			}
		}
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
		final State start = new State();
		BaseState result = Algorithms.backtrackingSearch(start, Operation.values());
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
			
			Main.getInstance().getLog().append("=== " + getClass().getSimpleName() + " Solution (" + states.size() + " nodes, " + (System.currentTimeMillis() - startTime) + "ms) ===\r\n");
			states.forEach(s -> Main.getInstance().getLog().append(s + "\r\n"));
			
			title.setText("Solution (" + states.size() + " nodes, " + (System.currentTimeMillis() - startTime) + "ms)");
			
			if (moveTask != null)
			{
				moveTask.cancel(false);
				moveTask = null;
			}
			for (int i = 0;i < board.length;i++)
				for (int j = 0;j < board[i].length;j++)
					board[i][j].setText(" ");
			index = 1;
			
			task(states);
		}
	}
	
	private void task(final LinkedList<BaseState> states)
	{
		final State currentState = (State) states.get(index - 1);
		for (int i = 0;i < board.length;i++)
			for (int j = 0;j < board[i].length;j++)
				if (currentState.board[i][j] != 0)
					board[i][j].setText(String.valueOf(currentState.board[i][j]));
		
		if (states.size() == index)
			return;
		
		moveTask = threadPool.schedule(() ->
		{
			index++;
			task(states);
		}, 1500, TimeUnit.MILLISECONDS);
	}
}