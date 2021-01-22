package datastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is a generic BST class
 * - it has a default constructor BST<E>();
 * - it has a constructor BST<E>(E[] Collection);
 *
 * @author Evelyn Wu
 * @version 12/01/2019
 *
 */
public class BST<E extends Comparable<E>> extends Object implements Iterable<E>, Cloneable {
	/**
	 * This is a nested class that provides nodes for the LinikedList
	 * - it has a default constructor Node<E>();
	 * - it has a constructor Node<E>(E data);
	 *
	 * @author Evelyn Wu
	 *
	 */
	private static class Node <E> {
		/**
		 * Left and right child of the Node
		 */
		Node<E> left, right;

		/**
		 * height of the Node
		 */
		int height = 1;

		/**
		 * data stored in the Node
		 */
		E data;

		/**
		 * Constructs a new Node that stores the data
		 * @param data - data to store
		 */
		Node(E data) {
			left = null;
			right = null;
			this.data = data;
		}

		/**
		 * @return a string representation of this Node
		 */
		@Override
		public String toString() {
		    return data.toString();
		}

	}

	/**
	 * The first Node of the BST
	 */
	private Node<E> root;

	/**
	 * The number of elements stored in this BST
	 */
	private int size;

	/**
	 * Default constructor of a BST object
	 */
	public BST() {
		root = null;
		size = 0;
	}

	/**
	 * Consructs a new BST object stores the elements in a collection
	 * @param collection - collection with elements to store
	 * @throw NullPointerException - if the specified collection is null or
	 * 	if any element of the collection is null
	 */
	public BST(E[] collection) {
		for ( E element : collection ) {
			try {
				add(element);
			} catch ( NullPointerException e ) {
				continue;
			}
		}
	}

	/**
	 * get the first Node of this BST object
	 * @return root of this BST
	 */
	public Node<E> root() {
		return root;
	}

	/**
	 * Returns the height of this tree. The height of a leaf is 1.
	 * The height of the tree is the height of its root node
	 * @return the height of this tree or zero if the tree is empty
	 */
	public int height() {
		return ( root == null ) ? 0 : root.height;
	}

	/**
	 * updates the height of a given Node
	 * @param node - Node to be updated
	 */
	private void updateHeight(Node<E> node) {
		if ( node.left == null && node.right == null )
			node.height = 1;

		else if  ( node.left == null )
			node.height = node.right.height + 1;

		else if ( node.right == null )
			node.height = node.left.height + 1;

		else
			node.height = Math.max(node.right.height, node.left.height) + 1;
	}

	/**
	 * Adds the specified element to this set if it is not already present
	 * @param data - data to be added to this set
	 * @return true if this set did not already contain the specified element
	 * @throw NullPointerException - if the specified element is null and this set
	 * 	uses natural ordering, or its comparator does not permit null elements
	 */
	public boolean add(E data) {
		if ( data == null )
			throw new NullPointerException("Null data");

		int originalSize = size;
		root = add(root, data);

		if( size > originalSize ) return true;
		return false;
	}

	/**
	 * Adds the specified element recursively
	 * @param node - current Node
	 * @param data - data to add
	 * @return the root Node
	 */
	private Node<E> add(Node<E> node, E data) {
		if ( node == null ) {
			node = new Node<E>(data);
			size += 1;
		}
		int comp = data.compareTo(node.data);
		if ( comp == 0 )
			return node;
		else {
			if ( comp > 0 ) {
				node.right = add(node.right, data);
			}
			if ( comp < 0 ) {
				node.left = add(node.left, data);
			}
		}
		updateHeight(node);
		return node;
	}

	/**
	 * Adds all of the elements in the specified collection to this tree.
	 * @param collection - collection containing elements to be added to this set
	 * @return true if this set changed as a result of the call
	 * @throw NullPointerException - if the specified collection is null or if any
	 * 	element of the collection is null
	 */
	public boolean addAll(Collection<? extends E> collection) {
		if ( collection == null )
			throw new NullPointerException("Null collection");

		int originalSize = size;

		for ( E data : collection ) {
			try {
				add(data);
			} catch ( NullPointerException e ) {
				throw new NullPointerException("Null element in the collection");
			}
		}
		return ( size > originalSize );
	}


	@SuppressWarnings("unchecked")
	public int addNodes() {
		return addNodes((Node<Integer>) root, 0);
	}

	private int addNodes(Node<Integer> node, int sum) {
		if ( node == null || (node.left == null&&node.right == null)) {
			//System.out.println("SumHere: " + sum);
			return sum;
		}

		return node.data + addNodes(node.left, sum) + addNodes(node.right, sum);

	}

