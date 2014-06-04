
import java.util.ArrayList;
import YLocation;

public abstract class RadikalChessPiece implements Cloneable {
	
	protected int color;
	protected XYLocation loc;
	protected boolean isOnJaque = false;

	public RadikalChessPiece (int color, XYLocation loc) {
		this.color = color;
		this.loc = loc;
	}
	
	public abstract double getPathCost();
	
    @Override
	public abstract String toString ();

	public abstract ArrayList<XYLocation> getValidMoves(RadikalChessState state);
	
	public int getColor() {
		return this.color;
	}
	
	public XYLocation getXYLocation () {
		return this.loc;
	}

    @Override
	public RadikalChessPiece clone () {
		RadikalChessPiece copy = null;
		try {
			copy = (RadikalChessPiece) super.clone();
			copy.isOnJaque = isOnJaque;
		} catch (CloneNotSupportedException e) {
			System.out.println("Error : RadikalChessPiece clone");
		}
		return copy;
	}
	
	public boolean canMove (RadikalChessState state, XYLocation locFinal) {
		if (state.isEmpty(locFinal.getXCoOrdinate(), locFinal.getYCoOrdinate()) || state.getValue(locFinal.getXCoOrdinate(), locFinal.getYCoOrdinate()).getColor() != this.color ) {
			if (getValidMoves(state).contains(locFinal)) {
				return true;
			}
		}
		return false;
	}
	
	protected static boolean isDangerMove (RadikalChessPiece piece, RadikalChessState state) {
		XYLocation KingXYLocation = state.getPieceXYLocation("K"+((piece.color==1)?"N":"B"));
		for (int i = 0; i < state.getRows(); i++) {
			for (int j = 0; j < state.getColumns(); j++) {
				RadikalChessPiece enemyPiece = state.getValue(i,j);
				if (enemyPiece != null && enemyPiece.color != piece.color) {
					switch (enemyPiece.toString().charAt(0)) {
						case 'P':
								if (pawnEatMovements(enemyPiece, state).contains(KingXYLocation)) return true;
								break;
						case 'Q':
								if (queenMovements(enemyPiece, state).contains(KingXYLocation)) return true;
								break;
						case 'K':
								if (kingMovements(enemyPiece, state).contains(KingXYLocation)) return true;
								break;
						case 'A':
								if (bishopMovements(enemyPiece, state).contains(KingXYLocation)) return true;
								break;
						default :
								if (castleMovements(enemyPiece, state).contains(KingXYLocation)) return true;
								
					}
				}
			}
		}
		return false;
	}
	
