package com.peterverzijl.softwaresystems.qwirkle.gameengine.editor;

import java.awt.BorderLayout;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.EngineObject;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameEngineComponent;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Component;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameObject;

@SuppressWarnings("serial")
public class GameObjectViewer extends JFrame {

	JTextField textField = new JTextField();
	JScrollPane scrollPane = new JScrollPane();
	JTree tree;

	Renderer renderer;

	GameEngineComponent mEngine;

	public GameObjectViewer(GameEngineComponent engine) {
		mEngine = engine;

		// Get all gameobjects
		List<GameObject> gameObjects = new ArrayList<GameObject>();
		for (EngineObject eo : GameEngineComponent.objects) {
			if (eo instanceof GameObject) {
				gameObjects.add((GameObject) eo);
			}
		}

		// Populate the list with gameobjects and components
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Game");
		for (GameObject go : gameObjects) {
			DefaultMutableTreeNode goNode = new DefaultMutableTreeNode(go.name);
			rootNode.add(goNode);
			for (Component c : go.getComponents()) {
				DefaultMutableTreeNode cNode = new DefaultMutableTreeNode(c.getClass().getSimpleName());
				goNode.add(cNode);
			}
		}

		tree = new JTree(rootNode);

		getContentPane().setLayout(new BorderLayout());
		tree.setCellRenderer(renderer);
		tree.addTreeSelectionListener(new TreeHandler());
		scrollPane.getViewport().add(tree);
		getContentPane().add("Center", scrollPane);
		getContentPane().add("South", textField);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setSize(500, 500);
		setVisible(true);
	}

	public class TreeHandler implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			TreePath path = e.getPath();
			String text = path.getPathComponent(path.getPathCount() - 1).toString();
			if (path.getPathCount() > 3) {
				text += ": ";
				text += Integer.toString((int) (Math.random() * 50)) + " Wins ";
				text += Integer.toString((int) (Math.random() * 50)) + " Losses";
			}
			textField.setText(text);
		}
	}
}

@SuppressWarnings("serial")
class Renderer extends JLabel implements TreeCellRenderer {
	public java.awt.Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		setText(value.toString() + "                   ");
		return this;
	}
}