	/**
	 * stores whether the set is changed as a result of the call of remove();
	 */
	private boolean found;

	/**
	 * Removes the specified element from this tree if it is present.
	 * @param o - object to be removed from this set, if present
	 * @return true if this set contained the specified element
	 * @throw ClassCastException - if the specified object cannot be compared
	 * 	with the elements currently in this tree
	 * @throw NullPointerException - if the specified element is null
	 */
	@SuppressWarnings("unchecked")
	public boolean remove(Object o) {
		found = false;
		if ( o == null )
			throw new NullPointerException("Null element indicated");
		//E originalRoot = root.data;
		E data;
		try {
			data = (E) o;
			root = remove(root, data);
			size--;
		} catch ( ClassCastException e ) {
			throw new ClassCastException("Cannot cast the input object");
		}

		return found;
	}

	/**
	 * Removes the specified element from this tree recursively
	 * @param node - current Node
	 * @param data - data to remove
	 * @return root Node
	 */
	private Node<E> remove(Node<E> node, E data) {
		int comp;
		//not found
		if ( node == null ) {
			found = false;
			size ++;
		}

		//go left and right accordingly
		else if ( (comp = data.compareTo(node.data)) < 0 ) {
			node.left = remove(node.left, data);
			updateHeight(node);
		}
		else if ( comp > 0 ) {
			node.right = remove(node.right, data);
			updateHeight(node);
		}

		//found it, remove
		else {
			node = removeNode(node);
			found = true;
		}
		return node;
	}

	/**
	 * removes a Node from a set recursively
	 * @param node - node to remove
	 * @return node to replace
	 */
	private Node<E> removeNode(Node<E> node) {
		E data;
		//check if the node to remove has children
		if ( node.left == null ) {
			return node.right;
		}
		else if ( node.right == null ) {
			return node.left;
		}

		//if the node has 2 children, replace its data with the left largest
		else {
			data = getLeftLargest(node);
			node.data = data;
			node.left = remove(node.left, data);
			return node;
		}
	}

	/**
	 * Removes all of the elements from this set.
	 */
	public void clear() {
		root = null;
		size = 0;
	}

	/**
	 * Returns true if this set contains the specified element.
	 * @param o - object to be checked for containment in this set
	 * @return true if this set contains the specified element
	 * @throw ClassCastException - if the specified object cannot be compared with
	 * 	the elements currently in the set
	 * @throw NullPointerException - if the specified element is null and this set
	 * 	uses natural ordering, or its comparator does not permit null elements
	 */
	public boolean contains(Object o) {
		if ( o == null )
			throw new NullPointerException("Null data");

		Node<E> current = root;

		while ( current != null ) {
			try {
				@SuppressWarnings("unchecked")
				int comp = current.data.compareTo((E) o);

				if ( comp == 0 ) return true;

				if ( comp < 0 )
					current = current.right;

			    else
			    	current = current.left;
			} catch ( ClassCastException e ) {
				throw new ClassCastException(
						"The specified object cannot be compared with elements in this set");
			}
		}
		return false;
	}

	/**
	 * Returns true if this collection contains all of the elements in the specified collection.
	 * @param c - collection to be checked for containment in this tree
	 * @return true if this tree contains all of the elements in the specified collection
	 * @throw NullPointerException - if the specified collection contains one or more null elements
	 * 	and this collection does not permit null elements, or if the specified collection is null.
	 */
	public boolean containsAll(Collection<?> c) {
		if ( c == null )
			throw new NullPointerException("Null collection");

		for ( Object data : c ) {
			if ( !contains(data) )
				return false;
		}
		return true;
	}

	/**
	 * Returns the number of elements in this tree
	 * @return the number of elements in this tree
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns true if this set contains no elements
	 * @return true if this set contains no elements
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * Returns the element at the specified position in this tree
	 * @param index - index of the element to return
	 * @return the element at the specified position in this tree
	 * @throw IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())
	 */
	public E get(int index) {
		if ( index < 0 || index > size - 1 )
			throw new IndexOutOfBoundsException("Index out of bound");

		Iterator<E> iter = iterator();
		int count = -1;
		E data = root.data;

		while ( count < index ) {
			data = iter.next();
			count ++;
		}

		return data;
	}

