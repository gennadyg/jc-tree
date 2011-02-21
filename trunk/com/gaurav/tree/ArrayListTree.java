package com.gaurav.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ArrayListTree<E> implements Tree<E>, Serializable{
	private static final long serialVersionUID = 9188932537753945512L;
	private ArrayList<E> nodeList = new ArrayList<E>();
	private ArrayList<Integer> parentList = new ArrayList<Integer>();
	private ArrayList<ArrayList<Integer>> childrenList = new ArrayList<ArrayList<Integer>>();
	private int size = 0;
	
	public List<E> preOrderTraversal()
	{
		return preOrderTraversal(0, new ArrayList<E>());
	}
	private List<E> preOrderTraversal(int nodeIndex, ArrayList<E> list) {
		if(nodeList.get(nodeIndex) != null)
			list.add(nodeList.get(nodeIndex));
		ArrayList<Integer> children = childrenList.get(nodeIndex);
		for(int i = 0; i < children.size(); i++)
			preOrderTraversal(children.get(i), list);
		return list;
	}
	public List<E> postOrderTraversal()
	{
		return postOrderTraversal(0, new ArrayList<E>());
	}
	private List<E> postOrderTraversal(int nodeIndex, ArrayList<E> list) {
		ArrayList<Integer> children = childrenList.get(nodeIndex);
		for(int i = 0; i < children.size(); i++)
			postOrderTraversal(children.get(i), list);
		if(nodeList.get(nodeIndex) != null)
			list.add(nodeList.get(nodeIndex));
		return list;
	}
	public List<E> inorderOrderTraversal()
	{
		return inorderOrderTraversal(0, new ArrayList<E>());
	}
	private List<E> inorderOrderTraversal(int nodeIndex, ArrayList<E> list) {
		ArrayList<Integer> children = childrenList.get(nodeIndex);
		if(children.size() > 0)
		{
			for(int i = 0; i < children.size(); i++)
			{
				if(i >= children.size() / 2 && nodeList.get(nodeIndex) != null)
					list.add(nodeList.get(nodeIndex));
				inorderOrderTraversal(children.get(i), list);
			}
		}
		else
			list.add(nodeList.get(nodeIndex));
		return list;
	}
	public List<E> levelOrderTraversal()
	{
		if(nodeList.isEmpty())
			return new ArrayList<E>();
		else
		{
			LinkedList<Integer> queue = new LinkedList<Integer>();
			queue.add(0);
			return levelOrderTraversal(new ArrayList<E>(), queue);
		}
	}
	private List<E> levelOrderTraversal(ArrayList<E> list, LinkedList<Integer> queue) {
		if(!queue.isEmpty())
		{
			list.add(nodeList.get(queue.getFirst()));
			ArrayList<Integer> children = childrenList.get(queue.getFirst());
			for(int i = 0; i < children.size(); i++)
				queue.add(children.get(i));
			queue.remove();
			levelOrderTraversal(list, queue);
		}
		return list;
	}
	public List<E> children(E e)
	{
		if(e == null)
			return new ArrayList<E>();
		int index = nodeList.indexOf(e);
		if(index > -1)
		{
			ArrayList<E> children = new ArrayList<E>();
			for (Iterator<Integer> iterator = childrenList.get(index).iterator(); iterator.hasNext();) {
				children.add(nodeList.get(iterator.next()));
			}
			return children;
		}
		else
			return null;
	}
	@Override
	public Collection<E> leaves() {
		if(nodeList.isEmpty())
			return new ArrayList<E>();
		else
		{
			LinkedList<Integer> queue = new LinkedList<Integer>();
			queue.add(0);
			return leaves(new ArrayList<E>(), queue);
		}
	}
	private Collection<E> leaves(ArrayList<E> list, LinkedList<Integer> queue) {
		if(!queue.isEmpty())
		{
			ArrayList<Integer> children = childrenList.get(queue.getFirst());
			if(children.isEmpty())
				list.add(nodeList.get(queue.getFirst()));
			for(int i = 0; i < children.size(); i++)
				queue.add(children.get(i));
			queue.remove();
			leaves(list, queue);
		}
		return list;
	}
	public E parent(E e)
	{
		int index = nodeList.indexOf(e);
		if(index > -1)
			return nodeList.get(parentList.get(index));
		else
			return null;
	}
	public boolean add(E parent, E child) {
		if(parent == null)
		{
			if(nodeList.size() > 0)
				throw new IllegalArgumentException("parent cannot be null except for root element");
			else
			{
				nodeList.add(child);
				parentList.add(-1);
				childrenList.add(new ArrayList<Integer>());
				size++;
				return true;
			}
		}
		int	parentIndex = nodeList.indexOf(parent);
		if(parentIndex > -1 && nodeList.indexOf(child) == -1)
		{
			nodeList.add(child);
			parentList.add(parentIndex);
			childrenList.get(parentIndex).add(nodeList.size() - 1);
			childrenList.add(new ArrayList<Integer>());
			size++;
			return true;
		}
		return false;
	}
	@Override
	public boolean add(E e) {
		if(nodeList.size() > 0)
			return add(nodeList.get(0), e);
		else
			return add(null, e);
	}
	public boolean addAll(E parent, Collection<? extends E> c) {
		for (Iterator<? extends E> iterator = c.iterator(); iterator.hasNext();)
			add(parent, iterator.next());
		return true;
	}
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean retVal = false;
		for (Iterator<? extends E> iterator = c.iterator(); iterator.hasNext();)
			retVal |= add(iterator.next());
		return retVal;
	}

	@Override
	public void clear() {
		nodeList.clear();
		parentList.clear();
		childrenList.clear();
		size = 0;
	}

	@Override
	public boolean contains(Object o) {
		if(o == null)
			return false;
		else
			return nodeList.indexOf(o) > -1;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return nodeList.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return size() > 0;
	}

	@Override
	public Iterator<E> iterator() {
		return getCurrentList().iterator();
	}

	private List<E> getCurrentList() {
		List<E> nodes = new ArrayList<E>();
		for(int i = 0;i < nodes.size(); i++)
			if(nodeList.get(i) != null)
				nodes.add(nodeList.get(i));
		return nodes;
	}
	@Override
	public boolean remove(Object o) {
		int i = nodeList.indexOf(o);
		if(i > -1)
			return remove(i);
		else
			return false;
	}
	private boolean remove(int index) {
		if(index > -1)
		{
			Integer parentIndex = parentList.set(index, -1);
			childrenList.get(parentIndex).remove(Integer.valueOf(index));
			nodeList.set(index, null);
			size--;
			ArrayList<Integer> children = childrenList.get(index);
			for (int j = 0; j < children.size(); j++) 
				remove(children.get(j).intValue());
			childrenList.get(index).clear();
			return true;
		}
		else
			return false;
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
		boolean retVal = false;
		for (Iterator<E> iterator = nodeList.iterator(); iterator.hasNext();) {
			E object = iterator.next();
			if(!c.contains(object))
				retVal |= remove(object);
		}
		return retVal;
	}

	@Override
	public Object[] toArray() {
		return getCurrentList().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return getCurrentList().toArray(a);
	}

	@Override
	public int size() {
		return size;
	}
}