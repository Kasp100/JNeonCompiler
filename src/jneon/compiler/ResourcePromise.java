package jneon.compiler;

public interface ResourcePromise<T extends AutoCloseable, E extends Throwable> {

	T create() throws E;

}
