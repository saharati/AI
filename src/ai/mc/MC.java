package ai.mc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

public final class MC
{
	private static final int ANIMATION_SPEED = 25;
	
	private final ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);
	private final Map<Point, Boolean> humansRight = new HashMap<>();
	private final Map<Point, Boolean> cannibalsRight = new HashMap<>();
	private final Map<Point, Boolean> humansLeft = new HashMap<>();
	private final Map<Point, Boolean> cannibalsLeft = new HashMap<>();
	private final Map<Point, Boolean> onBoat = new HashMap<>();
	private final Point boat = new Point(360, 270);
	private final JLabel title = new JLabel("Choose an Algorithm");
	private final JPanel backgroundPanel = new JPanel(null)
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		protected void paintComponent(final Graphics g)
		{
			super.paintComponent(g);
			
			g.drawImage(ImageType.MC_BACKGROUND.getIcon().getImage(), 0, 0, null);
			
			humansRight.entrySet().stream().filter(e -> e.getValue()).forEach(e -> g.drawImage(ImageType.MC_HUMAN.getIcon().getImage(), e.getKey().x, e.getKey().y, null));
			cannibalsRight.entrySet().stream().filter(e -> e.getValue()).forEach(e -> g.drawImage(ImageType.MC_ZOMBIE.getIcon().getImage(), e.getKey().x, e.getKey().y, null));
			humansLeft.entrySet().stream().filter(e -> e.getValue()).forEach(e -> g.drawImage(ImageType.MC_HUMAN.getIcon().getImage(), e.getKey().x, e.getKey().y, null));
			cannibalsLeft.entrySet().stream().filter(e -> e.getValue()).forEach(e -> g.drawImage(ImageType.MC_ZOMBIE.getIcon().getImage(), e.getKey().x, e.getKey().y, null));
			onBoat.forEach((k, v) -> g.drawImage(v ? ImageType.MC_HUMAN.getIcon().getImage() : ImageType.MC_ZOMBIE.getIcon().getImage(), k.x, k.y, null));
			
			g.drawImage(ImageType.MC_BOAT.getIcon().getImage(), boat.x, boat.y, null);
		}
	};
	
	private ScheduledFuture<?> moveTask;
	private int index;
	
	public MC()
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
		
		backgroundPanel.setPreferredSize(new Dimension(800, 600));
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
		
		humansRight.clear();
		cannibalsRight.clear();
		humansLeft.clear();
		cannibalsLeft.clear();
		onBoat.clear();
		
		humansRight.put(new Point(635, 60), true);
		humansRight.put(new Point(635, 230), true);
		humansRight.put(new Point(635, 390), true);
		cannibalsRight.put(new Point(720, 60), true);
		cannibalsRight.put(new Point(720, 230), true);
		cannibalsRight.put(new Point(720, 390), true);
		
		humansLeft.put(new Point(25, 60), false);
		humansLeft.put(new Point(25, 230), false);
		humansLeft.put(new Point(25, 390), false);
		cannibalsLeft.put(new Point(100, 60), false);
		cannibalsLeft.put(new Point(100, 230), false);
		cannibalsLeft.put(new Point(100, 390), false);
		
		boat.setLocation(360, 270);
		index = 1;
	}
	
	private void run(final String algorithm)
	{
		final long startTime = System.currentTimeMillis();
		final State start = new State(0, 0, 3, 3, Location.RIGHT, null);
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
			
			System.out.println("=== Solution (" + states.size() + " nodes, " + (System.currentTimeMillis() - startTime) + "ms) ===");
			states.forEach(System.out::println);
			
			title.setText("Solution (" + states.size() + " nodes, " + (System.currentTimeMillis() - startTime) + "ms)");
			
			resetVariables();
			task(states);
		}
	}
	
	private void task(final LinkedList<BaseState> states)
	{
		if (index == states.size())
			return;
		
		final State currentState = (State) states.get(index - 1);
		final State nextState = (State) states.get(index);
		if (nextState.MR < currentState.MR)
		{
			final int MRsToMove = currentState.MR - nextState.MR;
			for (int j = 0;j < MRsToMove;j++)
			{
				final Entry<Point, Boolean> entry = humansRight.entrySet().stream().filter(e -> e.getValue()).findAny().get();
				entry.setValue(false);
				onBoat.put(new Point(entry.getKey()), true);
			}
		}
		else if (nextState.MR > currentState.MR)
		{
			final int MRsToMove = nextState.MR - currentState.MR;
			for (int j = 0;j < MRsToMove;j++)
			{
				final Entry<Point, Boolean> entry = humansLeft.entrySet().stream().filter(e -> e.getValue()).findAny().get();
				entry.setValue(false);
				onBoat.put(new Point(entry.getKey()), true);
			}
		}
		if (nextState.CR < currentState.CR)
		{
			final int CRsToMove = currentState.CR - nextState.CR;
			for (int j = 0;j < CRsToMove;j++)
			{
				final Entry<Point, Boolean> entry = cannibalsRight.entrySet().stream().filter(e -> e.getValue()).findAny().get();
				entry.setValue(false);
				onBoat.put(new Point(entry.getKey()), false);
			}
		}
		else if (nextState.CR > currentState.CR)
		{
			final int CRsToMove = nextState.CR - currentState.CR;
			for (int j = 0;j < CRsToMove;j++)
			{
				final Entry<Point, Boolean> entry = cannibalsLeft.entrySet().stream().filter(e -> e.getValue()).findAny().get();
				entry.setValue(false);
				onBoat.put(new Point(entry.getKey()), false);
			}
		}
		
		final List<Point> pointsToMove = new ArrayList<>(onBoat.keySet());
		if (currentState.B == Location.RIGHT)
		{
			pointsToMove.get(0).setLocation(460, 180);
			if (pointsToMove.size() == 2)
				pointsToMove.get(1).setLocation(400, 180);
			
			moveTask = threadPool.scheduleAtFixedRate(() ->
			{
				boat.setLocation(boat.x - 3, boat.y);
				for (final Point p : pointsToMove)
					p.setLocation(p.x - 3, p.y);
				if (boat.x == 210)
				{
					for (final boolean v : onBoat.values())
					{
						if (v)
							humansLeft.entrySet().stream().filter(e -> !e.getValue()).findAny().get().setValue(true);
						else
							cannibalsLeft.entrySet().stream().filter(e -> !e.getValue()).findAny().get().setValue(true);
					}
					onBoat.clear();
					
					index++;
					moveTask.cancel(false);
					threadPool.schedule(() -> task(states), 3, TimeUnit.SECONDS);
				}
				backgroundPanel.repaint();
			}, ANIMATION_SPEED, ANIMATION_SPEED, TimeUnit.MILLISECONDS);
		}
		else
		{
			pointsToMove.get(0).setLocation(250, 180);
			if (pointsToMove.size() == 2)
				pointsToMove.get(1).setLocation(310, 180);
			
			moveTask = threadPool.scheduleAtFixedRate(() ->
			{
				boat.setLocation(boat.x + 3, boat.y);
				for (final Point p : pointsToMove)
					p.setLocation(p.x + 3, p.y);
				if (boat.x == 360)
				{
					for (final boolean v : onBoat.values())
					{
						if (v)
							humansRight.entrySet().stream().filter(e -> !e.getValue()).findAny().get().setValue(true);
						else
							cannibalsRight.entrySet().stream().filter(e -> !e.getValue()).findAny().get().setValue(true);
					}
					onBoat.clear();
					
					index++;
					moveTask.cancel(false);
					threadPool.schedule(() -> task(states), 3, TimeUnit.SECONDS);
				}
				backgroundPanel.repaint();
			}, ANIMATION_SPEED, ANIMATION_SPEED, TimeUnit.MILLISECONDS);
		}
		backgroundPanel.repaint();
	}
}