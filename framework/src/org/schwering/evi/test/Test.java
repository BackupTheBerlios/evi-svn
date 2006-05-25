package org.schwering.evi.test;

import java.net.URL;

import org.schwering.evi.core.IButtonable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.util.HTMLBrowser;

/**
 * Test module.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id: Test.java 50 2006-05-11 00:21:14Z schwering $
 */
public class Test extends HTMLBrowser implements IModule, IPanel, IButtonable {
	private static final long serialVersionUID = 6675184535258501432L;

	public Test(URL url) {
		super(url);
	}
	
	public Test(String s) {
		super(s);
	}
	
	public Test() {
		super("hello.html");
	}
	
	public String getTitle() {
		return "Testmodul";
	}
}