	protected static ArrayList<XYLocation> bishopMovements (RadikalChessPiece piece, RadikalChessState state) {
		
		ArrayList<XYLocation> moves = new ArrayList<>();
		int rows = state.getRows(), columns = state.getColumns(), pieceX = piece.loc.getYCoOrdinate(), pieceY = piece.loc.getXCoOrdinate();
		
		for (int i = pieceY + 1 , j = pieceX + 1; i < rows && j < columns ;i++,j++) {
			if (state.isEmpty(i, j)) {
				moves.add(new XYLocation (i, j));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (i, j))) {
					moves.add(new XYLocation (i, j));
					break;
				} else {
					break;
				}
			}
		}
		for (int i = pieceY - 1, j =  pieceX - 1; i >= 0 && j >= 0 ;i--, j--) {
			if (state.isEmpty(i, j)) {
				moves.add(new XYLocation (i, j));
			} else {
				if (state.isEnemyHere( piece, new XYLocation (i, j))) {
					moves.add(new XYLocation (i, j));
					break;
				} else {
					break;
				}
			}
		}
		for (int i =  pieceY + 1 , j =  pieceX - 1; i < rows && j >= 0;i++,j--) {
			if (state.isEmpty(i, j)) {
				moves.add(new XYLocation (i, j));
			} else {
				if (state.isEnemyHere( piece, new XYLocation (i, j))) {
					moves.add(new XYLocation (i, j));
					break;
				} else {
					break;
				}
			}
		}
		for (int i =  pieceY - 1, j = pieceX + 1; i >= 0 && j < columns;i--,j++) {
			if (state.isEmpty(i, j)) {
				moves.add(new XYLocation (i, j));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (i, j))) {
					moves.add(new XYLocation (i, j));
					break;
				} else {
					break;
				}
			}
		}
		return moves;
	}
	
	protected static ArrayList<XYLocation> castleMovements (RadikalChessPiece piece, RadikalChessState state) {
		
		ArrayList<XYLocation> moves = new ArrayList<>();
		int rows = state.getRows(), columns = state.getColumns(), pieceY = piece.loc.getXCoOrdinate(), pieceX = piece.loc.getYCoOrdinate();
		
		for (int i = pieceY + 1; i < rows ;i++) {
			if (state.isEmpty(i, pieceX)) {
				moves.add(new XYLocation (i, pieceX));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (i, pieceX))) {
					moves.add(new XYLocation (i, pieceX));
					break;
				} else {
					break;
				}
			}
		}
		for (int i = pieceY - 1; i >= 0 ; i--) {
			if (state.isEmpty(i, pieceX)) {
				moves.add(new XYLocation (i, pieceX));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (i, pieceX))) {
					moves.add(new XYLocation(i, pieceX));
					break;
				} else {
					break;
				}
			}
		}
		for (int i = pieceX + 1; i < columns ;i++) {
			if (state.isEmpty(pieceY, i)) {
				moves.add(new XYLocation (pieceY, i));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (pieceY, i))) {
					moves.add(new XYLocation(pieceY, i));
					break;
				} else {
					break;
				}
			}
		}
		for (int i = pieceX - 1; i >= 0 ;i--) {
			if (state.isEmpty(pieceY, i)) {
				moves.add(new XYLocation (pieceY, i));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (pieceY, i))) {
					moves.add(new XYLocation(pieceY, i));
					break;
				} else {
					break;
				}
			}
		}
		return moves;
	}
	
	protected static ArrayList<XYLocation> pawnMovements (RadikalChessPiece piece, RadikalChessState state) {
		
		ArrayList<XYLocation> moves = new ArrayList<>();
		moves.addAll(pawnNonEatMovements(piece, state));
		moves.addAll(pawnEatMovements(piece, state));
		return moves;
	}
	
	protected static ArrayList<XYLocation> pawnEatMovements (RadikalChessPiece piece, RadikalChessState state) {
		
		ArrayList<XYLocation> moves = new ArrayList<>();
		int rows = state.getRows(), columns = state.getColumns(), pieceY = piece.loc.getXCoOrdinate(), pieceX = piece.loc.getYCoOrdinate();
		
		if (piece.color == 1) {
			if (pieceY + 1 < rows && pieceX- 1 >= 0 && state.isEnemyHere(piece, new XYLocation(pieceY + 1, pieceX - 1))) {
				moves.add(new XYLocation(pieceY + 1, pieceX - 1));
			}
			if (pieceY + 1 < rows && pieceX  + 1 < columns && state.isEnemyHere(piece, new XYLocation(pieceY + 1, pieceX  + 1))) {
				moves.add(new XYLocation(pieceY + 1, pieceX  + 1));
			}
		} else {
			if (pieceY - 1 >= 0 && pieceX  + 1 < columns && state.isEnemyHere(piece, new XYLocation(pieceY - 1, pieceX  + 1))) {
				moves.add(new XYLocation(pieceY - 1, pieceX  + 1));
			}
			if (pieceY - 1 >= 0 && pieceX - 1 >= 0 && state.isEnemyHere(piece, new XYLocation(pieceY- 1, pieceX  - 1))) {
				moves.add(new XYLocation(pieceY - 1, pieceX - 1));
			}
		}
		return moves;
	}
	
	protected static ArrayList<XYLocation> pawnNonEatMovements (RadikalChessPiece piece, RadikalChessState state) {
		
		ArrayList<XYLocation> moves = new ArrayList<>();
		int rows = state.getRows(), pieceY = piece.loc.getXCoOrdinate(), pieceX = piece.loc.getYCoOrdinate();		
		if (piece.color == 1) {
			if (pieceY + 1 < rows && state.isEmpty(pieceY + 1, pieceX)) {
				moves.add(new XYLocation(pieceY + 1, pieceX));
			}
		} else {
			if (pieceY - 1 >= 0 && state.isEmpty(pieceY - 1, pieceX )) {
				moves.add(new XYLocation(pieceY - 1, pieceX ));
			}
		}
		return moves;
	}
	
	protected static ArrayList<XYLocation> queenMovements (RadikalChessPiece piece, RadikalChessState state) {
		ArrayList<XYLocation> moves = castleMovements(piece, state);
		moves.addAll(bishopMovements(piece, state));
		return moves;
	}
	
	protected static ArrayList<XYLocation> kingMovements (RadikalChessPiece piece, RadikalChessState state) {
		
		ArrayList<XYLocation> moves = new ArrayList<>();
		int rows = state.getRows(), columns = state.getColumns(), pieceY = piece.loc.getXCoOrdinate(), pieceX = piece.loc.getYCoOrdinate();
		
		if (pieceY - 1 >= 0) {
			if (state.isEmpty(pieceY - 1, pieceX)) {
				moves.add(new XYLocation(pieceY - 1, pieceX));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (pieceY - 1, pieceX))) {
					moves.add(new XYLocation(pieceY - 1, pieceX));
				}
			}
		}
		
		if (pieceY + 1 < rows) {
			if (state.isEmpty(pieceY + 1, pieceX)) {
				moves.add(new XYLocation(pieceY + 1, pieceX));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (pieceY + 1, pieceX))) {
					moves.add(new XYLocation(pieceY + 1, pieceX));
				}
			}
		}
		
		if (pieceX- 1 >= 0) {
			if (state.isEmpty(pieceY, pieceX - 1)) {
				moves.add(new XYLocation(pieceY, pieceX- 1));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (pieceY, pieceX- 1))) {
					moves.add(new XYLocation(pieceY, pieceX- 1));
				}
			}
		}
		
		if (pieceX+ 1 < columns) {
			if (state.isEmpty(pieceY, pieceX + 1)) {
				moves.add(new XYLocation(pieceY, pieceX + 1));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (pieceY, pieceX + 1))) {
					moves.add(new XYLocation(pieceY, pieceX + 1));
				}
			}
		}
		
		if (pieceY- 1 >= 0 && pieceX - 1 >= 0) {
			if (state.isEmpty(pieceY - 1, pieceX - 1)) {
				moves.add(new XYLocation(pieceY- 1, pieceX - 1));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (pieceY - 1, pieceX - 1))) {
					moves.add(new XYLocation(pieceY - 1, pieceX - 1));
				}
			}
		}
		
		if (pieceY - 1 >= 0 && pieceX + 1 < columns) {
			if (state.isEmpty(pieceY - 1, pieceX + 1)) {
				moves.add(new XYLocation(pieceY - 1, pieceX + 1));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (pieceY - 1, pieceX + 1))) {
					moves.add(new XYLocation(pieceY - 1, pieceX + 1));
				}
			}
		}
		
		if (pieceY + 1 < rows && pieceX - 1 >= 0) {
			if (state.isEmpty(pieceY + 1 , pieceX - 1)) {
				moves.add(new XYLocation(pieceY + 1, pieceX - 1));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (pieceY + 1 , pieceX - 1))) {
					moves.add(new XYLocation(pieceY + 1 , pieceX- 1));
				}
			}
		}
		
		if (pieceY + 1 < rows && pieceX + 1 < columns) {
			if (state.isEmpty(pieceY + 1 , pieceX+ 1)) {
				moves.add(new XYLocation(pieceY + 1 , pieceX + 1));
			} else {
				if (state.isEnemyHere(piece, new XYLocation (pieceY + 1 , pieceX + 1))) {
					moves.add(new XYLocation(pieceY + 1 , pieceX + 1));
				}
			}
		}
		return moves;
	}
}
