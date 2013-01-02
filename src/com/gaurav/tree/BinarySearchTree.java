/*
 * Copyright 2010 Gaurav Saxena
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gaurav.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The nodes in this class always have a particular number of children. It is not possible to add more children 
 * than the number provided in the constructor. Since the children are numbered, it is possible to access them
 * by index. This class serves as a base class to trees which are used to keep order among its elements e.g. BST
 * 
 * add(parent, child) adds child to the first available slot. Thus, it is better to add nodes using 
 * add(parent, child, index)
 * @author Gaurav Saxena
 *
 * @param <E>
 */
public class BinarySearchTree<E extends Comparable<E>> implements NumberedTree<E>, Cloneable {
	private ArrayList<E> nodeList = new ArrayList<E>();
	private ArrayList<Integer> parentList = new ArrayList<Integer>();
	private ArrayList<int[]> childrenArray = new ArrayList<int[]>();
	private int size = 0;
	private int depth = 0;
	private final int MAX_CHILDREN = 2;
	private int rootIndex = -1;
	@Override
	public boolean add(E parent, E child) throws NodeNotFoundException
	{
		throw new UnsupportedOperationException("A binary search tree determines parent of a child on its own and hence it is not possible to add the child to any given parent. Please use add(child)");
	}
	@Override
	public boolean add(E parent, E child, int index) throws NodeNotFoundException
	{
		throw new UnsupportedOperationException("A binary search tree determines parent of a child on its own and hence it is not possible to add the child to any given parent or index. Please use add(child)");
	}
	/**
	 * If tree is empty, it adds a root. In case tree is not empty, it will attempt to add parameter as a child of the root 
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	@Override
	public boolean add(E child) {
		try {
			if(size() == 0)
				return addChild(null, child);
			else {
				E parent = findParent(root(), child);
				return addChild(parent, child, parent.compareTo(child) > 0 ? 0 : 1);
			}
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * Adds child at the first available slot in the children array. 
	 * If none of the slots are available it throws exception
	 * @see com.gaurav.tree.Tree#add(java.lang.Object, java.lang.Object)
	 **/
	private boolean addChild(E parent, E child) throws NodeNotFoundException {
		checkNode(child);
		if(isRootElementBeingAdded(parent, child))
			return true;
		int	parentIndex = nodeList.indexOf(parent);
		if(parentIndex > -1) {
			int childIndex = nodeList.indexOf(child);
			int emptySlot;
			if(childIndex == -1) {
				if((emptySlot = getEmptySlot(childrenArray.get(parentIndex))) > -1) {
					addChild(child, parentIndex, emptySlot);
					return true;
				} else
					throw new IndexOutOfBoundsException("Children array of parent is already full");
			} else {
				nodeList.set(childIndex, child);
				return false;
			}
		} else
			throw new NodeNotFoundException("No node was found for parent object");
	}
	private boolean isRootElementBeingAdded(E parent, E child) {
		if(parent == null) {
			if(isEmpty()) {
				addRoot(child);
				return true;
			} else
				throw new IllegalArgumentException("parent cannot be null except for root element");
		} else
			return false;
	}

	private boolean addChild(E parent, E child, int index) throws NodeNotFoundException {
		checkNode(child);
		checkIndex(index);
		if(isRootElementBeingAdded(parent, child))
			return true;
		int	parentIndex = nodeList.indexOf(parent);
		if(parentIndex > -1) {
			if(nodeList.indexOf(child) == -1) {
				addChild(child, parentIndex, index);
				return true;
			} else
				return false;
		} else
			throw new NodeNotFoundException("No node was found for parent object");
	}

