package com.sinosoft.one.sqlparser.test.drop;

import java.io.StringReader;

import junit.framework.TestCase;
import com.sinosoft.one.sqlparser.JSQLParserException;
import com.sinosoft.one.sqlparser.parser.CCJSqlParserManager;
import com.sinosoft.one.sqlparser.statement.drop.Drop;

public class DropTest extends TestCase {
	CCJSqlParserManager parserManager = new CCJSqlParserManager();

	public DropTest(String arg0) {
		super(arg0);
	}

	public void testDrop() throws JSQLParserException {
		String statement =
			"DROP TABLE mytab";
		Drop drop = (Drop) parserManager.parse(new StringReader(statement));
		assertEquals("TABLE", drop.getType());
		assertEquals("mytab", drop.getName());
		assertEquals(statement, ""+drop);
		
		statement =
					"DROP INDEX myindex CASCADE";
		drop = (Drop) parserManager.parse(new StringReader(statement));
		assertEquals("INDEX", drop.getType());
		assertEquals("myindex", drop.getName());
		assertEquals("CASCADE", drop.getParameters().get(0));
		assertEquals(statement, ""+drop);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(DropTest.class);
	}

}