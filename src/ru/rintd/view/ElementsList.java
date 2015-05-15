package ru.rintd.view;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import ru.rintd.json2grid.Node;

public class ElementsList extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6333835739404395302L;
	private JTree jTree;
	private DefaultMutableTreeNode root = new DefaultMutableTreeNode("Nodes");
	private DefaultTreeModel model;
	
	public ElementsList(ArrayList<ArrayList<Node>> nodes){
		jTree = new JTree(root);
		add(jTree);
		model = new DefaultTreeModel(root);
		jTree.setModel(model);
		setNodes(nodes);
	}
	
	public void setNodes(ArrayList<ArrayList<Node>> nodes){
		root.removeAllChildren();
		if (nodes != null & nodes.size() != 0){
			int i = 0;
			for (ArrayList<Node> arrayList : nodes) {
				DefaultMutableTreeNode childLevel = new DefaultMutableTreeNode("level"+(i+1));
				for (Node node : arrayList) {
					DefaultMutableTreeNode treeElement = new DefaultMutableTreeNode("UID: "+node.uid);
					DefaultMutableTreeNode elemBE = new DefaultMutableTreeNode("Building element id:"+node.buildElID);
					treeElement.add(elemBE);
					DefaultMutableTreeNode elemH = new DefaultMutableTreeNode("Height: "+node.h);
					treeElement.add(elemH);
					DefaultMutableTreeNode elemType = new DefaultMutableTreeNode("Type: "+node.type);
					treeElement.add(elemType);
					DefaultMutableTreeNode elemXY = new DefaultMutableTreeNode("XY: ["+node.xy[0]+"; "+node.xy[1]+"]");
					treeElement.add(elemXY);
					childLevel.add(treeElement);
				}
				root.add(childLevel);
				i++;
			}
		}
		updateUI();
	}
	
}