	/**
	 * Returns a collection whose elements range from fromElement, inclusive,
	 * 	to toElement, inclusive
	 * @param fromElement - low endpoint (inclusive) of the returned collection
	 * @param toElement - high endpoint (inclusive) of the returned collection
	 * @return a collection containing a portion of this tree whose elements range
	 * 	from fromElement, inclusive, to toElement, inclusive
	 * @throw NullPointerException - if fromElement or toElement is null
	 * @throw IllegalArgumentException - if fromElement is greater than toElement
	 */
	public ArrayList<E> getRange(E fromElement, E toElement) {
		if ( fromElement == null || toElement == null )
			throw new NullPointerException("Null element indicated");

		if ( fromElement.compareTo(toElement) > 0 )
			throw new IllegalArgumentException("fromElement > toElement");

		ArrayList<E> list = new ArrayList<E>();
		for ( E data : this ) {
			int comp1 = data.compareTo(fromElement);
			int comp2 = data.compareTo(toElement);
			if ( comp1 >= 0 && comp2 <= 0 )
				list.add(data);
		}
		return list;
	}

	/**
	 * Returns the Node stores smaller data
	 * @param a - Node to compare
	 * @param b - Node to compare
	 * @return the Node stores smaller data
	 */
	private Node<E> Min(Node<E> a, Node<E> b) {
		if ( a == null ) return b;
		if ( b == null ) return a;
		int comp = a.data.compareTo(b.data);
		if ( comp > 0 ) return b;
		else return a;
	}

	/**
	 * Returns the Node stores larger data
	 * @param a - Node to compare
	 * @param b - Node to compare
	 * @return the Node stores larger data
	 */
	private Node<E> Max(Node<E> a, Node<E> b) {
		if ( a == null ) return b;
		if ( b == null ) return a;
		int comp = a.data.compareTo(b.data);
		if ( comp > 0 ) return a;
		else return b;
	}

	/**
	 * Returns the least element in this tree greater than or equal to
	 * 	the given element, or null if there is no such element.
	 * @param data - the value to match
	 * @return the least element greater than or equal to e, or null if
	 * 	there is no such element
	 * @throw NullPointerException - if the specified element is null
	 */
	public E ceiling(E data) {
		if ( data == null )
			throw new NullPointerException("Null data");

	    Node<E> current = root;
	    Node<E> ceiling = null;
	    while( current != null ) {
	        int comp = data.compareTo(current.data);
	        if( comp == 0 )
	        	return current.data;
	        else if(comp > 0) {
	        	current = current.right;
	        }
	        else if(comp < 0) {
	        	ceiling = Min(current, ceiling);
	        	current = current.left;
	        }
	    }
	    try {
	    	return ceiling.data;
	    } catch ( NullPointerException e ) {
	    	return null;
	    }
	}

	/**
	 * Returns the greatest element in this set less than or equal to
	 * the given element, or null if there is no such element
	 * @param data - the value to match
	 * @return the greatest element less than or equal to e, or null if
	 * 	there is no such element
	 * @throw NullPointerException - if the specified element is null
	 */
	public E floor(E data) {
		if ( data == null )
			throw new NullPointerException("Null data");

	    Node<E> current = root;
	    Node<E> floor = null;
	    while( current != null ) {
	        int comp = data.compareTo(current.data);
	        if( comp == 0 )
	        	return current.data;
	        else if(comp < 0) {
	        	current = current.left;
	        }
	        else if(comp > 0) {
	        	floor = Max(current, floor);
	        	current = current.right;
	        }
	    }
	    try {
	    	return floor.data;
	    } catch ( NullPointerException e ) {
	    	return null;
	    }
	}

	/**
	 * Compares the specified object with this tree for equality
	 * @param o - object to be compared for equality with this tree
	 * @return true if the specified object is equal to this tree
	 */
	@Override
	public boolean equals(Object o) {
		if ( this == o )
			return true;

		if ( o == null )
			return false;

		if ( !(o instanceof BST ) )
			return false;

		@SuppressWarnings("unchecked")
		BST<E> tree = (BST<E>) o;
		if ( tree.size != size )
			return false;

		Iter iter = new Iter();
		ArrayList<E> nodes = iter.getNodes();
		if ( !tree.containsAll(nodes) )
			return false;

		return true;
	}

	/**
	 * Returns the first (lowest) element currently in this tree
	 * @return the first (lowest) element currently in this tree
	 * @throw NoSuchElementException - if this set is empty
	 */
	public E first() {
		if ( root == null )
			throw new NoSuchElementException("Empty tree");

		Node<E> current = root;
		while ( current.left != null )
			current = current.left;
		return current.data;
	}

