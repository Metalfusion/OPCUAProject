package com.prosysopc.ua.android;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;

// A data container of the OPC UA nodes with only the interesting data for the UI
public class UINode implements Comparable<UINode> {

	public UINodeType type;
	public List<UINode> childNodes;
	public List<AttributeValuePair> attributes;
	public String name;
	public NodeId nodeID;

	public Boolean attributesSet = false;	// Have the attributes of this node been read and set
	public Boolean referencesSet = false;	// Have the child nodes of this node been read and set

	public UINode() {

		super();
		childNodes = new ArrayList<UINode>();
	}

	public UINode(UINodeType type, String name, NodeId nodeID) {

		this(type, name, nodeID, null);
	}

	public UINode(UINodeType type, String name, NodeId nodeID, List<UINode> childNodes, List<AttributeValuePair> attributes) {

		this(type, name, nodeID, childNodes);
		this.attributes = attributes;
	}

	public UINode(UINodeType type, String name, NodeId nodeID, List<UINode> childNodes) {

		super();
		this.type = type;
		this.name = name;
		this.nodeID = nodeID;

		if (childNodes != null) {
			this.childNodes = childNodes;
		} else {
			this.childNodes = new ArrayList<UINode>();
		}

	}

	public void addChild(UINode child) {

		if (!childNodes.contains(child)) {
			childNodes.add(child);
		}

	}

	public void addAttribute(String name, String value) {

		if (attributes == null) {
			attributes = new ArrayList<UINode.AttributeValuePair>();
		}

		attributes.add(new AttributeValuePair(name, value));
	}

	public void addAttributes(List<AttributeValuePair> list) {

		if (attributes == null) {

			attributes = list;
		} else {

			attributes.addAll(list);
		}

	}

	public NodeId getNodeId() {

		return nodeID;
	}

	// Enumeration for the node type, folder has children, leaf doesn't
	public enum UINodeType {
		
		// The id's are also the resource integers for the icons
		folderNode(R.drawable.folder), leafNode(R.drawable.text_list_bullets);
		
		public int value;
		private UINodeType(int value) {
			this.value = value;
		}

	}

	// Holds an attribute name and the related value
	public class AttributeValuePair {
		public String attrName;
		public String attrValue;

		public AttributeValuePair(String name, String value) {

			attrName = name;
			attrValue = value;
		}

	}

	// Sorting primarily by type and then by name
	@Override
	public int compareTo(UINode another) {

		if (this.type != another.type) {

			return this.type.value >= another.type.value ? 1 : -1;

		} else {

			return this.name.compareTo(another.name);
		}

	}

}
