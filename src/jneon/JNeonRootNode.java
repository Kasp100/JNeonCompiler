package jneon;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jneon.compiler.PkgSign;
import jneon.exceptions.CompileTimeException;
import jneon.subnodes.TopLevelNode;

public final class JNeonRootNode implements Serializable {

	private static final long serialVersionUID = 5292436791648563123L;

	private final Map<PkgSign, PkgNode> pkgs;

	private JNeonRootNode(Map<PkgSign, PkgNode> pkgs) {
		this.pkgs = pkgs;
	}

	public Map<PkgSign, PkgNode> getPkgs() {
		return pkgs;
	}

	public static class Builder {

		private final Map<PkgSign, PkgNode.Builder> pkgs = new HashMap<>();

		public void add(PkgSign sign, TopLevelNode node) throws CompileTimeException {

			if(pkgs.containsKey(sign)) {
				pkgs.get(sign).add(node);
			}else {
				final PkgNode.Builder b = new PkgNode.Builder();
				b.add(node);
				pkgs.put(sign, b);
			}

		}

		public JNeonRootNode build() {
			final Map<PkgSign, PkgNode> createdPkgs = HashMap.newHashMap(pkgs.size());

			pkgs.forEach((pkgSign, pkgNodeBuilder) -> {
				createdPkgs.put(pkgSign, pkgNodeBuilder.build());
			});

			return new JNeonRootNode(Collections.unmodifiableMap(createdPkgs));
		}

	}

}