	/**
	 * Returns the last (highest) element currently in this tree
	 * @return the last (highest) element currently in this tree
	 * @throw NoSuchElementException - if this set is empty
	 */
	public E last() {
		if ( root == null )
			throw new NoSuchElementException("Empty tree");

		Node<E> current = root;
		while ( current.right != null )
			current = current.right;
		return current.data;
	}

	/**
	 * Returns the greatest element in this set strictly less than the given element,
	 * 	or null if there is no such element
	 * @param data - the value to match
	 * @return the greatest element less than e, or null if there is no such element
	 * @throw ClassCastException - if the specified element cannot be compared with the
	 * 	elements currently in the set
	 * @throw NullPointerException - if the specified element is null
	 */
	public  E lower(E data) {
		if ( data == null )
			throw new NullPointerException("Null data");

	    Node<E> current = root;
	    Node<E> floor = null;
	    while( current != null ) {
	        int comp = data.compareTo(current.data);
	        if(comp <= 0) {
	        	current = current.left;
	        }
	        else if(comp > 0) {
	        	floor = Max(current, floor);
	        	current = current.right;
	        }
	    }
	    try {
	    	return floor.data;
	    } catch ( NullPointerException e ) {
	    	return null;
	    }
	}

	/**
	 * Returns the least element in this tree strictly greater than the given element,
	 * or null if there is no such element
	 * @param data - the value to match
	 * @return the least element greater than e, or null if there is no such element
	 * @throw NullPointerException - if the specified element is null
	 */
	public  E higher(E data) {
		if ( data == null )
			throw new NullPointerException("Null data");

	    Node<E> current = root;
	    Node<E> ceiling = null;
	    while( current != null ) {
	        int comp = data.compareTo(current.data);
	        if(comp >= 0) {
	        	current = current.right;
	        }
	        else if(comp < 0) {
	        	ceiling = Min(current, ceiling);
	        	current = current.left;
	        }
	    }
	    try {
	    	return ceiling.data;
	    } catch ( NullPointerException e ) {
	    	return null;
	    }
	}

	/**
	 * Get the largest element on the left of the root
	 * @param node - root Node
	 * @return the largest element on the left of the root
	 */
	private E getLeftLargest(Node<E> node) {
		if ( node.left == null ) return node.data;
		else {
			Node<E> current = node.left;
			while ( current.right != null )
				current = current.right;
			return current.data;
		}
	}

	/**
	 * Returns a shallow copy of this tree instance (i.e., the elements
	 * 	themselves are not cloned but the nodes are)
	 * @return a shallow copy of this tree
	 */
	public BST<E> clone() {
		BST<E> cloned = new BST<E>();
		Iterator<E> preiter = preorderIterator();
		while ( preiter.hasNext() ) {
			E element = preiter.next();
			cloned.add(element);
		}
		return cloned;
	}

	/**
	 * This function returns an array containing all the elements returned by this tree's
	 * 	iterator, in the same order, stored in consecutive elements of the array, starting
	 * 	with index 0.
	 * @return an array, whose runtime component type is Object, containing all of the
	 * 	elements in this tree
	 */
	public Object[] toArray() {
		Object[] array = new Object[size];
		for ( int i = 0; i < size; i ++ ) {
			array[i] = get(i);
		}
		return array;
	}

	/**
	 * Returns a string representation of this tree
	 * @return a string representation of this collection
	 */
	@Override
	public String toString() {
		if ( root == null )
			return "[]";
		StringBuilder tree = new StringBuilder();
		tree.append("[");
		int counter = 0;
		for ( E data : this ) {
			if ( counter < size - 1 ) {
				tree.append(String.valueOf(data));
				tree.append(", ");
			} else {
				tree.append(String.valueOf(data));
			}
			counter ++;
		}
		tree.append("]");
		return tree.toString();
	}

	/**
	 * Produces tree like string representation of this tree. Returns a string
	 * 	representation of this tree in a tree-like format
	 * @return string containing tree-like representation of this tree
	 */
	public String toStringTreeFormat() {
		if ( root == null ) {
			return "null";
		}
		StringBuilder tree = new StringBuilder();
		preOrderPrint(root, 0, tree);
		return tree.toString();
	}

	/**
	 * Produces tree like string representation of this tree
	 */
	private void preOrderPrint(Node<E> root, int level, StringBuilder tree) {
		if ( root != null ) {
			String spaces = "\n";
			if ( level > 0 ) {
				for ( int i = 0; i < level - 1; i++ )
					spaces += "   ";
				spaces += "|--";
			}
			tree.append(spaces);
			tree.append(root.data);
			preOrderPrint(root.left, level + 1, tree);
			preOrderPrint(root.right, level + 1, tree);
		}

		else {
			String spaces = "\n";
			if ( level > 0 ) {
				for ( int i = 0; i < level - 1; i++ )
					spaces += "   ";
				spaces += "|--";
			}
			tree.append(spaces);
			tree.append("null");
		}
	}

