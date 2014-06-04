
import XYLocation;

public class RadikalChessAction  {

	private RadikalChessPiece piece;
	private XYLocation locFinal;
	private boolean coronation = false;
	
	public RadikalChessAction (XYLocation loc, RadikalChessPiece piece) {
		this.locFinal = loc;
		this.piece = piece;
	}
	
	public RadikalChessAction (XYLocation locFinal, RadikalChessPiece pieceToChange, boolean coronation) {
		this(locFinal, pieceToChange);
		this.coronation = coronation;
	}
	
	public XYLocation getXYLocation () {
		return this.locFinal;
	}
	
	public boolean isCoronationMove () {
		return this.coronation;
	}
	
	public RadikalChessPiece getPiece () {
		return this.piece;
	}
}
