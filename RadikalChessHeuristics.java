
import java.util.ArrayList;

import RadikalChessPiece;
import RadikalChessState;
import HeuristicFunction;
import XYLocation;

public class RadikalChessHeuristics implements HeuristicFunction {
	
	private int h1_N = 0, h1_B = 0, h2_B = 0, h2_N = 0, h3_B = 0, h3_N = 0;
	private double h4_B, h4_N, h5_N, h5_B;
	private boolean h1Enable = false, 
					h2Enable = false, 
					h3Enable = false, 
					h4Enable = false,
					h5Enable = false;

	public RadikalChessHeuristics (boolean h1Enable, 
										boolean h2Enable, 
											boolean h3Enable, 
												boolean h4Enable, 
													boolean h5Enable) {
		this.h1Enable = h1Enable;
		this.h2Enable = h2Enable;
		this.h3Enable = h3Enable;
		this.h4Enable = h4Enable;
		this.h5Enable = h5Enable;
	}
	
	@Override
	public double h(Object State) {
		evaluateState((RadikalChessState) State);
		double eval = (h4_N - h4_B) + (h1_N - h1_B) + (h2_N - h2_B) + (h3_N - h3_B)+(h5_B - h5_N);
		if (eval > 0) 
			 return 0.5 + eval*0.00000000025;
		else {
			if (eval < 0) 
				return Math.abs(eval*0.00000000025);
			return 0.5;
		}
	}
	
	private void evaluateState (RadikalChessState state) {
		
		XYLocation KingNXYLocation = state.getPieceXYLocation("KN");
		XYLocation KingBXYLocation = state.getPieceXYLocation("KB");

		if (h3Enable) {
			h3_B = state.getPlayerMoves("B").size();
			h3_N = state.getPlayerMoves("N").size();
		}
			
		int rows = state.getRows();
		int columns = state.getColumns();
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns ; j++) {
				if (state.getValue(i, j) != null) {
					RadikalChessPiece piece = state.getValue(i,j);
					ArrayList<XYLocation> movesPiece = piece.getValidMoves(state);
					if (state.getValue(i, j).getColor() == 1) {
						if (h1Enable && movesPiece.contains(piece.getColor()==1?KingBXYLocation:KingNXYLocation)) 
							h1_N++;
						if (h2Enable) {
							for (XYLocation locs : movesPiece) {
								if (state.getValue(locs.getXCoOrdinate(),locs.getYCoOrdinate()) != null) 
									h2_N += state.getValue(locs.getXCoOrdinate(), locs.getYCoOrdinate()).getPathCost();
							}
						}
						if (h4Enable) {
							if (piece.toString().equals("PN")) 
								h4_N += piece.getPathCost() * (i);
							else
								h4_N += piece.getPathCost();
						}
						if (h5Enable) 
							h5_N += EuclideDistance.distanceTo(piece.getXYLocation(), KingBXYLocation);
					} else {
						if (h1Enable && movesPiece.contains(piece.getColor()==1?KingBXYLocation:KingNXYLocation))
							h1_B++;
						if (h2Enable) {
							for (XYLocation locs : movesPiece) {
								if (state.getValue(locs.getXCoOrdinate(),locs.getYCoOrdinate()) != null) 
									h2_B += state.getValue(locs.getXCoOrdinate(), locs.getYCoOrdinate()).getPathCost();
							}
						}
						if (h4Enable) {
							if (piece.toString().equals("PB")) 
								h4_B += piece.getPathCost() * (rows - i - 1);
							else
								h4_B += piece.getPathCost();
						}
						if (h5Enable) 
							h5_B += EuclideDistance.distanceTo(piece.getXYLocation(), KingNXYLocation);
					}
				}
			}
		}
	}
}
