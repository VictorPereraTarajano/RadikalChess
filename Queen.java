package aima.core.environment.radikalchess.pieces;

import aima.core.environment.radikalchess.RadikalChessAction;
import java.util.ArrayList;

import aima.core.environment.radikalchess.RadikalChessPiece;
import aima.core.environment.radikalchess.RadikalChessState;
import aima.core.environment.radikalchess.heuristics.EuclideDistance;
import aima.core.util.datastructure.XYLocation;

public class Queen extends RadikalChessPiece {
	
	public Queen (int color, XYLocation loc) {
		super(color, loc);
	}

	@Override
	public String toString() {
		return (color == 1)?"QN":"QB";
	}
	
	@Override
	public double getPathCost() {
		return 9;
	}

	@Override
	public ArrayList<XYLocation> getValidMoves(RadikalChessState state) {
		XYLocation KingXYLocation = state.getPieceXYLocation("K"+(this.color == 1?"B":"N"));
		ArrayList<XYLocation> validMoves = new ArrayList<>();
		int distInitial = EuclideDistance.distanceTo(this.loc, KingXYLocation);
		if (!isDangerMove(this, state)) {
			for (XYLocation location : queenMovements(this, state)) {
				RadikalChessState newState = state.clone();
				newState.movePiece(new RadikalChessAction(this.loc, this), this.loc , location, false);
				if (!isDangerMove(this, newState)) {
					if (state.isEnemyHere(this, location)) {
						if (state.getValue(location.getXCoOrdinate(), location.getYCoOrdinate()).toString().contains("K"+(this.color == 1?"B":"N"))) {
							validMoves.clear();
							validMoves.add(location);
							return validMoves;
						} else 
							validMoves.add(location);
					} else {
						if (distInitial > EuclideDistance.distanceTo(location, KingXYLocation)) 
							validMoves.add(location);
						else {
							if (queenMovements(this, newState).contains(KingXYLocation))
								validMoves.add(location);
						}
					}
				}
			}
		} else {
			for (XYLocation location : queenMovements(this, state)) {
				RadikalChessState newState = state.clone();
				newState.movePiece(new RadikalChessAction(this.loc, this), this.loc , location, false);
				if (!isDangerMove(this, newState)) {
					validMoves.add(location);
				}
			}
			return validMoves;
		}
		return validMoves;
	}
}
