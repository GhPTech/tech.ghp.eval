package tech.ghp.eval.provider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.service.component.annotations.Component;

import osgi.enroute.debug.api.Debug;
import tech.ghp.eval.api.Eval;

/**
 * 
 */
@Component(
			name = "tech.ghp.eval", 
			property={Debug.COMMAND_SCOPE + "=test",Debug.COMMAND_FUNCTION + "=eval"}
			)
public class EvalImpl implements Eval{
	Pattern EXPR = Pattern.compile("\\s*(?<left>\\d+)\\s*(?<op>\\+|-|\\*|/)\\s*(?<right>\\d+)\\s*");
    @Override
	public double eval(String expression) throws Exception {
		Matcher m = EXPR.matcher(expression);
		if ( !m.matches())
			throw new IllegalArgumentException("Invalid expression " + expression);

		double left = Double.valueOf( m.group("left"));
		double right = Double.valueOf( m.group("right"));
		switch( m.group("op")) {
			case "+": return left + right;
			case "-": return left - right;
			case "*": return left * right;
			case "/": return left / right;
		}
		return Double.NaN;
	}

}