	private void checkIndex(int index) {
		if(index < 0 || index > MAX_CHILDREN - 1)
			throw new IndexOutOfBoundsException("index found to be " + index + ".It should be between 0 and " + (MAX_CHILDREN - 1));
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean retVal = false;
		for (Iterator<? extends E> iterator = c.iterator(); iterator.hasNext();)
			retVal |= add(iterator.next());
		return retVal;
	}
	@Override
	public boolean addAll(E parent, Collection<? extends E> c) {
		try{
			for (Iterator<? extends E> iterator = c.iterator(); iterator.hasNext();)
				add(parent, iterator.next());
			return true;
		} catch(NodeNotFoundException ex) {
			return false;
		}
	}
	@Override
	public E child(E parent, int index) throws NodeNotFoundException {
		checkNode(parent);
		int parentIndex = nodeList.indexOf(parent);
		int childIndex;
		if(parentIndex > -1) {
			if((childIndex = childrenArray.get(parentIndex)[index]) > -1)
				return nodeList.get(childIndex);
			else
				return null;
		} else
			throw new NodeNotFoundException("No node was found for object");
	}
	@Override
	public List<E> children(E e) throws NodeNotFoundException {
		checkNode(e);
		int index = nodeList.indexOf(e);
		if(index > -1) {
			ArrayList<E> children = new ArrayList<E>();
			for (int i = 0; i < childrenArray.get(index).length; i++)
				if(childrenArray.get(index)[i] > -1)
					children.add(nodeList.get(childrenArray.get(index)[i]));
			return children;
		} else
			throw new NodeNotFoundException("No node was found for object");
	}
	@Override
	public void clear() {
		nodeList.clear();
		parentList.clear();
		childrenArray.clear();
		size = 0;
		depth = 0;
		rootIndex = -1;
	}
	@Override
	@SuppressWarnings("unchecked")
	public Object clone() {
		BinarySearchTree<E> v = null;
		try {
			v = (BinarySearchTree<E>) super.clone();
			v.nodeList = (ArrayList<E>) nodeList.clone();
			v.parentList = (ArrayList<Integer>) parentList.clone();
			v.childrenArray = new ArrayList<int[]>();
			v.size = this.size;
			v.depth = this.depth;
			for(int i = 0; i < childrenArray.size(); i++)
				v.childrenArray.add(Arrays.copyOf(childrenArray.get(i), childrenArray.get(i).length));
		} catch (CloneNotSupportedException e) {
			//This should't happen because we are cloneable
		}
		return v;
	}
	@Override
	public E commonAncestor(E node1, E node2) throws NodeNotFoundException {
		checkNode(node1);
		checkNode(node2);
		int height1 = 0;
		E e1 = node1; 
		while(e1 != null) {
			height1++;
			e1 = parent(e1);
		}
		int height2 = 0;
		E e2 = node2; 
		while(e2 != null) {
			height2++;
			e2 = parent(e2);
		}
		if(height1 > height2) {
			while(height1 - height2 > 0) {
				node1 = parent(node1);
				height1--;
			}
		} else {
			while(height2 - height1 > 0) {
				node2 = parent(node2);
				height2--;
			}
		}
		while(node1 != null && !node1.equals(node2)) {
			node1 = parent(node1);
			node2 = parent(node2);
		}
		return node1;
	}
	@Override
	public boolean contains(Object o) {
		if(o == null)
			return false;
		else
			return nodeList.contains(o);
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		return nodeList.containsAll(c);
	}
	@Override
	public int depth() {
		return depth;
	}
	@Override
	@Deprecated
	public List<E> inorderOrderTraversal() {
		return inorderOrderTraversal(0, new ArrayList<E>());
	}
	@Override
	public List<E> inOrderTraversal() {
		if(isEmpty())
			return new ArrayList<E>();
		else
			return inorderOrderTraversal(rootIndex, new ArrayList<E>());
	}
	@Override
	public boolean isAncestor(E node, E child) throws NodeNotFoundException {
		checkNode(child);
		child = parent(child);
		if(node != null) {//if parent is root, it has to be an ancestor
			while(child != null) {
				if(child.equals(node))
					return true;
				else
					child = parent(child);
			}
		}
		return true;
	}
	@Override
	public boolean isDescendant(E parent, E node) throws NodeNotFoundException {
		checkNode(node);
		int index = nodeList.indexOf(node);
		E child = parent(node);
		if(index > -1) {
			while(child != null) {
				if(child.equals(parent))
					return true;
				else
					child = parent(child);
			}
			return false;
		} else
			throw new NodeNotFoundException("No node was found for object");
	}
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	@Override
	public Iterator<E> iterator() {
		return getCurrentList().iterator();
	}
	@Override
	public List<E> leaves() {
		if(isEmpty())
			return new ArrayList<E>();
		else
			return leaves(rootIndex, new ArrayList<E>());
	}
	private List<E> leaves(int nodeIndex, ArrayList<E> list) {
		int[] children = childrenArray.get(nodeIndex);
		if(children.length > 0)	{
			int i = 0;
			int chidrenLength = children.length;
			for(int len = (int)Math.ceil((double)chidrenLength / 2); i < len; i++)
				if(children[i] > -1)
					leaves(children[i], list);
			if(isChildrenArrayEmpty(childrenArray.get(nodeIndex)))
				list.add(nodeList.get(nodeIndex));
			for(int len = chidrenLength; i < len; i++)
				if(children[i] > -1)
					leaves(children[i], list);
		}
		else if(isChildrenArrayEmpty(childrenArray.get(nodeIndex)))
			list.add(nodeList.get(nodeIndex));
		return list;
	}
	@Override
	public List<E> levelOrderTraversal() {
		if(isEmpty())
			return new ArrayList<E>();
		else {
			LinkedList<Integer> queue = new LinkedList<Integer>();
			queue.add(0);
			return levelOrderTraversal(new ArrayList<E>(), queue);
		}
	}
	@Override
	public E parent(E e) throws NodeNotFoundException {
		checkNode(e);
		int index = nodeList.indexOf(e);
		if(index == 0)
			return null;
		else if(index > 0)
			return nodeList.get(parentList.get(index));
		else
			throw new NodeNotFoundException("No node was found for object");
	}
	@Override
	public List<E> postOrderTraversal() {
		if(isEmpty())
			return new ArrayList<E>();
		else
			return postOrderTraversal(rootIndex, new ArrayList<E>());
	}
	public List<E> preOrderTraversal() {
		if(isEmpty())
			return new ArrayList<E>();
		else
			return preOrderTraversal(rootIndex, new ArrayList<E>());
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		checkNode((E)o);
		int index = nodeList.indexOf((E)o);
		if(index > -1) {
			try {//http://en.wikipedia.org/wiki/Binary_search_tree#Deletion
				List<E> children = children((E) o);
				if(children.isEmpty())
					deleteCase1(index);
				else if(children.size() == 1)
					deleteCase2(index);
				else
					deleteCase3(index, o);
				return true;
			} catch (NodeNotFoundException e) {
				e.printStackTrace();//Not expected as we have already checked for presence in tree
				return false;
			}
		}
		return false;
	}
	@SuppressWarnings("unchecked")
	private void deleteCase3(int index, Object o) throws NodeNotFoundException {
		E successor = successor((E)o);
		int successorIndexOf = nodeList.indexOf(successor);
		nodeList.set(index, successor);
		nodeList.set(successorIndexOf, (E) o);
		remove(o);		
	}
	private void deleteCase2(int index) {
		Integer parentIndex = parentList.set(index, -1);
		for(int i = 0; i < childrenArray.get(parentIndex).length; i++)
			if(childrenArray.get(parentIndex)[i] == index)
				childrenArray.get(parentIndex)[i] = -1;
		nodeList.set(index, null);
		size--;
		for(int i : childrenArray.get(index))
			if(i > -1)
				parentList.set(i, parentIndex);
		depth = recalculateDepth(rootIndex, 0);		
	}
	private void deleteCase1(int index) {
		Integer parentIndex = parentList.set(index, -1);
		for(int i = 0; i < childrenArray.get(parentIndex).length; i++)
			if(childrenArray.get(parentIndex)[i] == index)
				childrenArray.get(parentIndex)[i] = -1;
		nodeList.set(index, null);
		size--;
		depth = recalculateDepth(rootIndex, 0);
	}
	private E successor(E node) throws NodeNotFoundException {
		E right = right(node);
		if(right != null) {
			node = right;
			while(left(node) != null)
				node = left(node);
			return node;
		} else {
			while(!right(parent(node)).equals(node))
				node = parent(node);
			return node;
		}
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean retVal = false;
		for (Iterator<?> iterator = c.iterator(); iterator.hasNext();)
			retVal |= remove(iterator.next());
		return retVal;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Tree interface doesn't support retainAll");
	}

