package jneon;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jneon.exceptions.CompileTimeException;
import jneon.subnodes.PkgNode;

public class JNeonTopNode implements Serializable {

	private static final long serialVersionUID = 5292436791648563123L;

	private final Set<PkgNode> pkgs = new HashSet<>();

	public void addPackage(PkgNode p) {
		if(!pkgs.add(p)) {
			throw new CompileTimeException("Duplicate root package");
		}
	}

}
