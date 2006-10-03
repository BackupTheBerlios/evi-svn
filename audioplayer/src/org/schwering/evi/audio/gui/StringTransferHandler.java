/*
 * StringTransferHandler.java is used by the 1.4
 * ExtendedDnDDemo.java example.
 */
package org.schwering.evi.audio.gui;

import java.awt.datatransfer.*;
import javax.swing.*;
import java.io.IOException;

public abstract class StringTransferHandler extends TransferHandler {
    
    protected abstract String exportString(JComponent c);
    protected abstract void importString(JComponent c, String str);
    protected abstract void cleanup(JComponent c, boolean remove);
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
     */
    protected Transferable createTransferable(JComponent c) {
        return new StringSelection(exportString(c));
    }
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
     */
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
     */
    public boolean importData(JComponent c, Transferable t) {
        if (canImport(c, t.getTransferDataFlavors())) {
            try {
                String str = (String)t.getTransferData(DataFlavor.stringFlavor);
                importString(c, str);
                return true;
            } catch (UnsupportedFlavorException ufe) {
            } catch (IOException ioe) {
            }
        }

        return false;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#exportDone(javax.swing.JComponent, java.awt.datatransfer.Transferable, int)
     */
    protected void exportDone(JComponent c, Transferable data, int action) {
        cleanup(c, action == MOVE);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor[])
     */
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        for (int i = 0; i < flavors.length; i++) {
            if (DataFlavor.stringFlavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }
}
