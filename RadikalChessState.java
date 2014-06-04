package aima.core.environment.radikalchess;

import java.util.ArrayList;
import aima.core.environment.radikalchess.pieces.Bishop;
import aima.core.environment.radikalchess.pieces.Castle;
import aima.core.environment.radikalchess.pieces.King;
import aima.core.environment.radikalchess.pieces.Pawn;
import aima.core.environment.radikalchess.pieces.Queen;
import aima.core.util.datastructure.XYLocation;

public class RadikalChessState implements Cloneable {
	
	public static final String N = "N";
	public static final String B = "B";
	private RadikalChessPiece [] [] board;
	private int rows = 6, columns = 4;
	private String playerToMove = N;
	private double utility = -1;
	
	public RadikalChessState () {
		board = new RadikalChessPiece [rows][columns];
		initBoard(new String [] [] {{"KN","QN","AN","TN"},
									 {"PN","PN","PN","PN"},
									 {"  ","  ","  ","  "},
									 {"  ","  ","  ","  "},
									 {"PB","PB","PB","PB"},
									 {"TB","AB","QB","KB"}});
	}
	
	public RadikalChessState (String initialBoard [] []) {
		this.rows = initialBoard.length;
		this.columns = initialBoard[0].length;
		this.board = new RadikalChessPiece [rows][columns];
		initBoard(initialBoard);
		analyzeUtility();
	}

	public String getPlayerToMove () {
		return this.playerToMove;
	}
	
	public int getRows () {
		return this.rows;
	}
	
	public int getColumns () {
		return this.columns;
	}
	
	public void setPlayerToMove (String playerToMove) {
		this.playerToMove = playerToMove;
	}
	
	public boolean isEmpty (int row, int column) {
		return (board[row][column] == null);
	}
	
	public boolean isMate (RadikalChessPiece piece, XYLocation loc) {
		if (board[loc.getXCoOrdinate()][loc.getYCoOrdinate()] != null) 
			return board[loc.getXCoOrdinate()][loc.getYCoOrdinate()].toString().equals("K"+(piece.getColor()==1?"B":"N"));
		return false;
	}
	
	public boolean isEnemyHere (RadikalChessPiece piece, XYLocation loc) {
		return !isEmpty(loc.getXCoOrdinate(), loc.getYCoOrdinate()) && getValue(loc.getXCoOrdinate(), loc.getYCoOrdinate()).getColor() != piece.getColor();
	}
	
	public RadikalChessPiece getValue (int row, int column) {
		return (board[row][column]);
	}
	
