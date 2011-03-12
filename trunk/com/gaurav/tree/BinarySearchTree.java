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


public class BinarySearchTree<E extends Comparable<E>> extends ArrayTree<E> implements Cloneable{
	
	public BinarySearchTree() {
		super(2);
	}
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
	@Override
	public boolean add(E child)
	{
		try {
			if(size() == 0)
				return super.add(null, child);
			else
			{
				E parent = findParent(root(), child);
				return super.add(parent, child, parent.compareTo(child) > 0 ? 0 : 1);
			}
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	private E findParent(E root, E child) throws NodeNotFoundException
	{
		if(child.compareTo(root) > 0)
		{
			E right = right(root);
			if(right != null)
				return findParent(right, child);
			else
				return root;
		}
		else
		{
			E left = left(root);
			if(left != null)
				return findParent(left, child);
			else
				return root;
		}
	}
	public E left(E parent) throws NodeNotFoundException {
		return child(parent, 0);
	}
	public E right(E parent) throws NodeNotFoundException {
		return child(parent, 1);
	}
}
