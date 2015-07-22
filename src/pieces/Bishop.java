package pieces;

import game.RadikalChessAction;
import game.RadikalChessPiece;
import game.RadikalChessState;
import utilities.EuclideDistance;
import utilities.XYLocation;

import java.util.ArrayList;

public class Bishop  extends RadikalChessPiece {
	
	public Bishop  (int color, XYLocation loc) {
		super(color, loc);
	}

	@Override
	public String toString() {
		return (this.color == 1)?"AN":"AB";
	}
	
	@Override
	public double getPathCost() {
		return 5;
	}
	
	@Override
	public ArrayList<XYLocation> getValidMoves(RadikalChessState state) {
		XYLocation KingXYLocation = state.getPieceXYLocation("K"+(this.color == 1?"B":"N"));
		ArrayList<XYLocation> validMoves = new ArrayList<>();
		int distInitial = EuclideDistance.distanceTo(this.loc, KingXYLocation);
		if (!RadikalChessPiece.isDangerMove(this, state)) {
			for (XYLocation location : RadikalChessPiece.bishopMovements(this, state)) {
				RadikalChessState newState = state.clone();
				newState.movePiece(new RadikalChessAction(this.loc, this), this.loc , location, false);
				if (!RadikalChessPiece.isDangerMove(this, newState)) {
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
							if (RadikalChessPiece.bishopMovements(this, newState).contains(KingXYLocation))
								validMoves.add(location);
						}
					}
				}
			}
		} else {
			for (XYLocation location : RadikalChessPiece.bishopMovements(this, state)) {
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
