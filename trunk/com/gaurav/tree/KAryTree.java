package com.gaurav.tree;

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
import java.util.List;


/**
 * This tree allows addition of at most k-children to any parent and maintains insertion order among children
 * @author Gaurav Saxena
 * @param <E>
 */
public class KAryTree<E> extends ArrayListTree<E> {
	private static final long serialVersionUID = 1783745590577898841L;
	private int k;
	public KAryTree(int k) {
		this.k = k;
	}
	@Override
	public boolean add(E parent, E child) throws NodeNotFoundException {
		if(children(parent).size() < k)
			return add(parent, child);
		else
			throw new IllegalArgumentException("Cannot add more than " + k +" children to a parent");
	}
	public E child(E e, int index) throws NodeNotFoundException
	{
		List<E> children;
		if(index > k)
			throw new IndexOutOfBoundsException(index + "is more than " + k);
		else if(index > (children = children(e)).size())
			throw new NodeNotFoundException("Index passed is more than " + children.size() + " children of the node");
		else
			return children.get(index);
	}
}
