package jneon;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jneon.exceptions.CompileTimeException;
import jneon.subnodes.TopLevelNode;

public class PkgNode implements Serializable {

	private static final long serialVersionUID = 1482752098879191453L;

	private final Set<TopLevelNode> nodes;

	private PkgNode(Set<TopLevelNode> nodes) {
		this.nodes = nodes;
	}

	public Set<TopLevelNode> getNodes() {
		return nodes;
	}

	public static class Builder {
		private final Set<TopLevelNode> nodes = new HashSet<>();

		public void add(TopLevelNode node) throws CompileTimeException {
			if(!nodes.add(node)) {
				throw new CompileTimeException("Duplicate node");
			}
		}

		public PkgNode build() {
			return new PkgNode(Set.copyOf(nodes));
		}
	}

}
