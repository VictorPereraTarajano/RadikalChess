package aima.gui.applications.search.games;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import aima.core.environment.radikalchess.RadikalChessAction;
import aima.core.environment.radikalchess.RadikalChessGame;
import aima.core.environment.radikalchess.RadikalChessPiece;
import aima.core.environment.radikalchess.RadikalChessState;
import aima.core.environment.radikalchess.heuristics.RadikalChessHeuristics;
import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.adversarial.AlphaBetaSearchLimited;
import aima.core.search.adversarial.MinimaxSearchLimited;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Metrics;
import aima.core.util.datastructure.XYLocation;

public class RadikalChessApp extends JFrame implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	private int posX = 100, posY = 100, width = 850, height = 800, gameReviewIterator = 0;
	
	private static Color Black = new Color (99,99,99);
	private static Color White= Color.WHITE;
	
	private RadikalChessButton [] [] boardGUI;
	private RadikalChessGame game;
	private RadikalChessState currentState;
	
	private JPanel gamePanel, appPanel;
	private JLabel statusBar;
	private JToolBar tbar;
	private JTextField textField;
	private JComboBox<?> comboBoxAlg, comboBoxDif;
	private JButton proposeMove, clear, recreateBoard, gameReviewF, gameReviewB;
	
	private int doAction = -1;
	private XYLocation locOri;
	private ArrayList<RadikalChessAction> locMoves;
	private Metrics searchMetrics;
	private List<RadikalChessState> gameReview;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RadikalChessApp () {
		super("RadikalChess");
		this.setIconImage(new ImageIcon(getClass().getResource("KN.png")).getImage());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(posX, posY, width, height);
		
		game = new RadikalChessGame();
		
		currentState = game.getInitialState();
		
		tbar = new JToolBar();
		tbar.setFloatable(false);
		
		statusBar = new JLabel(" ");
		
		gamePanel = new JPanel(new GridLayout(currentState.getRows(), currentState.getColumns()));
		
		comboBoxDif = new JComboBox(new String [] {" H1", " H2", " H3"," H4", " H5", " H6"});
		comboBoxAlg = new JComboBox(new String [] {" Minimax SearchLimited ", " AlphaBeta SearchLimited "});
		comboBoxAlg.setSelectedIndex(1);
		comboBoxDif.setSelectedIndex(0);
		
		appPanel = (JPanel) getContentPane();
		appPanel.setLayout(new BorderLayout());
		
		proposeMove = new JButton(" Propose Move ");
		clear = new JButton(" Clear ");
		recreateBoard = new JButton(" Recreate Board ");
		gameReviewF = new JButton("\u25BA");
		gameReviewB = new JButton("\u25C4");
		gameReviewF.setEnabled(false);
		gameReviewB.setEnabled(false);
		
		textField = new JTextField("<KN,QN,AN,>,<,,,TN>,<,,QB,>,<,,,>,<,,PB,>,<,,KB,QB>");
		
		boardGUI = new RadikalChessButton [currentState.getRows()] [currentState.getColumns()];
		
		proposeMove.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!game.isTerminal(currentState)) {
					proposeMove();
				}
			}
			
			});
		
		clear.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				gameReviewIterator = 0;
				currentState = game.getInitialState();
				gameReviewB.setEnabled(false);
				gameReviewF.setEnabled(false);
				repaintBoardGUI(currentState);
				updateStatus();
			}
			
		});
		
		recreateBoard.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				recreateBoardGUI();
				gameReviewB.setEnabled(false);
				gameReviewF.setEnabled(false);
				updateStatus();
			}
			
		});
		
		gameReviewF.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gameReviewIterator < gameReview.size() - 1) {
					gameReviewIterator++;
					repaintBoardGUI(gameReview.get(gameReviewIterator));
				}
			}
			
		});
		
		gameReviewB.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gameReviewIterator > 0) {
					gameReviewIterator--;
					repaintBoardGUI(gameReview.get(gameReviewIterator));
				}
			}
			
		});
		
		initBoardGUI(currentState);
		
		tbar.add(comboBoxAlg);
		tbar.add(comboBoxDif);
		tbar.add(Box.createHorizontalGlue());
		tbar.add(textField);
		tbar.add(recreateBoard);
		tbar.add(clear);
		tbar.add(proposeMove);
		tbar.add(gameReviewB);
		tbar.add(gameReviewF);
		
		appPanel.add(statusBar, BorderLayout.SOUTH);
		appPanel.add(tbar, BorderLayout.NORTH);
		appPanel.add(gamePanel, BorderLayout.CENTER);
		
		updateStatus();
		
	}
	
	private void recreateBoardGUI () {
		
		String text = textField.getText();
		int rows = text.length() - text.replaceAll("<", "").length();		
		String subtext = text.substring(text.indexOf('<'), text.indexOf('>'));		
		int columns = subtext.length() - subtext.replaceAll( ",", "").length();	
		String [] [] newBoard = new String [rows][columns + 1];		
		int init = 0, fin = 0;
		
		for (int i = 0; i < rows; i++) {
			init = text.indexOf('<', fin);
			fin = text.indexOf('>', fin+1);
			subtext = text.substring(init+1, fin);
			String res = " ";
			int col = 0;
			for (int j = 0; j < subtext.length(); j++) {
				if (subtext.charAt(j) == ',') {
					newBoard [i][col] = res.trim();
					res = " ";
					col++;
				} else {
					if (j == subtext.length() - 1) {
						res += subtext.charAt(j);
						newBoard [i][col] = res.trim();
					} else {
						res += subtext.charAt(j);
					}
				}
			}
		}
		
		game = new RadikalChessGame (newBoard);
		clearBoardGUI(currentState);
		initBoardGUI(currentState = game.getInitialState());
		gamePanel.updateUI();
	}
	
	private void initBoardGUI (RadikalChessState state) {
		boardGUI = new RadikalChessButton [state.getRows()] [state.getColumns()];
		for (int i = 0; i < state.getRows(); i++) {
			for (int j = 0; j < state.getColumns(); j++) {
				if (state.getValue(i, j) != null) {
					boardGUI[i][j] = new RadikalChessButton (state.getValue(i, j), i, j);
				} else {
					boardGUI[i][j] = new RadikalChessButton (i, j);
				}
				boardGUI[i][j].addMouseListener(this);
				gamePanel.add(boardGUI[i][j]);
			}
		}
		
	}
	
	private void clearBoardGUI(RadikalChessState state) {
		for (int i = 0; i < state.getRows(); i++) {
			for (int j = 0; j < state.getColumns(); j++) {
				gamePanel.remove(boardGUI[i][j]);
			}
		}
	}
	
	private void repaintBoardGUI (RadikalChessState state) {
		for (int i = 0; i < state.getRows(); i++) {
			for (int j = 0; j < state.getColumns(); j++) {
				boardGUI[i][j].piece = state.getValue(i, j);
			}
		}
		repaint();
	}
	
	private void paintValidMoves (XYLocation locPiece) {
		if (locMoves != null) {
			for (RadikalChessAction locs : locMoves) {
				boardGUI[locs.getXYLocation().getXCoOrdinate()][locs.getXYLocation().getYCoOrdinate()].setBackground(Color.WHITE);
			}
		}
		locMoves = currentState.getPlayerMoves(currentState.getPlayerToMove());
		ArrayList<RadikalChessAction> locAux = new ArrayList<RadikalChessAction>();		
		for (RadikalChessAction locs : locMoves) {
			if (locs.getPiece().getXYLocation().equals(locPiece)) {
				boardGUI[locs.getXYLocation().getXCoOrdinate()][locs.getXYLocation().getYCoOrdinate()].markMoves = true;
				if (!currentState.isEnemyHere(currentState.getValue(locOri.getXCoOrdinate(), locOri.getYCoOrdinate()), locs.getXYLocation())) {
					boardGUI[locs.getXYLocation().getXCoOrdinate()][locs.getXYLocation().getYCoOrdinate()].backgroundColor = new Color(34,139,34);
				} else {
					boardGUI[locs.getXYLocation().getXCoOrdinate()][locs.getXYLocation().getYCoOrdinate()].backgroundColor = new Color (255,10,10);
				}
			} else {
				locAux.add(locs);
			}
		}
		locMoves.removeAll(locAux);
		repaint();
	}
	
	private HeuristicFunction getEvalComboBox () {
		switch (comboBoxDif.getSelectedIndex()) {
			case 0:
			 	return new RadikalChessHeuristics(true, false, false, false, false);
			case 1:
				return new RadikalChessHeuristics(false, true, false, false, false);
			case 2: 
				return new RadikalChessHeuristics(false, false, true, false, false);
			case 3:
				return new RadikalChessHeuristics(false, false, false, true, false);
			case 4:
				return new RadikalChessHeuristics(false, false , false, false, true);
			default:
				return new RadikalChessHeuristics(true, false, false, true, true);
		}
	}
	
	private void proposeMove () {
		AdversarialSearch<RadikalChessState, RadikalChessAction> search;
		switch (comboBoxAlg.getSelectedIndex()) {
			case 0:
					search = MinimaxSearchLimited.createFor(game,3, getEvalComboBox());
					break;
			default:
					search = AlphaBetaSearchLimited.createFor(game,3, getEvalComboBox());
		}
		RadikalChessAction action = search.makeDecision(currentState);
		searchMetrics = search.getMetrics();
		currentState = game.getResult(currentState, action);
		game.addStateToReview(currentState);
		repaintBoardGUI(currentState);
		currentState.analyzeUtility();
		updateStatus();
	}
	
	private void updateStatus () {
		String statusText;
		if (game.isTerminal(currentState)) {
			gameReviewB.setEnabled(true);
			gameReviewF.setEnabled(true);
			gameReview = game.getGameReview();
			gameReviewIterator = gameReview.size() - 1;
			if (game.getUtility(currentState, RadikalChessState.N) == 0) 
				statusText = " B has won :-)";
			else
				statusText = " N has won :-)";
		} else
			statusText = " Next move: " + game.getPlayer(currentState);
		if (searchMetrics != null)
			statusText += "    " + searchMetrics;
		statusBar.setText(statusText);
	}
	
	public static void main (String args []) {
		RadikalChessApp game = new RadikalChessApp();
		//game.setResizable(false);
		game.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (!game.isTerminal(currentState)) {
			XYLocation locButton = ((RadikalChessButton) e.getComponent()).loc;
			if (doAction < 0 && currentState.getValue(locButton.getXCoOrdinate(), locButton.getYCoOrdinate()) != null) {
				locOri = locButton;
				if (currentState.getValue(locOri.getXCoOrdinate(), locOri.getYCoOrdinate()).toString().contains(currentState.getPlayerToMove()) && currentState.getValue(locOri.getXCoOrdinate(), locOri.getYCoOrdinate()) != null) {
					paintValidMoves(locButton);
					doAction *= -1;
				}
			} else {
				if (doAction > 0) {
					for (RadikalChessAction locs : locMoves) {
						boardGUI[locs.getXYLocation().getXCoOrdinate()][locs.getXYLocation().getYCoOrdinate()].markMoves = false;
						if (locs.getXYLocation().equals(((RadikalChessButton) e.getComponent()).loc)) {
							currentState = game.getResult(currentState, locs);
							game.addStateToReview(currentState);
							currentState.analyzeUtility();
							repaintBoardGUI(currentState);
						}
					}
					doAction *= -1;
					updateStatus();
				}
			}
		}
	}
	
	protected class RadikalChessButton extends JButton {
		
		private static final long serialVersionUID = 1L;
		private RadikalChessPiece piece;
		private boolean markMoves = false;
		private Color backgroundColor;
		private XYLocation loc;

		public RadikalChessButton (int x, int y) {
			super();
			loc = new XYLocation(x, y);
		}
		
		public RadikalChessButton (RadikalChessPiece piece, int x, int y) {
			this(x, y);
			this.piece = piece;
		}
		
		public void paint (Graphics g) {
			if (loc.getXCoOrdinate()%2 == 0) {
				if (loc.getYCoOrdinate()%2 == 0) {
					paintBlackPositions();
				} else {
					paintWhitePieces();
				}
			} else {
				if (loc.getYCoOrdinate()%2 == 0) {
					paintWhitePieces();
				} else {
					paintBlackPositions();
				}
			}
			super.paint((Graphics2D) g);
		}
		
		private void paintBlackPositions () {
			if (markMoves) 
				this.setBackground(backgroundColor);
			else {
				this.setBackground(Black);
			}
			if (this.piece != null) 
				selectIconPiece(piece.toString());
			else
				this.setIcon(null);
		}
		
		private void paintWhitePieces () {
			if (markMoves) 
				this.setBackground(backgroundColor);
			else {
				this.setBackground(White);
			}
			if (this.piece != null) 
				selectIconPiece(piece.toString());
			else
				this.setIcon(null);
		}
		
		private void selectIconPiece (String piece) {
			ImageIcon icon = new ImageIcon(getClass().getResource(piece.toString()+".png"));
			this.setIcon(new ImageIcon(icon.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH)));
		}
	}
}	
