package org.schwering.evi.test;

import java.awt.Component;

import javax.swing.Icon;

import org.schwering.evi.core.*;
import org.schwering.evi.gui.main.HelloWorldPanel;
import org.schwering.evi.util.HTMLBrowser;

public class Test implements IModule, IPanel {
	
	private Component instance;
	
	public Test(IParent parent, String url) {
		System.out.println("module would start with args: '"+ url +"'");
		instance = new HTMLBrowser(url);
	}
	
	public Test(IParent parent) {
		System.out.println("module would start without args!");
		instance = HelloWorldPanel.getInstance();
	}
	
	public void dispose() {
		System.out.println("IModule.dispose() in org.schwering.evi.test.Test");
	}
	
	public String getTitle() {
		return "TestModule";
	}
	
	public Component getPanelInstance() {
		return instance;
	}
	
	public Icon getIcon() {
		return null;
	}

}
