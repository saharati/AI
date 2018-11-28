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
import javax.swing.SwingConstants;

import ai.horse.Horse;
import ai.jug.Jug;
import ai.mc.MC;
import ai.puzzle.Puzzle;
import ai.queens.Queens;

public final class Main
{
	public static void main(final String[] args)
	{
		final JFrame frame = new JFrame("AI - Sahar Atias");
		final JPanel contentPanel = new JPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		frame.setContentPane(contentPanel);
		frame.setLayout(new BorderLayout());
		
		final JLabel selectGame = new JLabel("Choose a Problem");
		selectGame.setFont(new Font("Arial", Font.BOLD, 25));
		selectGame.setHorizontalAlignment(SwingConstants.CENTER);
		frame.add(selectGame, BorderLayout.PAGE_START);
		
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
		horse.addActionListener(e -> Horse.open());
		horse.setAlignmentX(Component.CENTER_ALIGNMENT);
		horse.setMaximumSize(new Dimension(Integer.MAX_VALUE, horse.getMinimumSize().height));
		buttonsPanel.add(horse);
		buttonsPanel.add(Box.createVerticalStrut(10));
		final JButton jugs = new JButton("Jugs");
		jugs.addActionListener(e -> Jug.open());
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
		frame.add(buttonsPanel, BorderLayout.CENTER);
		
		frame.getContentPane().setBackground(new Color(0x7B, 0xD9, 0xF1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}