package org.schwering.evi.gui.main;

import java.util.Vector;

import javax.swing.SingleSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Generic implementation of <code>SingleSelectionModel</code>.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class TabBarSingleSelectionModel implements SingleSelectionModel {
	protected Vector listeners = new Vector();
	protected int index = -1;

	/* (non-Javadoc)
	 * @see javax.swing.SingleSelectionModel#clearSelection()
	 */
	public void clearSelection() {
		index = -1;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SingleSelectionModel#getSelectedIndex()
	 */
	public int getSelectedIndex() {
		return index;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SingleSelectionModel#setSelectedIndex(int)
	 */
	public void setSelectedIndex(int i) {
		if (index != i) {
			index = i;
			fireStateChanged();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.SingleSelectionModel#isSelected()
	 */
	public boolean isSelected() {
		return index != -1;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SingleSelectionModel#removeChangeListener(javax.swing.event.ChangeListener)
	 */
	public void removeChangeListener(ChangeListener listener) {
		listeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see javax.swing.SingleSelectionModel#addChangeListener(javax.swing.event.ChangeListener)
	 */
	public void addChangeListener(ChangeListener listener) {
		listeners.add(listener);
	}
	
	protected void fireStateChanged() {
		ChangeEvent e = new ChangeEvent(this);
		for (int i = 0; i < listeners.size(); i++) {
			((ChangeListener)listeners.get(i)).stateChanged(e);
		}
	}
}
