
import Metrics;

public interface AdversarialSearch<STATE, ACTION> {

	ACTION makeDecision(STATE state);

	Metrics getMetrics();
}