	public XYLocation getPieceXYLocation (String pieceString) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (board[i][j] != null && board[i][j].toString().equals(pieceString)) 
					return new XYLocation(i, j);
			}
		}
		return null;
	}
	
	public double getUtility () {
		return utility;
	}
	
	public void analyzeUtility () {
		if (getPieceXYLocation("KN") == null) 
			utility = 0;
		else {
			if (getPieceXYLocation("KB") == null) 
				utility = 1;
			else {
				if (getPlayerMoves(playerToMove).isEmpty()) 
					utility = (playerToMove=="N")?0:1;
			}
		}
	}
	
	public ArrayList<RadikalChessAction> getPieceMoves (RadikalChessPiece piece) {
		ArrayList<RadikalChessAction> listAction = new ArrayList<>();
		for (XYLocation moves : piece.getValidMoves(this)) {
			if (piece.toString().equals("PN") && piece.getXYLocation().getXCoOrdinate() == rows - 2) 
				listAction.add(new RadikalChessAction(moves, new Queen(1, piece.getXYLocation()), true));
			else {
				if (piece.toString().equals("PB") && piece.getXYLocation().getXCoOrdinate() == 1) 
					listAction.add(new RadikalChessAction(moves, new Queen(0, piece.getXYLocation()), true));
				else {
					listAction.add(new RadikalChessAction (moves, piece));
				}
			}
		}
		return listAction;
	}
	
	public ArrayList<RadikalChessAction> getPlayerMoves (String player) {
		ArrayList<RadikalChessAction> listAction = new ArrayList<>();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (board[i][j] != null && board[i][j].toString().contains(player) ) {
					for (RadikalChessAction action : getPieceMoves(board[i][j])) {
						XYLocation loc = action.getXYLocation();
						if (getValue(loc.getXCoOrdinate(), loc.getYCoOrdinate()) != null && getValue(loc.getXCoOrdinate(), loc.getYCoOrdinate()).toString().equals("K"+(player=="N"?"B":"N"))) {
							listAction.clear();
							listAction.add(action);
							return listAction;
						} else
							listAction.add(action);
					}
				}
			}
		}
		return listAction;
	}
	
	public boolean movePiece (RadikalChessAction action, XYLocation locInitial, XYLocation locFinal, boolean userMove) {
		if (action.isCoronationMove()) {
			this.board[locFinal.getXCoOrdinate()][locFinal.getYCoOrdinate()] = action.getPiece().clone();
			this.board[locFinal.getXCoOrdinate()][locFinal.getYCoOrdinate()].loc = locFinal;
			this.board[locInitial.getXCoOrdinate()][locInitial.getYCoOrdinate()] = null;
			return true;
		} else {
			if (board[locInitial.getXCoOrdinate()][locInitial.getYCoOrdinate()].toString().contains(playerToMove)) {
	            if (userMove) {
	            	if (board[locInitial.getXCoOrdinate()][locInitial.getYCoOrdinate()].canMove(this, locFinal)) {
						this.board[locFinal.getXCoOrdinate()][locFinal.getYCoOrdinate()] = this.board[locInitial.getXCoOrdinate()][locInitial.getYCoOrdinate()];
						this.board[locFinal.getXCoOrdinate()][locFinal.getYCoOrdinate()].loc = locFinal;
						this.board[locInitial.getXCoOrdinate()][locInitial.getYCoOrdinate()] = null;
						return true;
	            	}
	            } else {
					this.board[locFinal.getXCoOrdinate()][locFinal.getYCoOrdinate()] = this.board[locInitial.getXCoOrdinate()][locInitial.getYCoOrdinate()];
					this.board[locFinal.getXCoOrdinate()][locFinal.getYCoOrdinate()].loc = locFinal;
					this.board[locInitial.getXCoOrdinate()][locInitial.getYCoOrdinate()] = null;
					return true;
	            }
			}
		}
		return false;
	}
	
        @Override
	public RadikalChessState clone () {
		RadikalChessState copy = null;
		try {
			copy = (RadikalChessState) super.clone();
			copy.board = cloneBoard(board);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return copy;
	}
	
        @Override
	public String toString() {
		String boardString = "";
		for (int i = 0; i < rows; i++) {
			boardString += rows - i - 1+" | ";
			for (int j = 0; j < columns; j++) {
				if (board[i][j] == null) {
					boardString += "  ";
				} else {
					boardString += board[i][j];
				}
				if (j == columns - 1) {
					boardString += "\n";
				} else {
					boardString += " ";
				}
			}
		}
		boardString += "    __ __ __ __\n    0  1  2  3 ";
		return boardString;
	}

 	private void initBoard (String initialBoard [][]) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				if (initialBoard[i][j] != null) {
					switch (initialBoard[i][j]) {
						case "PN" :
								this.board[i][j] = new Pawn(1, new XYLocation(i, j));
								break;
						case "QN" :
								this.board[i][j] = new Queen (1, new XYLocation(i, j));
								break;
						case "KN" :
								this.board[i][j] = new King(1, new XYLocation(i, j));
								break;
						case "AN" :
								this.board[i][j] = new Bishop(1, new XYLocation(i, j));
								break;
						case "TN" :
								this.board[i][j] = new Castle(1, new XYLocation(i, j));
								break;
						case "PB" :
								this.board[i][j] = new Pawn(0, new XYLocation(i, j));
								break;
						case "TB" :
								this.board[i][j] = new Castle (0, new XYLocation(i, j));
								break;
						case "AB" :
								this.board[i][j] = new Bishop (0, new XYLocation(i, j));
								break;
						case "KB" :
								this.board[i][j] = new King (0, new XYLocation(i, j));
								break;
						case "QB" :
								this.board[i][j] = new Queen(0, new XYLocation(i, j));
					}
				}
			}
		}
	}

	private RadikalChessPiece[][] cloneBoard (RadikalChessPiece [][] array) {
            int cloneRows = array.length;
            int cloneColumns = array[0].length;
	    RadikalChessPiece [] [] copyBoard = new RadikalChessPiece [rows][columns];
	    for (int i = 0; i < cloneRows; i++) {
	    	for (int j = 0; j < cloneColumns; j++) {
	    		if (board[i][j] != null) {
		    		copyBoard[i][j] =  board[i][j].clone();
	    		}
	    	}
	    }
	    return copyBoard;
	}
}
