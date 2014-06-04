package aima.core.environment.radikalchess.heuristics;

import aima.core.util.datastructure.XYLocation;

public class EuclideDistance  {

	public static int distanceTo (XYLocation locInit, XYLocation locFinal) {
		return (int) Math.sqrt(Math.pow((locFinal.getXCoOrdinate() - locInit.getXCoOrdinate()),2) + (Math.pow((locFinal.getYCoOrdinate() - locInit.getYCoOrdinate()), 2)));
	}
}
