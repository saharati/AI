package ai.jug;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
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

import ai.Algorithms;
import ai.BaseState;
import util.ImageType;

public final class Jug
{
	private static final Color POOL_COLOR = new Color(79, 183, 255);
	
	private final ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);
	private final JLabel jug1Data = new JLabel();
	private final JLabel jug2Data = new JLabel();
	private final JLabel poolData = new JLabel();
	private final JLabel title = new JLabel("Choose an Algorithm");
	private final JPanel backgroundPanel = new JPanel(null)
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		protected void paintComponent(final Graphics g)
		{
			super.paintComponent(g);
			
			g.drawImage(ImageType.JUG_BACKGROUND.getIcon().getImage(), 0, 0, null);
			g.setColor(Color.WHITE);
			g.fillRect(50, 70, 100, 150);
			g.fillRect(290, 70, 100, 150);
			g.setColor(POOL_COLOR);
			
			final int jug1Current = Integer.parseInt(jug1Data.getText().split(" /")[0]);
			if (jug1Current > 0)
			{
				final int fill1Height = (int) (150 * ((double) jug1Current / State.m[0]));
				g.fillRect(50, 70 + (150 - fill1Height), 100, fill1Height);
			}
			final int jug2Current = Integer.parseInt(jug2Data.getText().split(" /")[0]);
			if (jug2Current > 0)
			{
				final int fill2Height = (int) (150 * ((double) jug2Current / State.m[1]));
				g.fillRect(290, 70 + (150 - fill2Height), 100, fill2Height);
			}
		}
	};
	
	private ScheduledFuture<?> moveTask;
	private int index;
	
	public Jug()
	{
		resetVariables();
		
		final JFrame frame = new JFrame("AI - Sahar Atias");
		final JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
		title.setFont(new Font("Arial", Font.BOLD, 25));
		topPanel.add(title, BorderLayout.WEST);
		
		final JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 0));
		final JComboBox<String> algorithms = new JComboBox<>();
		algorithms.addItem("breadthFirstSearch");
		algorithms.addItem("depthFirstSearch");
		algorithms.addItem("iterativeDeepingSearch");
		actionPanel.add(algorithms);
		final JButton run = new JButton("Run");
		run.addActionListener(e -> run(algorithms.getItemAt(algorithms.getSelectedIndex())));
		actionPanel.add(run);
		topPanel.add(actionPanel, BorderLayout.EAST);
		frame.add(topPanel, BorderLayout.PAGE_START);
		
		jug1Data.setBounds(75, 180, 300, 150);
		jug1Data.setFont(new Font("Arial", Font.BOLD, 25));
		jug2Data.setBounds(315, 180, 300, 150);
		jug2Data.setFont(new Font("Arial", Font.BOLD, 25));
		poolData.setBounds(560, 180, 300, 150);
		poolData.setFont(new Font("Arial", Font.BOLD, 25));
		
		backgroundPanel.add(jug1Data);
		backgroundPanel.add(jug2Data);
		backgroundPanel.add(poolData);
		backgroundPanel.setPreferredSize(new Dimension(800, 300));
		frame.add(backgroundPanel, BorderLayout.PAGE_END);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void resetVariables()
	{
		if (moveTask != null)
		{
			moveTask.cancel(false);
			moveTask = null;
		}
		
		State.g = 4;
		State.m = new int[] {3, 5, 10000};
		
		jug1Data.setText("0 / " + State.m[0]);
		jug2Data.setText("0 / " + State.m[1]);
		poolData.setText(State.m[2] + " / " + State.m[2]);
		index = 1;
	}
	
	private void run(final String algorithm)
	{
		final long startTime = System.currentTimeMillis();
		final State start = new State();
		BaseState result;
		switch (algorithm)
		{
			case "breadthFirstSearch":
				result = Algorithms.breadthFirstSearch(start);
				break;
			case "depthFirstSearch":
				result = Algorithms.depthFirstSearch(start);
				break;
			default:
				result = Algorithms.iterativeDeepingSearch(start);
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
			
			System.out.println("=== Solution (" + states.size() + " nodes) ===");
			states.forEach(System.out::println);
			
			title.setText("Solution (" + states.size() + " nodes, " + (System.currentTimeMillis() - startTime) + "ms)");
			
			resetVariables();
			task(states);
		}
	}
	
	private void task(final LinkedList<BaseState> states)
	{
		final State currentState = (State) states.get(index - 1);
		jug1Data.setText(currentState.c[0] + " / " + State.m[0]);
		jug2Data.setText(currentState.c[1] + " / " + State.m[1]);
		poolData.setText(currentState.c[2] + " / " + State.m[2]);
		backgroundPanel.repaint();
		
		if (index == states.size())
			return;
		
		moveTask = threadPool.schedule(() ->
		{
			index++;
			task(states);
		}, 3, TimeUnit.SECONDS);
	}
}