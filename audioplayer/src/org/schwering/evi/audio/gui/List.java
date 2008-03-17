package org.schwering.evi.audio.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.schwering.evi.audio.core.Playlist;

/**
 * A JList with listeners and so on.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class List extends JList implements ListCellRenderer, MouseListener, 
KeyListener {
	private static final long serialVersionUID = -8109383024385420651L;
	
	private MainPanel owner;
	private Hashtable<URL, ListItem> elements = new Hashtable<URL, ListItem>();
	private Playlist playlist;
	private PopupMenu popup = new PopupMenu(this);
	
	public List(MainPanel owner, Playlist playlist) {
		super(playlist);
		this.owner = owner;
		this.playlist = playlist;
		setCellRenderer(this);
		addKeyListener(this);
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
	 * Returns the cached element corresponding to the URL. 
	 * Generally, this is a Component (e.g. JLabel) that represents a playlist 
	 * entry (URL) in the list.
	 * @return The ListItem that represents URL in the list.
	 */
	public ListItem getListElement(URL url) {
		return elements.get(url);
	}

	/**
	 * Scrolls to the currently played URL. In fact, it tries to scroll to 
	 * an area +/- 5 songs around the current URL.<br />
	 */
	public void scrollToPlayingURL() {
		try {
			URL url = playlist.getPlayingURL();
			if (playlist == null || url == null) {
				return;
			}
			int index = playlist.indexOf(url);
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
	 * Plays a single selected URL.
	 */
	public void playSelected() {
		int index = getSelectedIndex();
		if (index != -1) {
			playlist.play(index);
		}
	}
	
	/**
	 * Removes all selected URLs from the list.
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
		URL url = (URL)value;
		
		if (!elements.containsKey(url)) {
			ListItem e = new ListItem(this, url);
			elements.put(url, e);
		}
		ListItem e = (ListItem)elements.get(url);
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
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			int index = getSelectedIndex();
			if (index != -1) {
				playlist.play(index);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			owner.requestFocus();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
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
