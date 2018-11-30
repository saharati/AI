package prs;

import java.awt.Color;
import java.awt.Font;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import util.ImageType;

public final class PRS extends JFrame
{
	private static final long serialVersionUID = -838102525274691708L;
	
	private static final int LAST_X_NUMBER = 3;
	private static final Random RND = new Random();
	private static final ImageIcon[] ICONS = {ImageType.PRS_ROCK.getIcon(), ImageType.PRS_PAPER.getIcon(), ImageType.PRS_SCISSORS.getIcon()};
	private static final Map<String, String> OPPOSITIONS = new HashMap<>();
	private static final Map<String, Integer> INDEXES = new HashMap<>();
	static
	{
		OPPOSITIONS.put("rock", "paper");
		OPPOSITIONS.put("paper", "scissors");
		OPPOSITIONS.put("scissors", "rock");
		
		INDEXES.put("rock", 0);
		INDEXES.put("paper", 1);
		INDEXES.put("scissors", 2);
	};
	
	private final int[][][] _evaluateRecentMoves = new int[3][3][3];
	private final int[] _pastMoves = {0, 1, 2};
	private final int[] extendedPastMoves = new int[10];
	private int[][] _markovChain = new int[3][3];
	private final LinkedList<String> _lastX = new LinkedList<>();
	private final Map<String, Integer> _observations = new HashMap<>();
	private final ScheduledThreadPoolExecutor _stpe = new ScheduledThreadPoolExecutor(1);
	private final JLabel _computerLabel;
	private final JLabel _userLabel;
	private final JLabel _computerScore;
	private final JLabel _userScore;
	
	private int _randomCounter;
	private String _userSelection;
	private String _previousUserSelection;
	private String _method = "Random";
	private boolean _userTurn = true;
	
