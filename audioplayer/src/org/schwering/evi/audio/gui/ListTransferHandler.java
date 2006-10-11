/*
 * ListTransferHandler.java is used by the 1.4
 * ExtendedDnDDemo.java example.
 */
package org.schwering.evi.audio.gui;

import java.io.File;
import java.net.URL;

import javax.swing.*;

import org.schwering.evi.audio.core.Playlist;

public class ListTransferHandler extends StringTransferHandler {
    private int[] indices = null;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added.
            
    //Bundle up the selected items in the list
    //as a single string, for export.
    protected String exportString(JComponent c) {
        List list = (List)c;
        Playlist playlist = (Playlist)list.getPlaylist();
        indices = list.getSelectedIndices();
        
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < indices.length; i++) {
        	File file = (File)playlist.getElementAt(indices[i]);
        	if (file != null) {
        		buf.append(file.toString());
        		if (i != indices.length - 1) {
        			buf.append("\n");
        		}
        	}
        }
        return buf.toString();
    }

    //Take the incoming string and wherever there is a
    //newline, break it into a separate item in the list.
    protected void importString(JComponent c, String str) {
        JList target = (JList)c;
        Playlist playlist = (Playlist)target.getModel();
        int index = target.getSelectedIndex();

        //Prevent the user from dropping data back on itself.
        //For example, if the user is moving items #4,#5,#6 and #7 and
        //attempts to insert the items after item #5, this would
        //be problematic when removing the original items.
        //So this is not allowed.
        if (indices != null && index >= indices[0] - 1 &&
              index <= indices[indices.length - 1]) {
            indices = null;
            return;
        }

        int max = playlist.getSize();
        if (index < 0) {
            index = max;
        } else {
            index++;
            if (index > max) {
                index = max;
            }
        }
        addIndex = index;
        String[] values = str.split("\n");
        addCount = values.length;
        for (int i = 0; i < values.length; i++) {
        	try {
	        	URL url = new URL(values[i]);
	            playlist.addElementAt(index++, url);
        	} catch (Exception exc) {
        		throw new RuntimeException(exc);
        	}
        }
    }

    //If the remove argument is true, the drop has been
    //successful and it's time to remove the selected items 
    //from the list. If the remove argument is false, it
    //was a Copy operation and the original list is left
    //intact.
    protected void cleanup(JComponent c, boolean remove) {
        if (remove && indices != null) {
            JList source = (JList)c;
            Playlist playlist = (Playlist)source.getModel();
            //If we are moving items around in the same list, we
            //need to adjust the indices accordingly, since those
            //after the insertion point have moved.
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] > addIndex) {
                        indices[i] += addCount;
                    }
                }
            }
            for (int i = indices.length - 1; i >= 0; i--) {
                playlist.removeElementAt(indices[i]);
            }
        }
        indices = null;
        addCount = 0;
        addIndex = -1;
    }
}
