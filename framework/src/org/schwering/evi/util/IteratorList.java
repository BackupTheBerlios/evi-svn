/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.util;

import java.util.List;
import java.util.Vector;

/**
 * A list combined with iterator navigation.<br />
 * <br />
 * Let's say in a list with 5 objects the current index is 2. Then this list's method 
 * work as follows:<br />
 * <pre>
 *  method: moveBefore()                                                                moveBehind()
 *            |                                                                             |
 *  index:   -1            0           1             2            3            4            5
 *  objects:              o[0]        o[1]          o[2]         o[3]         o[4]
 *                                     |             |            |
 *  method:                         previous()    current()      next()
 * </pre>
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class IteratorList {
	private int index = -1;
	private List list;
	
	/**
	 * Creates a new list.
	 */
	public IteratorList() {
		list = new Vector();
	}
	
	/**
	 * The size of the list.
	 * @return The size.
	 */
	public synchronized int size() {
		return list.size();
	}
	
	/**
	 * Appends an item at the end of the list.
	 * @param item The item.
	 */
	public synchronized void append(Object item) {
		if (item != null)
			list.add(size(), item);
	}
	
	/**
	 * Returns the current element.
	 * @return The current element or <code>null</code>.
	 */
	public synchronized Object current() {
		return (index >= 0 && index < size()) ? list.get(index) : null;
	}
	
	/**
	 * Returns the previous element.
	 * @return The previous element or <code>null</code>.
	 */
	public synchronized Object previous() {
		if (index >= 0)
			index--;
		return current();
	}
	
	/**
	 * Returns the next element.
	 * @return The next element or <code>null</code>.
	 */
	public synchronized Object next() {
		if (index < size())
			index++;
		return current();
	}
	
	/**
	 * Indicates whether the cursor is currently before the list.
	 * @return <code>true</code> if the cursor is before the list.
	 */
	public synchronized boolean isBefore() {
		return index == -1;
	}
	
	/**
	 * Indicates whether the cursor is currently behind the list.
	 * @return <code>true</code> if the cursor is behind the list.
	 */
	public synchronized boolean isBehind() {
		return index == size();
	}
	
	/**
	 * Moves the cursor before the list. This means that current() and previous() 
	 * both return <code>null</code> and next() returns the first element.
	 */
	public synchronized void moveBefore() {
		index = -1;
	}
	
	/**
	 * Moves the cursor behind the list. This means that current() and next() both 
	 * return <code>null</code> and previous() returns the last element.
	 */
	public synchronized void moveBehind() {
		index = size();
	}
	
	/**
	 * Removes the first element.
	 */
	public synchronized void removeFirst() {
		if (size() > 0)
			list.remove(0);
		if (index > -1) // adjust index only if index was not moveBefore()
			index--;
	}
	
	/**
	 * Removes the last element.
	 */
	public synchronized void removeLast() {
		if (size() > 0)
			list.remove(size() - 1);
		if (index > size())	// adjust index only if index was moveBehind()
			index--;
	}
}
