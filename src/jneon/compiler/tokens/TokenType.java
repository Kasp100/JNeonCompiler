package jneon.compiler.tokens;

import java.util.Optional;

public enum TokenType {
	EOF,
	REFERENCE,
	STRING_LITERAL,
	NUMERIC_LITERAL,
	STATIC_ACCESSOR("::"),
	CURLY_OPEN("{"),
	CURLY_CLOSE("}"),
	SMALLER_THAN("<"),
	GREATER_THAN(">"),
	ROUND_OPEN("("),
	ROUND_CLOSE(")"),
	ASSIGNMENT("="),
	EQUALITY("=="),
	PACKAGE_DECL("pkg"),
	IMPORT("import"),
	CLASS_DECL("class"),
	ACCESS_PUBLIC("public"),
	ACCESS_PROTECTED("protected"),
	ACCESS_PRIVATE("private"),
	VAR("var"),
	BOOL_LIT_TRUE("true"),
	BOOL_LIT_FALSE("false"),
	UINT("#uint", "#nat"),
	INT("#int"),
	CHAR("#char"),
	BOOL("#bool", "#bit"),
	FP32("#fp32", "#float"),
	FP64("#fp64", "#double"),
	UINT64("#uint64", "#nat64"),
	INT64("#int64"),
	UINT32("#uint32", "#nat32"),
	INT32("#int32"),
	UINT16("#uint16", "#nat16"),
	INT16("#int64"),
	UINT8("#uint8", "#nat8", "#byte"),
	INT8("#int8"),
	SHARED("shared"),
	EXTERNAL("external"),
	CONST("const"),
	MUTABLE_REF("mut:"),
	MUTATING_DECL("mut"),
	COPY("copy"),
	CLASS_COPYABLE("copyable"),
	CLASS_SERIALISABLE("serialisable"),
	RETURN("ret"),
	END_STATEMENT(";"),
	COMMA(",");

	/** An array of the primary syntax followed by the aliases. */
	private final String[] syntax;
	
	private TokenType(String... syntax) {
		this.syntax = syntax;
	}

	private TokenType() {
		this.syntax = new String[0];
	}
	
	public Optional<String> getSyntax() {
		if(syntax.length > 0) {
			return Optional.of(syntax[0]);
		}else {
			return Optional.empty();
		}
	}

	public boolean hasMatchingSyntax(String s) {
		for(String keyword : syntax) {
			if(keyword.equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static Optional<TokenType> match(String s) {
		for(TokenType tt : values()) {
			if(tt.hasMatchingSyntax(s)) {
				return Optional.of(tt);
			}
		}
		return Optional.empty();
	}
	
}
