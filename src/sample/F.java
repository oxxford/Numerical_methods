package sample;

/**
 * simple interface for delegate
 * @param <One> input type
 * @param <Two> input type
 * @param <Three> output type
 */
@FunctionalInterface
public interface F <One, Two, Three> {
    Three apply(One d1, Two d2);
}
