package tech.ghp.eval.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A service that evaluates an expression
 */
@ProviderType
public interface Eval {
	
	/**
	 * Evaluates an expression and returns the result
	 */
	double eval(String expression) throws Exception;
	
}
