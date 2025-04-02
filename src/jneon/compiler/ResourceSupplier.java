package jneon.compiler;

public interface ResourceSupplier<T extends AutoCloseable, E extends Throwable> {

	T create() throws E;

}
