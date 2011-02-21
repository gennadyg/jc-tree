package com.gaurav.tree;

import java.util.Collection;

/**
 * @author Gaurav
 *
 * @param <T> a collection of type E
 * @param <E> object type contained in the tree
 */
public interface Tree<E> extends Collection<E>{
	/**
	 * @param e child object
	 * @return the parent object
	 */
	public E parent(E e);
	/**
	 * @param e parent object
	 * @return collection of children
	 */
	public Collection<E> children(E e);
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
	
	public Collection<E> leaves();
	/**
	 * @param parent object to which child object needs to be added. Parent is allowed to be null
	 * only when there are no other nodes present in the tree. In that case, the child will be added as root
	 * @param child object 
	 * @return true if parent was found and child is not already in the tree, otherwise false
	 */
	public boolean add(E parent, E child);
	/**
	 * Uses add(E parent, E e) to add all the objects present in the collection
	 * @param parent
	 * @param c
	 * @return
	 */
	public boolean addAll(E parent, Collection<? extends E> c);
}