	@Override
	public E root() {
		if(isEmpty())
			return null;
		else
			return nodeList.get(rootIndex);
	}
	@Override
	public List<E> siblings(E e) throws NodeNotFoundException {
		checkNode(e);
		E parent = parent(e);
		if(parent != null) {
			List<E> children = children(parent);
			children.remove(e);
			return children;
		}
		else
			return new ArrayList<E>();
	}
	@Override
	public int size() {
		return size;
	}
	@Override
	public Object[] toArray() {
		return getCurrentList().toArray();
	}
	@Override
	public <T> T[] toArray(T[] a) {
		return getCurrentList().toArray(a);
	}

	private void addChild(E child, int parentIndex, int childIndex) {
		nodeList.add(child);
		parentList.add(parentIndex);
		childrenArray.get(parentIndex)[childIndex] = nodeList.size() - 1;
		int[] children = new int[MAX_CHILDREN];
		Arrays.fill(children, -1);
		childrenArray.add(children);
		size++;
		int currentDepth = 2;
		while(parentIndex != 0)	{
			parentIndex = parentList.get(parentIndex);
			currentDepth++;
		}
		depth = Math.max(currentDepth, depth);
	}

	private void addRoot(E child) {
		nodeList.add(child);
		rootIndex = nodeList.size() - 1;
		parentList.add(-1);
		int[] children = new int[MAX_CHILDREN];
		Arrays.fill(children, -1);
		childrenArray.add(children);
		size++;
		depth++;
	}

