package xdevs.lib.projects.graph;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class TreeRender extends DefaultTreeCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	    
    public TreeRender () {
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        TreeNode nodoInfo = (TreeNode)(node.getUserObject());
        setIcon(nodoInfo.getIcono());
        setToolTipText(nodoInfo.getTexto());
        setText(nodoInfo.getTexto());
        return this;
    }
}
