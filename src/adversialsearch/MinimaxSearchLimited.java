package adversialsearch;

import game.Game;
import utilities.HeuristicFunction;
import utilities.Metrics;

public class MinimaxSearchLimited<STATE, ACTION, PLAYER> implements
		AdversarialSearch<STATE, ACTION> {

	private Game<STATE, ACTION, PLAYER> game;
	private int expandedNodes;
	private int currentDepth = 0;
	private int maxDepth;
	private HeuristicFunction heuristic;

	public static <STATE, ACTION, PLAYER> MinimaxSearchLimited<STATE, ACTION, PLAYER> createFor(
			Game<STATE, ACTION, PLAYER> game, int maxDepth) {
		return new MinimaxSearchLimited<STATE, ACTION, PLAYER>(game, maxDepth);
	}
	
	/** Creates a new search object for a given game. */
	public static <STATE, ACTION, PLAYER> MinimaxSearchLimited<STATE, ACTION, PLAYER> createFor(
			Game game, int maxDepth, HeuristicFunction heuristic) {
		return new MinimaxSearchLimited<STATE, ACTION, PLAYER>(game, maxDepth, heuristic);
	}

	public MinimaxSearchLimited(Game<STATE, ACTION, PLAYER> game, int maxDepth, HeuristicFunction heuristic) {
		this.game = game;
		this.maxDepth= 2*maxDepth;
		this.heuristic = heuristic;
	}

	public MinimaxSearchLimited(Game<STATE, ACTION, PLAYER> game, int maxDepth) {
		this.game = game;
		this.maxDepth= 2*maxDepth;
	}
	
	@Override
	public ACTION makeDecision(STATE state) {
		expandedNodes = 0;
		ACTION result = null;
		double resultValue = Double.NEGATIVE_INFINITY;
		PLAYER player = game.getPlayer(state);
		for (ACTION action : game.getActions(state)) {
			double value = minValue(game.getResult(state, action), player);
			if (value > resultValue) {
				result = action;
				resultValue = value;
			}
		}
		return result;
	}

	public double maxValue(STATE state, PLAYER player) { 						
		expandedNodes++;
		currentDepth++;
		if (game.isTerminal(state)) {
			currentDepth--;
			return game.getUtility(state, player);
		} else {
			if (currentDepth == maxDepth) {
				currentDepth--;
				return getEval(state, heuristic);
			}
		}
		double value = Double.NEGATIVE_INFINITY;
		for (ACTION action : game.getActions(state)) {
			value = Math.max(value,minValue(game.getResult(state, action), player));
		}
		currentDepth--;
		return value;
	}

	public double minValue(STATE state, PLAYER player) {
		expandedNodes++;
		currentDepth++;
		if (game.isTerminal(state)) {
			currentDepth--;
			return game.getUtility(state,player);
		} else {
			if (currentDepth == maxDepth) {
				currentDepth--;
				return getEval(state, heuristic);
			}
		}
		double value = Double.POSITIVE_INFINITY;
		for (ACTION action : game.getActions(state)) {
			value = Math.min(value,maxValue(game.getResult(state, action), player));
		}
		currentDepth--;
		return value;
	}

	@Override
	public Metrics getMetrics() {
		Metrics result = new Metrics();
		result.set("expandedNodes", expandedNodes);
		return result;
	}
	
	public double getEval (STATE state, HeuristicFunction heuristic) {
		return heuristic.h(state);	
	}
}