	private void checkNode(E child) {
		if(child == null)
			throw new IllegalArgumentException("null nodes are not allowed");
	}

	private List<E> getCurrentList() {
		return inOrderTraversal();
	}
	private int getEmptySlot(int[] children) {
		for (int i = 0; i < children.length; i++)
			if(children[i] == -1)
				return i;
		return -1;
	}
	private List<E> inorderOrderTraversal(int nodeIndex, ArrayList<E> list) {
		int[] children = childrenArray.get(nodeIndex);
		if(children.length > 0)	{
			int i = 0;
			for(int len = (int)Math.ceil((double)children.length / 2); i < len; i++)
				if(children[i] > -1)
					inorderOrderTraversal(children[i], list);
			list.add(nodeList.get(nodeIndex));
			for(int len = children.length; i < len; i++)
				if(children[i] > -1)
					inorderOrderTraversal(children[i], list);
		}
		else
			list.add(nodeList.get(nodeIndex));
		return list;
	}
	private boolean isChildrenArrayEmpty(int[] children) {
		for (int i = 0; i < children.length; i++)
			if(children[i] != -1)
				return false;
		return true;
	}
	private List<E> levelOrderTraversal(ArrayList<E> list, LinkedList<Integer> queue) {
		while(!queue.isEmpty()) {
			list.add(nodeList.get(queue.getFirst()));
			for(int i : childrenArray.get(queue.getFirst()))
				if(i > -1)
					queue.add(i);
			queue.remove();
		}
		return list;
	}
	private List<E> postOrderTraversal(int nodeIndex, ArrayList<E> list) {
		for(int  i :  childrenArray.get(nodeIndex))
			if(i > -1)
				postOrderTraversal(i, list);
		if(nodeList.get(nodeIndex) != null)
			list.add(nodeList.get(nodeIndex));
		return list;
	}
	private List<E> preOrderTraversal(int nodeIndex, ArrayList<E> list) {
		if(nodeList.get(nodeIndex) != null)
			list.add(nodeList.get(nodeIndex));
		for(int i : childrenArray.get(nodeIndex))
			if(i > -1)
				preOrderTraversal(i, list);
		return list;
	}
	private int recalculateDepth(int index, int depth) {
		int childDepth = depth + 1;
		if(isChildrenArrayEmpty(childrenArray.get(index)))
			return childDepth;
		for(int i : childrenArray.get(index))
			if(i != -1)
				depth = Math.max(depth, recalculateDepth(i, childDepth));
		return depth;
	}
	@Override
	public String toString() {
		return getCurrentList().toString();
	}
	private E findParent(E root, E child) throws NodeNotFoundException {
		if(child.compareTo(root) > 0) {
			E right = right(root);
			if(right != null)
				return findParent(right, child);
			else
				return root;
		} else {
			E left = left(root);
			if(left != null)
				return findParent(left, child);
			else
				return root;
		}
	}
	/**
	 * @param parent
	 * @return the left child if present, or null otherwise
	 * @throws NodeNotFoundException
	 */
	public E left(E parent) throws NodeNotFoundException {
		return child(parent, 0);
	}
	/**
	 * @param parent
	 * @return the right child if present, or null otherwise
	 * @throws NodeNotFoundException
	 */
	public E right(E parent) throws NodeNotFoundException {
		return child(parent, 1);
	}
}
