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

import java.util.Collection;

/**
 * The tree interface is a general interface for all types an their implementations. It extends {@link Collection}
 * and thus all its methods.
 * {@link Collection} add(Object) and addAll(Collection c) methods always adds the first element as root and later
 * ones as children of the root. It is better to use methods of {@link Tree} interface - add(E, E) and 
 * add(E, Collection&lt;E&gt; c) as give better control on the tree structure.
 * This interface doesn't guarantee any order to be maintained among the children of the tree
 * @author Gaurav Saxena
 *
 * @param <E> object type contained in the tree
 */
public interface Tree<E> extends Collection<E>{
	/**
	 * @param e child object
	 * @return the parent object. Null if e is root
	 * @throws NodeNotFoundException if e is not found
	 */
	public E parent(E e) throws NodeNotFoundException;
	/**
	 * @param e parent object
	 * @return collection of children
	 * @throws NodeNotFoundException if e is not found
	 */
	public Collection<E> children(E e) throws NodeNotFoundException;
	/**
	 * @return collection of children arranged as preOrderTraversal of underlying tree
	 */
	public Collection<E> preOrderTraversal();
	/**
	 * @return collection of children arranged as postOrderTraversal of underlying tree
	 */
	public Collection<E> postOrderTraversal();
	/**
	 * @return collection of children arranged as inorderOrderTraversal of underlying tree
	 */
	public Collection<E> inorderOrderTraversal();
	/**
	 * @return collection of children arranged as levelOrderTraversal of underlying tree
	 */
	public Collection<E> levelOrderTraversal();
	
	/**
	 * @return all the leaves of the tree i.e. those nodes which do not have children
	 */
	public Collection<E> leaves();
	/**
	 * @return all the siblings of the node i.e. those nodes which have the same parent as the parameter
	 * @throws NodeNotFoundException 
	 */
	public Collection<E> siblings(E e) throws NodeNotFoundException;
	/**
	 * @param parent object to which child object needs to be added. Parent is allowed to be null
	 * only when there are no other nodes present in the tree. In that case, the child will be added as root
	 * @param child object 
	 * @return true if parent was found and child is not already in the tree, otherwise false
	 * @throws NodeNotFoundException if parent is not found
	 */
	public boolean add(E parent, E child) throws NodeNotFoundException;
	/**
	 * Uses add(E parent, E e) to add all the objects present in the collection
	 * @param parent object to which child object needs to be added. Parent is allowed to be null
	 * only when there are no other nodes present in the tree. In that case, the child will be added as root 
	 * @param c collection of children to be added to parent
	 * @return true if collection changed as a result of the operation, otherwise false
	 */
	public boolean addAll(E parent, Collection<? extends E> c);
	/**
	 * @return maximum depth of the tree
	 */
	public int depth();
	/**
	 * @return the root node or null if tree is empty
	 */
	public E root();
}
