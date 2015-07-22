package pieces;

import game.RadikalChessAction;
import game.RadikalChessPiece;
import game.RadikalChessState;
import utilities.XYLocation;

import java.util.ArrayList;

public class Pawn extends RadikalChessPiece {
	
	public Pawn (int color, XYLocation loc) {
		super(color, loc);
	}

	@Override
	public String toString() {
		return (this.color == 1)?"PN":"PB";
	}
	
	@Override
	public double getPathCost() {
		return 1;
	}

	@Override
	public ArrayList<XYLocation> getValidMoves(RadikalChessState state) {
		XYLocation KingXYLocation = state.getPieceXYLocation("K"+(this.color == 1?"B":"N"));
		ArrayList<XYLocation> validMoves = new ArrayList<>();
		if (!RadikalChessPiece.isDangerMove(this, state)) {
			for (XYLocation location : RadikalChessPiece.pawnMovements(this, state)) {
				RadikalChessState newState = state.clone();
				newState.movePiece(new RadikalChessAction(this.loc, this), this.loc , location, false);
				if (!RadikalChessPiece.isDangerMove(this, newState)) {
					validMoves.add(location);
					if (state.isEnemyHere(this, location)) {
						if (state.getValue(location.getXCoOrdinate(), location.getYCoOrdinate()).toString().contains("K" + (this.color == 1 ? "B" : "N"))) {
							validMoves.clear();
							validMoves.add(location);
							return validMoves;
						}
					} else {
						if (!state.isEnemyHere(this, location)) {
							if (RadikalChessPiece.pawnEatMovements(this, newState).contains(KingXYLocation))
								validMoves.add(location);
						}
					}
				}
			}
		} else {
			for (XYLocation location : RadikalChessPiece.pawnNonEatMovements(this, state)) {
				RadikalChessState newState = state.clone();
				newState.movePiece(new RadikalChessAction(this.loc, this), this.loc , location, false);
				if (!RadikalChessPiece.isDangerMove(this, newState)) {
					validMoves.add(location);
					return validMoves;
				}
			}
		}
		return validMoves;
	}
}
