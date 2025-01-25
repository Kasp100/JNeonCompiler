package jneon.compiler;

import jneon.exceptions.CompileTimeException;

public interface CompileTimeConsumer<T> {

	void accept(T t) throws CompileTimeException;

}