	/**
	 * An inorder iterator of a BST object
	 */
	class Iter implements Iterator<E> {
		ArrayList<E> nodes;
		int index;

		/**
		 * default constructor
		 */
	    public Iter() {
	        nodes = new ArrayList<E>();
	        index = -1;
	        inorder(root);
	    }

	    /**
	     * get an ArrayList stores all the elements in this BST in order
	     * @return an ArrayList stores all the elements in this BST in order
	     */
	    public ArrayList<E> getNodes() {
	    	return nodes;
	    }

	    /**
	     * add all the elements in the BST to an ArrayList in order
	     * @param root - the root Node
	     */
	    private void inorder(Node<E> root) {
	    	if ( root == null ) {
	    		return;
	    	}

	    	inorder(root.left);
	    	nodes.add(root.data);
	    	inorder(root.right);
	    }

	    /**
		 * get the next element
		 * @return the next element
		 * @throw NoSuchElementException if the current element is null
		 */
	    @Override
		public E next() {
	    	if (root == null) throw new
				NoSuchElementException("reached the end of this collection");
			return nodes.get( ++ index );
	    }

		/**
		 * checks if the list has the next element
		 * @return true if has next
		 */
	    @Override
	    public boolean hasNext() {
			return index + 1 < nodes.size();
	    }
	}

	/**
	 * Returns an iterator over the elements in this tree in ascending order
	 * @return an iterator over the elements in this set in ascending order
	 */
	@Override
	public Iterator<E> iterator() {
		Iterator<E> itr = new Iter();
		return itr;
	}

	/**
	 * An preorder iterator of a BST object
	 */
	class PreIter implements Iterator<E> {
		ArrayList<E> nodes;
		int index;

		/**
		 * default constructor
		 */
	    public PreIter() {
	        nodes = new ArrayList<E>();
	        index = -1;
	        preorder(root);
	    }

	    /**
	     * add all the elements in the BST to an ArrayList in preorder
	     * @param root - the root Node
	     */
	    private void preorder(Node<E> root) {
	    	if ( root == null ) {
	    		return;
	    	}

	    	nodes.add(root.data);
	    	preorder(root.left);
	    	preorder(root.right);

	    }

	    /**
		 * get the next element
		 * @return the next element
		 * @throw NoSuchElementException if the current element is null
		 */
	    @Override
		public E next() {
	    	if (root == null) throw new
				NoSuchElementException("reached the end of this collection");
			return nodes.get( ++ index );
	    }

	    /**
		 * checks if the list has the next element
		 * @return true if has next
		 */
	    @Override
	    public boolean hasNext() {
			return index + 1 < nodes.size();
	    }
	}

	/**
	 * Returns an iterator over the elements in this tree in order of the preorder traversal
	 * @return an iterator over the elements in this tree in order of the preorder traversal
	 */
	public Iterator<E> preorderIterator() {
		Iterator<E> preitr = new PreIter();
		return preitr;
	}

	/**
	 * An postorder iterator of a BST object
	 */
	class PostIter implements Iterator<E> {
		ArrayList<E> nodes;
		int index;

		/**
		 * default constructor
		 */
	    public PostIter() {
	        nodes = new ArrayList<E>();
	        index = -1;
	        postorder(root);
	    }

	    /**
	     * add all the elements in the BST to an ArrayList in postorder
	     * @param root - the root Node
	     */
	    private void postorder(Node<E> root) {
	    	if ( root == null ) {
	    		return;
	    	}

	    	postorder(root.left);
	    	postorder(root.right);
	    	nodes.add(root.data);
	    }

	    /**
		 * get the next element
		 * @return the next element
		 * @throw NoSuchElementException if the current element is null
		 */
	    @Override
		public E next() {
	    	if (root == null) throw new
				NoSuchElementException("reached the end of this collection");
			return nodes.get( ++ index );
	    }

	    /**
		 * checks if the list has the next element
		 * @return true if has next
		 */
	    @Override
	    public boolean hasNext() {
			return index + 1 < nodes.size();
	    }
	}

	/**
	 * Returns an iterator over the elements in this tree in order of the postorder traversal
	 * @return an iterator over the elements in this tree in order of the postorder traversal
	 */
	public Iterator<E> postorderIterator() {
		Iterator<E> postitr = new PostIter();
		return postitr;
	}

}
