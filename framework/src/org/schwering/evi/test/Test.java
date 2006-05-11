package org.schwering.evi.test;

import org.schwering.evi.core.*;
import org.schwering.evi.util.HTMLBrowser;

/**
 * Test module.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id: Test.java 50 2006-05-11 00:21:14Z schwering $
 */
public class Test extends HTMLBrowser implements IModule, IPanel {
	private static final long serialVersionUID = 6675184535258501432L;

	public Test(String url) {
		super(url);
		System.out.println("module would start with args: '"+ url +"'");
	}
	
	public Test() {
		super("hello.html");
		System.out.println("module would start without args");
	}
	
	public void dispose() {
		System.out.println("IModule.dispose() in org.schwering.evi.test.Test");
		super.dispose();
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return "Testmodule";
	}
}
