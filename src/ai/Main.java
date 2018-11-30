package ai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import ai.horse.Horse;
import ai.jug.Jug;
import ai.mc.MC;
import ai.puzzle.Puzzle;
import ai.queens.Queens;

public final class Main
{
	private final JTextArea log = new JTextArea();
	
	private Main()
	{
		final JFrame frame = new JFrame("AI - Sahar Atias");
		final JPanel contentPanel = new JPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		frame.setContentPane(contentPanel);
		frame.setLayout(new BorderLayout());
		
		final JPanel leftPane = new JPanel(new BorderLayout());
		leftPane.setBackground(new Color(0x7B, 0xD9, 0xF1));
		
		final JLabel selectGame = new JLabel("Choose a Problem");
		selectGame.setFont(new Font("Arial", Font.BOLD, 25));
		selectGame.setHorizontalAlignment(SwingConstants.CENTER);
		leftPane.add(selectGame, BorderLayout.PAGE_START);
		
		final JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		buttonsPanel.add(Box.createVerticalStrut(10));
		
		final JButton mc = new JButton("Three Missionaries");
		mc.addActionListener(e -> new MC());
		mc.setAlignmentX(Component.CENTER_ALIGNMENT);
		mc.setMaximumSize(new Dimension(Integer.MAX_VALUE, mc.getMinimumSize().height));
		buttonsPanel.add(mc);
		buttonsPanel.add(Box.createVerticalStrut(10));
		final JButton puzzle = new JButton("Puzzle");
		puzzle.addActionListener(e -> new Puzzle());
		puzzle.setAlignmentX(Component.CENTER_ALIGNMENT);
		puzzle.setMaximumSize(new Dimension(Integer.MAX_VALUE, puzzle.getMinimumSize().height));
		buttonsPanel.add(puzzle);
		buttonsPanel.add(Box.createVerticalStrut(10));
		final JButton horse = new JButton("Horse");
		horse.addActionListener(e -> new Horse());
		horse.setAlignmentX(Component.CENTER_ALIGNMENT);
		horse.setMaximumSize(new Dimension(Integer.MAX_VALUE, horse.getMinimumSize().height));
		buttonsPanel.add(horse);
		buttonsPanel.add(Box.createVerticalStrut(10));
		final JButton jugs = new JButton("Jugs");
		jugs.addActionListener(e -> new Jug());
		jugs.setAlignmentX(Component.CENTER_ALIGNMENT);
		jugs.setMaximumSize(new Dimension(Integer.MAX_VALUE, jugs.getMinimumSize().height));
		buttonsPanel.add(jugs);
		buttonsPanel.add(Box.createVerticalStrut(10));
		final JButton queens = new JButton("Queens");
		queens.addActionListener(e -> new Queens());
		queens.setAlignmentX(Component.CENTER_ALIGNMENT);
		queens.setMaximumSize(new Dimension(Integer.MAX_VALUE, queens.getMinimumSize().height));
		buttonsPanel.add(queens);
		buttonsPanel.setBackground(new Color(0x7B, 0xD9, 0xF1));
		leftPane.add(buttonsPanel, BorderLayout.CENTER);
		
		final JPanel rightPane = new JPanel(new BorderLayout());
		rightPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		rightPane.setBackground(new Color(0x7B, 0xD9, 0xF1));
		
		final JScrollPane scrollPane = new JScrollPane(log);
		scrollPane.setPreferredSize(new Dimension(350, log.getPreferredSize().height));
		
		log.setFont(new Font("Arial", Font.BOLD, 15));
		log.setEditable(false);
		log.setBorder(LineBorder.createBlackLineBorder());
		log.append("Chat Log\r\n");
		
		rightPane.add(scrollPane, BorderLayout.CENTER);
		
		frame.add(leftPane, BorderLayout.WEST);
		frame.add(rightPane, BorderLayout.CENTER);
		frame.getContentPane().setBackground(new Color(0x7B, 0xD9, 0xF1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public JTextArea getLog()
	{
		return log;
	}
	
	public static final Main getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final Main INSTANCE = new Main();
	}
	
	public static void main(final String[] args)
	{
		getInstance();
	}
}