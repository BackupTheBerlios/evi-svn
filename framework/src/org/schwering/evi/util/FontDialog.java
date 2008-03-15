/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

/**
 * GUI component that allows to select a font. The JPanel has a GridLayout with three
 * rows. The first row contains a JComboBox with all fontnames, the second row 
 * contains a textfield for the fontsize and a JComboBox for the style, the third 
 * row the close button.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class FontDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6642226249921646986L;

	private Wrapper[] FONT_STYLES = new Wrapper[] {
			new Wrapper(Messages.getString("FontDialog.PLAIN"), "PLAIN"), //$NON-NLS-1$
			new Wrapper(Messages.getString("FontDialog.BOLD"), "BOLD"),  //$NON-NLS-1$
			new Wrapper(Messages.getString("FontDialog.ITALIC"), "ITALIC"), //$NON-NLS-1$
			new Wrapper(Messages.getString("FontDialog.BOLDITALIC"), "BOLDITALIC") //$NON-NLS-1$
	};
	
	private JComboBox fontName;
	private JTextField fontSize;
	private JComboBox fontStyle;
	private JButton closeButton;
	
	/**
	 * Creates a new font selector panel.
	 */
	public FontDialog(JFrame owner, Font font) {
		super(owner);
		
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
		fontSize.setText(String.valueOf(12));
		RightClickMenu.addRightClickMenu(fontSize);
		
		closeButton = new JButton(Messages.getString("FontDialog.CLOSE")); //$NON-NLS-1$
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		fontStyle = new JComboBox(FONT_STYLES);
		JPanel sub1 = new JPanel(new BorderLayout());
		sub1.add(fontName);
		JPanel sub2 = new JPanel(new BorderLayout());
		sub2.add(fontSize, BorderLayout.WEST);
		sub2.add(new JLabel("pt  "), BorderLayout.CENTER); //$NON-NLS-1$
		sub2.add(fontStyle, BorderLayout.EAST);
		JPanel sub3 = new JPanel(new BorderLayout());
		sub3.add(new JPanel(), BorderLayout.WEST);
		sub3.add(closeButton, BorderLayout.CENTER);
		sub3.add(new JPanel(), BorderLayout.EAST);
		
		JPanel p = new JPanel(new GridLayout(3, 0));
		p.add(sub1);
		p.add(sub2);
		p.add(sub3);
		
		setSelectedFont(font);
		
		setContentPane(p);
		
		setModal(true);
		setSize(200, 100);
		setComponentOrientation((owner == null) 
				? getRootPane().getComponentOrientation() 
				: owner.getComponentOrientation());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Util.centerComponent(this);
		setVisible(true);
	}
	
	/**
	 * Invoked when the window is closed.
	 */
	public void close() {
		setVisible(false);
		dispose();
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
