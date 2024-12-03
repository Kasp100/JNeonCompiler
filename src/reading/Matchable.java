package reading;

public interface Matchable<PatternType> {

	boolean matches(PatternType pattern);

}
