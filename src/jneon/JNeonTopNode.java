package jneon;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jneon.exceptions.CompileTimeException;
import jneon.subnodes.PkgNode;

public final class JNeonTopNode implements Serializable {

	private static final long serialVersionUID = 5292436791648563123L;

	private final Set<PkgNode> pkgs;

	private JNeonTopNode(Set<PkgNode> pkgs) {
		this.pkgs = pkgs;
	}

	public Set<PkgNode> getPkgs() {
		return Collections.unmodifiableSet(pkgs);
	}

	public static class Builder {
		private final Set<PkgNode> pkgs = new HashSet<>();

		public void addPackage(PkgNode p) throws CompileTimeException {
			if(!pkgs.add(p)) {
				throw new CompileTimeException("Duplicate root package");
			}
		}

		public JNeonTopNode build() {
			return new JNeonTopNode(Collections.unmodifiableSet(new HashSet<>(pkgs)));
		}
	}

}
