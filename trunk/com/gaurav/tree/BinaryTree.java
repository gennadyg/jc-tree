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

import java.util.List;

/**
 * This tree allows addition of at most 2 children to a parent and maintains insertion order among children. The 
 * children are added sequentially i.e. the first child is added as left one and the next as right one. To control
 * the order in which children are inserted use {@link NumberedTree}
 * @author Gaurav Saxena
 * @param <E>
 */
public class BinaryTree<E> extends KAryTree<E> {
	public BinaryTree()
	{
		super(2);
	}
	/**
	 * @param e parent node
	 * @return left child of e, if e contains at least 1 child, null otherwise
	 * @throws NodeNotFoundException in case e is not present in the tree or has no children
	 */
	public E left(E e) throws NodeNotFoundException
	{
		List<E> children;
		if((children = children(e)).isEmpty())
			return null;
		else
			return children.get(0);
	}
	/**
	 * @param e parent node
	 * @return right child of e, if there are 2 children of e, null otherwise
	 * @throws NodeNotFoundException in case e is not present in the tree or has less than 2 children
	 */
	public E right(E e) throws NodeNotFoundException
	{
		List<E> children;
		if((children = children(e)).isEmpty() || children.size() == 1)
			return null;
		else
			return children.get(1);
	}
}
