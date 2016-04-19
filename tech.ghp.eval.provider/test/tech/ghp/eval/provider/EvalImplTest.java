package tech.ghp.eval.provider;

import junit.framework.TestCase;

/*
 * 
 * 
 * 
 */

public class EvalImplTest extends TestCase {
	
	/*
	 * 
	 * 
	 * 
	 */
	public void testSimple() throws Exception {
		EvalImpl t = new EvalImpl();
		assertEquals( 3.0,  t.eval("1 + 2"));
}
}
