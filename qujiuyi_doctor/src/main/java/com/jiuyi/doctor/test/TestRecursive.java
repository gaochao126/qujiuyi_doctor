package com.jiuyi.doctor.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 递归生成树
 * 
 * @author jiuyi
 *
 */
public class TestRecursive {

	public static void main(String[] args) {
		List<Node> nodes = new ArrayList<>();
		Node root = new Node(0, 0, "root");
		nodes.add(root);
		nodes.add(new Node(1, 0, "node" + 1));
		nodes.add(new Node(2, 0, "node" + 2));
		nodes.add(new Node(3, 1, "node" + 3));
		nodes.add(new Node(4, 1, "node" + 4));
		nodes.add(new Node(5, 2, "node" + 5));
		nodes.add(new Node(6, 2, "node" + 6));
		nodes.add(new Node(7, 0, "node" + 7));
		nodes.add(new Node(8, 6, "node" + 7));
		nodes.add(new Node(9, 4, "node" + 7));

		// buildTree(root, nodes);
		buildTree2(nodes);
		root.print(0);
	}

	public static void buildTree(Node parent, List<Node> nodes) {
		if (parent.builded()) {
			return;
		}
		parent.build();
		for (Node node : nodes) {
			if (node.parentId == parent.id && node.id != parent.id) {
				parent.children.add(node);
				buildTree(node, nodes);
			}
		}
	}

	public static void buildTree2(List<Node> nodes) {
		for (Node node : nodes) {
			node.children = new ArrayList<>();
			for (Node child : nodes) {
				if (node.id != child.id && child.parentId == node.id) {
					node.children.add(child);
				}
			}
		}
	}

	public static class Node {
		int id;
		int parentId;
		String data;
		List<Node> children;

		public Node(int id, int parentId, String data) {
			this.id = id;
			this.parentId = parentId;
			this.data = data;
		}

		public boolean isRoot() {
			return this.parentId == 0;
		}

		public boolean builded() {
			return this.children != null;
		}

		public void build() {
			this.children = new ArrayList<>();
		}

		public void print(int layer) {
			for (int i = 0; i < layer; i++) {
				System.out.print("-");
			}
			System.out.println(id);
			layer++;
			for (Node child : children) {
				child.print(layer);
			}
		}
	}

}
