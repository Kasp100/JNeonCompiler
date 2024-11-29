package jneoncode.subnodes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import jneoncode.exceptions.CompileTimeException;

public class PkgNode implements Serializable {

	private static final long serialVersionUID = 1482752098879191453L;

	private final String name;

	private final Optional<PkgNode> superPkg;
	private final Set<PkgNode> subpkgs = new HashSet<>();
	private final Set<TypeNode> types = new HashSet<>();

	public PkgNode(PkgNode superPkg, String name) {
		this.superPkg = Optional.of(superPkg);
		this.name = Objects.requireNonNull(name);
	}
	
	public PkgNode(String name) {
		this.superPkg = Optional.empty();
		this.name = Objects.requireNonNull(name);
	}
	
	public String getName() {
		return name;
	}

	public void attach(TypeNode type) {
		if(!types.add(type)) {
			throw new CompileTimeException("Duplicate type signature");
		}
	}

	public void attach(PkgNode sub) {
		if(!subpkgs.add(sub)) {
			throw new CompileTimeException("Duplicate package name");
		}
	}

	public Optional<PkgNode> getSub(String name) {
		for(PkgNode sub : subpkgs) {
			if(sub.getName().equals(name)) {
				return Optional.of(sub);
			}
		}
		return Optional.empty();
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PkgNode other = (PkgNode) obj;
		return Objects.equals(name, other.name);
	}
	
	@Override
	public String toString() {
		if(superPkg.isPresent()) {
			return superPkg.toString() + "::" + getName();
		}else {
			return getName();
		}
	}

}
