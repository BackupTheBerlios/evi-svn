package org.schwering.evi.util;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.schwering.evi.gui.conf.Messages;

/**
 * GUI component that allows to select a font. The JPanel has a GridLayout with two
 * rows. The first row contains a JComboBox with all fontnames, the second row 
 * contains a textfield for the fontsize and a JComboBox for the style.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class FontSelector extends JPanel {
	private Wrapper[] FONT_STYLES = new Wrapper[] {
			new Wrapper(Messages.getString("MainConfigurationPanel.PLAIN"), "PLAIN"), //$NON-NLS-1$ //$NON-NLS-2$
			new Wrapper(Messages.getString("MainConfigurationPanel.BOLD"), "BOLD"), //$NON-NLS-1$ //$NON-NLS-2$
			new Wrapper(Messages.getString("MainConfigurationPanel.ITALIC"), "ITALIC"), //$NON-NLS-1$ //$NON-NLS-2$
			new Wrapper(Messages.getString("MainConfigurationPanel.BOLD_AND_ITALIC"), "BOLDITALIC") //$NON-NLS-1$ //$NON-NLS-2$
	};
	
	private JComboBox fontName;
	private JTextField fontSize;
	private JComboBox fontStyle;
	
	public FontSelector() {
		super(new GridLayout(2, 0));
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fonts = ge.getAvailableFontFamilyNames();
		fontName = new JComboBox(fonts);
		
		fontSize = new JTextField(3);
		fontSize.setText(String.valueOf(fontSize));
		RightClickMenu.addRightClickMenu(fontSize);
		
		fontStyle = new JComboBox(FONT_STYLES);
		
		JPanel sub1 = new JPanel(new BorderLayout());
		sub1.add(fontName);
		JPanel sub2 = new JPanel(new BorderLayout());
		sub2.add(fontSize, BorderLayout.WEST);
		sub2.add(new JLabel("pt  "), BorderLayout.CENTER); //$NON-NLS-1$
		sub2.add(fontStyle, BorderLayout.EAST);
		
		add(sub1);
		add(sub2);
	}
	
	public void setSelectedFont(Font f) {
		String name = f.getFamily();
		int size = f.getSize();
		String style = Util.encodeFontStyle(f.getStyle());
		
		fontName.setSelectedItem(name);
		fontSize.setText(String.valueOf(size));
		fontStyle.setSelectedIndex(find(style, FONT_STYLES));
	}
	
	public Font getSelectedFont() {
		String name = (String)fontName.getSelectedItem();
		String size = fontSize.getText().trim();
		Wrapper w = (Wrapper)fontStyle.getSelectedItem();
		String style = (String)w.getObject();
		Font font = Util.decodeFont(name, size, style);
		return font;
	}
	
	/**
	 * Looks for a given object in an array of wrappers.
	 * @param searched The object which is searched.
	 * @param objs The objects wrapped by the <code>Wrapper</code>s in this 
	 * array are compared to <code>searched</code>. 
	 * @return The index or -1.
	 */
	private static int find(Object searched, Wrapper[] objs) {
		if (objs == null) {
			return -1;
		}
		for (int i = 0; i < objs.length; i++) {
			if (objs[i].getObject() != null 
					&& objs[i].getObject().equals(searched)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Wraps any object.
	 */
	class Wrapper {
		private Object object;
		private String string;
		
		public Wrapper(String key, Object obj) {
			string = key;
			object = obj;
		}
		
		public Object getObject() {
			return object;
		}
		
		public String toString() {
			return string;
		}
	}
}
