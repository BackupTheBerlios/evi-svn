package org.schwering.evi.audio.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.schwering.evi.audio.lang.Messages;

/**
 * A JPopupMenu for the List list.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class Popup extends JPopupMenu {
	private static final long serialVersionUID = 963478455107252205L;
	
	private JMenuItem playItem = new JMenuItem(Messages.getString("MainPanel.PLAY")); //$NON-NLS-1$
	private JMenuItem removeItem = new JMenuItem(Messages.getString("MainPanel.REMOVE")); //$NON-NLS-1$
	private JMenuItem addToQueueItem = new JMenuItem(Messages.getString("MainPanel.ADD_TO_QUEUE")); // $NON-NLS-1$
	private JMenuItem removeFromQueueItem = new JMenuItem(Messages.getString("MainPanel.REMOVE_FROM_QUEUE")); // $NON-NLS-1$
	
	public Popup(final List owner) {
		playItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.playSelected();
			}
		});
		removeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.removeSelected();
			}
		});
		addToQueueItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] indices = owner.getSelectedIndices();
				for (int i = 0; i < indices.length; i++) {
					owner.getPlaylist().addToQueue(indices[i]);
				}
			}
		});
		removeFromQueueItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] indices = owner.getSelectedIndices();
				for (int i = 0; i < indices.length; i++) {
					owner.getPlaylist().removeFromQueue(indices[i]);
				}
			}
		});
		add(playItem);
		add(removeItem);
		add(addToQueueItem);
		add(removeFromQueueItem);
	}
	
	public void setPlayEnabled(boolean b) {
		playItem.setEnabled(b);
	}
	
	public void setRemoveEnabled(boolean b) {
		removeItem.setEnabled(b);
	}
	
	public void setAddToQueueEnabled(boolean b) {
		addToQueueItem.setEnabled(b);
	}
	
	public void setRemoveFromQueueEnabled(boolean b) {
		removeFromQueueItem.setEnabled(b);
	}
}