	public PRS()
	{
		super("Rock, Paper, Scissors");
		
		final Font methodFont = new Font("Arial", Font.BOLD, 15);
		final JLabel method = new JLabel("Method:");
		method.setFont(methodFont);
		method.setBounds(10, 5, 300, 25);
		add(method);
		final JComboBox<String> methodBox = new JComboBox<>();
		methodBox.addItem("Random");
		methodBox.addItem("Observations");
		methodBox.addItem("Last X");
		methodBox.addItem("Neural Network");
		methodBox.addItem("Markov Chain");
		methodBox.addItem("Cheat");
		methodBox.addActionListener(e -> _method = (String) methodBox.getSelectedItem());
		methodBox.setFont(methodFont);
		methodBox.setBounds(80, 5, 150, 25);
		add(methodBox);
		
		final Font font = new Font("Arial", Font.BOLD, 33);
		final JLabel computer = new JLabel("Computer");
		computer.setFont(font);
		computer.setBounds(50, 67, 300, 30);
		add(computer);
		final JLabel user = new JLabel("User");
		user.setFont(font);
		user.setBounds(50, 222, 300, 30);
		add(user);
		
		_computerLabel = new JLabel(ImageType.PRS_EMPTY.getIcon());
		_computerLabel.setBounds(270, 10, 131, 145);
		add(_computerLabel);
		_userLabel = new JLabel(ImageType.PRS_EMPTY.getIcon());
		_userLabel.setBounds(270, 165, 131, 145);
		add(_userLabel);
		
		_computerScore = new JLabel("Score: 0");
		_computerScore.setFont(font);
		_computerScore.setBounds(480, 67, 300, 30);
		add(_computerScore);
		_userScore = new JLabel("Score: 0");
		_userScore.setFont(font);
		_userScore.setBounds(480, 222, 300, 30);
		add(_userScore);
		
		final JButton rockButton = new JButton(ImageType.PRS_ROCKBUTTON.getIcon());
		rockButton.addActionListener(e -> makeUserSelection("rock", ImageType.PRS_ROCK.getIcon()));
		rockButton.setBounds(10, 320, 210, 290);
		add(rockButton);
		final JButton paperButton = new JButton(ImageType.PRS_PAPERBUTTON.getIcon());
		paperButton.addActionListener(e -> makeUserSelection("paper", ImageType.PRS_PAPER.getIcon()));
		paperButton.setBounds(230, 320, 210, 290);
		add(paperButton);
		final JButton scissorsButton = new JButton(ImageType.PRS_SCISSORSBUTTON.getIcon());
		scissorsButton.addActionListener(e -> makeUserSelection("scissors", ImageType.PRS_SCISSORS.getIcon()));
		scissorsButton.setBounds(450, 320, 210, 290);
		add(scissorsButton);
		
		getContentPane().setBackground(new Color(0x7B, 0xD9, 0xF1));
		setSize(675, 655);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	private void makeUserSelection(final String selection, final ImageIcon icon)
	{
		if (!_userTurn)
			return;
		
		_userTurn = false;
		_randomCounter = 0;
		_userSelection = selection;
		_userLabel.setIcon(icon);
		
		showRandomIcons();
	}
	
	private void makeComputerSelection()
	{
		final ImageIcon icon;
		switch (_method)
		{
			case "Random":
				icon = ICONS[RND.nextInt(ICONS.length)];
				break;
			case "Last X":
				final Map<String, Integer> observations = new HashMap<>();
				_lastX.forEach(x -> observations.compute(x, (k, v) -> v == null ? 1 : v + 1));
				
				final Entry<String, Integer> entry = observations.isEmpty() ? null : Collections.max(observations.entrySet(), Entry.comparingByValue());
				if (entry == null)
					icon = ICONS[RND.nextInt(ICONS.length)];
				else
				{
					final String computerSelection = OPPOSITIONS.get(entry.getKey());
					switch (computerSelection)
					{
						case "rock":
							icon = ImageType.PRS_ROCK.getIcon();
							break;
						case "paper":
							icon = ImageType.PRS_PAPER.getIcon();
							break;
						default:
							icon = ImageType.PRS_SCISSORS.getIcon();
							break;
					}
				}
				break;
			case "Neural Network":
				icon = ICONS[NeuralNet.makeMove(_evaluateRecentMoves, _pastMoves)];
				break;
			case "Markov Chain":
				if (_previousUserSelection == null)
					icon = ICONS[RND.nextInt(ICONS.length)];
				else
				{
					final int prevIndex = INDEXES.get(_previousUserSelection);
					
					int nextIndex = 0;
					for (int i = 0;i < _markovChain.length;i++)
						if (_markovChain[prevIndex][i] > _markovChain[prevIndex][nextIndex])
							nextIndex = i;
					switch (nextIndex)
					{
						case 0:
							icon = ImageType.PRS_PAPER.getIcon();
							break;
						case 1:
							icon = ImageType.PRS_SCISSORS.getIcon();
							break;
						default:
							icon = ImageType.PRS_ROCK.getIcon();
							break;
					}
				}
				break;
			case "Cheat":
				switch (OPPOSITIONS.get(_userSelection))
				{
					case "rock":
						icon = ImageType.PRS_ROCK.getIcon();
						break;
					case "paper":
						icon = ImageType.PRS_PAPER.getIcon();
						break;
					default:
						icon = ImageType.PRS_SCISSORS.getIcon();
						break;
				}
				break;
			default: // Observations
				final String computerSelection = _observations.isEmpty() ? null : OPPOSITIONS.get(Collections.max(_observations.entrySet(), Entry.comparingByValue()).getKey());
				if (computerSelection == null)
					icon = ICONS[RND.nextInt(ICONS.length)];
				else
				{
					switch (computerSelection)
					{
						case "rock":
							icon = ImageType.PRS_ROCK.getIcon();
							break;
						case "paper":
							icon = ImageType.PRS_PAPER.getIcon();
							break;
						default:
							icon = ImageType.PRS_SCISSORS.getIcon();
							break;
					}
				}
				break;
		}
		
		_computerLabel.setIcon(icon);
		
		if (icon == ImageType.PRS_ROCK.getIcon())
		{
			switch (_userSelection)
			{
				case "rock":
					break;
				case "paper":
					_userScore.setText("Score: " + (extractScore(_userScore) + 1));
					break;
				case "scissors":
					_computerScore.setText("Score: " + (extractScore(_computerScore) + 1));
					break;
			}
		}
		else if (icon == ImageType.PRS_PAPER.getIcon())
		{
			switch (_userSelection)
			{
				case "rock":
					_computerScore.setText("Score: " + (extractScore(_computerScore) + 1));
					break;
				case "paper":
					break;
				case "scissors":
					_userScore.setText("Score: " + (extractScore(_userScore) + 1));
					break;
			}
		}
		else
		{
			switch (_userSelection)
			{
				case "rock":
					_userScore.setText("Score: " + (extractScore(_userScore) + 1));
					break;
				case "paper":
					_computerScore.setText("Score: " + (extractScore(_computerScore) + 1));
					break;
				case "scissors":
					break;
			}
		}
		
		final int index = INDEXES.get(_userSelection);
		NeuralNet.updateArray(_pastMoves, index);
		NeuralNet.updateArray(extendedPastMoves, index);
		NeuralNet.hillClimb(_evaluateRecentMoves, extendedPastMoves);
		
		if (_previousUserSelection != null)
			_markovChain[INDEXES.get(_previousUserSelection)][index]++;
		_previousUserSelection = _userSelection;
		_lastX.addFirst(_userSelection);
		if (_lastX.size() > LAST_X_NUMBER)
			_lastX.removeLast();
		_observations.compute(_userSelection, (k, v) -> v == null ? 1 : v + 1);
		_userTurn = true;
	}
	
	private void showRandomIcons()
	{
		_stpe.schedule(() ->
		{
			_randomCounter++;
			if (_randomCounter == 30)
				makeComputerSelection();
			else
			{
				_computerLabel.setIcon(ICONS[RND.nextInt(ICONS.length)]);
				showRandomIcons();
			}
		}, 33, TimeUnit.MILLISECONDS);
	}
	
	private static int extractScore(final JLabel label)
	{
		return Integer.parseInt(label.getText().split(": ")[1]);
	}
}