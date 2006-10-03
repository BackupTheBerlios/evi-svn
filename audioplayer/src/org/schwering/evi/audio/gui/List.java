package org.schwering.evi.audio.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Hashtable;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.schwering.evi.audio.core.Playlist;

/**
 * A JList with listeners and so on.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class List extends JList implements ListCellRenderer, MouseListener {
	private static final long serialVersionUID = -8109383024385420651L;
	
	private Hashtable elements = new Hashtable();
	private Playlist playlist;
	private Popup popup = new Popup(this);
	
	public List(Playlist playlist) {
		super(playlist);
		this.playlist = playlist;
		setCellRenderer(this);
		addMouseListener(this);
		setDragEnabled(true);
		setTransferHandler(new ListTransferHandler());
	}
	
	/**
	 * Returns the currently displayed playlist.
	 * @return The playlist.
	 */
	public Playlist getPlaylist() {
		return playlist;
	}
	
	/**
	 * Sets a new playlist as the displayed object of this list.
	 * @param playlist The new list.
	 */
	public void setPlaylist(Playlist playlist) {
		setModel(playlist);
		this.playlist = playlist;
	}
	
	/**
	 * Returns the cached element corresponding to the file. 
	 * Generally, this is a Component (e.g. JLabel) that represents a playlist 
	 * entry (file) in the list.
	 * @return The ListElement that represents file in the list.
	 */
	public ListElement getListElement(File file) {
		return (ListElement)elements.get(file);
	}

	/**
	 * Scrolls to the currently played file. In fact, it tries to scroll to 
	 * an area +/- 5 songs around the current file.<br />
	 */
	public void scrollToPlayingFile() {
		try {
			File file = playlist.getPlayingFile();
			if (playlist == null || file == null) {
				return;
			}
			int index = playlist.indexOf(file);
			if (index != -1) {
				int max = getLastVisibleIndex() - getFirstVisibleIndex();
				int index0 = index - max / 3;
				int index1 = index + max / 3;
				while (index0 < 0) {
					index0++;
				}
				while (index1 >= playlist.getSize()) {
					index1--;
				}
				
				Point p0 = indexToLocation(index0);
				Point p1 = indexToLocation(index1);
				
				int x = p0.x;
				int y = p0.y;
				int width = p1.x - x;
				int height = p1.y - y;
				Rectangle r = new Rectangle(x, y, width, height);
				scrollRectToVisible(r);
				repaint();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/**
	 * Wraps JList.indexToLocation. Because JList.indexToLocation seems to 
	 * be buggy, it tries up to 6 times to calculate the point. Each result 
	 * of JList.indexToLocation is compared with the result of 
	 * JList.locationToIndex.<br />
	 * <b>Note:</b> Swing has bugs. <code>JList.indexToLocation</code> might 
	 * do what it wants. Hence, encapsulate all calls of this method in 
	 * try/catch blocks.
	 * @param index The index of the line.
	 * @return The point.
	 */
	public Point indexToLocation(int index) {
		Point p = super.indexToLocation(index);
		for (int i = 0; i < 10 && locationToIndex(p) != index; i++) {
			p = super.indexToLocation(index);
		}
		return p;
	}
	
	/**
	 * Plays a single selected file.
	 */
	public void playSelected() {
		int index = getSelectedIndex();
		if (index != -1) {
			playlist.play(index);
		}
	}
	
	/**
	 * Removes all selected files from the list.
	 */
	public void removeSelected() {
		int[] arr = getSelectedIndices();
		if (arr != null) {
			for (int i = arr.length - 1; i >= 0; i--) {
				playlist.removeElementAt(arr[i]);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value, 
			int index, boolean isSelected, boolean cellHasFocus) {
		File file = (File)value;
		
		if (!elements.containsKey(file)) {
			ListElement e = new ListElement(this, file);
			elements.put(file, e);
		}
		ListElement e = (ListElement)elements.get(file);
		if (isSelected) {
			e.setForeground(list.getSelectionForeground());
			e.setBackground(list.getSelectionBackground());
		} else {
			e.setForeground(list.getForeground());
			e.setBackground(list.getBackground());
		}
		return e;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
			playSelected();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if (e.getClickCount() == 1 
				&& e.getButton() == MouseEvent.BUTTON3) {
			int index = getSelectedIndex();
			boolean enable = index != -1;
			popup.setPlayEnabled(enable);
			popup.setRemoveEnabled(enable);
			popup.setAddToQueueEnabled(enable && !playlist.isInQueue(index));
			popup.setRemoveFromQueueEnabled(enable && playlist.isInQueue(index));
			popup.show(this, e.getX(), e.getY());
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
	}
	
}
