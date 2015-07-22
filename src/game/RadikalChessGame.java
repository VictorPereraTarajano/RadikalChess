package game;

import java.util.ArrayList;
import java.util.List;


public class RadikalChessGame implements Game<RadikalChessState, RadikalChessAction, String> {
	
	private RadikalChessState initialState;
	private List<RadikalChessState> gameReview = new ArrayList<>();
	
	public RadikalChessGame () {
		initialState = new RadikalChessState();
		gameReview.add(initialState);
	}

	public RadikalChessGame (String initialBoard [] []) {
		initialState = new RadikalChessState(initialBoard);
		gameReview.add(initialState);
	}
	
	@Override
	public RadikalChessState getInitialState() {
		return initialState;
	}

	@Override
	public String[] getPlayers() {
		return new String [] {"Blacks", "Whites"};
	}

	@Override
	public String getPlayer(RadikalChessState state) {
		return state.getPlayerToMove();
	}
	
	@Override
	public boolean isTerminal(RadikalChessState state) {
		return (state.getUtility() != -1)?true:false;
	}
	
	public void addStateToReview (RadikalChessState state) {
		gameReview.add(state);
	}
	
	public List<RadikalChessState> getGameReview () {
		return gameReview;
	}
	
    @Override
	public RadikalChessState getResult (RadikalChessState state, RadikalChessAction action) {
		RadikalChessState nextState = state.clone();
		if (nextState.movePiece(action, action.getPiece().getXYLocation(), action.getXYLocation(), true)) {
			nextState.setPlayerToMove((nextState.getPlayerToMove().equals("N"))?"B":"N");
			nextState.analyzeUtility();
		}
		return nextState;
	}
	
	@Override
	public List<RadikalChessAction> getActions (RadikalChessState state) {
		return state.getPlayerMoves(state.getPlayerToMove());
	}

	@Override
	public double getUtility (RadikalChessState state, String player) {
		double result = state.getUtility();
		if (result == 0 || result == 1) {
			if (player.equals("B"))
				result = 1 - result;
		}
		return result;
	}
}
