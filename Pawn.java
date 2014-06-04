package aima.core.environment.radikalchess.pieces;

import aima.core.environment.radikalchess.RadikalChessAction;
import java.util.ArrayList;

import aima.core.environment.radikalchess.RadikalChessPiece;
import aima.core.environment.radikalchess.RadikalChessState;
import aima.core.util.datastructure.XYLocation;

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
		if (!isDangerMove(this, state)) {
			for (XYLocation location : pawnMovements(this, state)) {
				RadikalChessState newState = state.clone();
				newState.movePiece(new RadikalChessAction(this.loc, this), this.loc , location, false);
				if (!isDangerMove(this, newState)) {
					validMoves.add(location);
					if (state.isEnemyHere(this, location)) {
						if (state.getValue(location.getXCoOrdinate(), location.getYCoOrdinate()).toString().contains("K"+(this.color == 1?"B":"N"))) {
							validMoves.clear();
							validMoves.add(location);
							return validMoves;
						}
					} else {
						if (!state.isEnemyHere(this, location)) {
							if (pawnEatMovements(this, newState).contains(KingXYLocation))
								validMoves.add(location);
						}
					}
				}
			}
		} else {
			for (XYLocation location : pawnNonEatMovements(this, state)) {
				RadikalChessState newState = state.clone();
				newState.movePiece(new RadikalChessAction(this.loc, this), this.loc , location, false);
				if (!isDangerMove(this, newState)) {
					validMoves.add(location);
					return validMoves;
				}
			}
		}
		return validMoves;
	}
}
