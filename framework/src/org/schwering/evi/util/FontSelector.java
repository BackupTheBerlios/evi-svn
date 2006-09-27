/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

/**
 * GUI component that allows to select a font. The JPanel has a GridLayout with two
 * rows. The first row contains a JComboBox with all fontnames, the second row 
 * contains a textfield for the fontsize and a JComboBox for the style.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class FontSelector extends JPanel {
	private static final long serialVersionUID = 7921314592883676271L;

	private Wrapper[] FONT_STYLES = new Wrapper[] {
			new Wrapper(Messages.getString("FontSelector.PLAIN"), "PLAIN"), //$NON-NLS-1$
			new Wrapper(Messages.getString("FontSelector.BOLD"), "BOLD"),  //$NON-NLS-1$
			new Wrapper(Messages.getString("FontSelector.ITALIC"), "ITALIC"), //$NON-NLS-1$
			new Wrapper(Messages.getString("FontSelector.BOLDITALIC"), "BOLDITALIC") //$NON-NLS-1$
	};
	
	private JComboBox fontName;
	private JTextField fontSize;
	private JComboBox fontStyle;
	
	/**
	 * Creates a new font selector panel.
	 */
	public FontSelector() {
		super(new GridLayout(2, 0));
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final String[] fontNames = ge.getAvailableFontFamilyNames();
		fontName = new JComboBox(fontNames);
		ListCellRenderer renderer = new ListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				int selectedIndex = -1;
				for (int i = 0; i < fontNames.length; i++) {
					if (fontNames[i].equals(value)) {
						selectedIndex = i;
						break;
					}
				}
				if (selectedIndex == -1) {
					return null;
				}
				JTextField tf = new JTextField() {
					private static final long serialVersionUID = -2242934440988161668L;

					public String toString() {
						return getText();
					}
				};
				tf.setText(fontNames[selectedIndex]);
				tf.setEditable(false);
				tf.setBorder(null);
				tf.setFont(new Font(fontNames[selectedIndex], Font.PLAIN, 12));
				
				if (isSelected) {
					tf.setBackground(list.getSelectionBackground());
					tf.setForeground(list.getSelectionForeground());
				} else {
					tf.setBackground(list.getBackground());
					tf.setForeground(list.getForeground());
				}
				return tf;
			}
		};
		fontName.setRenderer(renderer);
		
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
	
	/**
	 * Sets the currently selected font.
	 * @param f The font which should be selected.
	 */
	public void setSelectedFont(Font f) {
		String name = f.getFamily();
		int size = f.getSize();
		String style = Util.encodeFontStyle(f.getStyle());
		
		int fontNameCount = fontName.getItemCount();
		for (int i = 0; i < fontNameCount; i++) {
			if (name.equals(fontName.getItemAt(i).toString())) {
				fontName.setSelectedIndex(i);
				break;
			}
		}
		fontSize.setText(String.valueOf(size));
		fontStyle.setSelectedIndex(find(style, FONT_STYLES));
	}
	
	/**
	 * Returns the selected font.
	 * @return The selected font.
	 */
	public Font getSelectedFont() {
		String name = fontName.getSelectedItem().toString();
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
