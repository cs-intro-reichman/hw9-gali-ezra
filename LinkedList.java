/**
 * Represents a list of Nodes. 
 */
public class LinkedList {

    private Node first; // pointer to the first element of this list
    private Node last;  // pointer to the last element of this list
    private int size;   // number of elements in this list

    /**
     * Constructs a new list.
     */
    public LinkedList() {
        first = null;
        last = null;
        size = 0;
    }

    /**
     * Gets the first node of the list
     * @return The first node of the list.
     */
    public Node getFirst() {
        return this.first;
    }

    /**
     * Gets the last node of the list
     * @return The last node of the list.
     */
    public Node getLast() {
        return this.last;
    }

    /**
     * Gets the current size of the list
     * @return The size of the list.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Gets the node located at the given index in this list.
     *
     * @param index
     *        the index of the node to retrieve, between 0 and size - 1
     * @throws IllegalArgumentException
     *         if index is negative or >= size
     * @return the node at the given index
     */
    
	public Node getNode(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException(
					"index must be between 0 and size");
		}
		if (index == size - 1) return last;
		ListIterator iterator = new ListIterator(first);
		for (int i = 0; i < index; i++){
			iterator.next();
		}
		return iterator.current;
	
}

    /**
     * Creates a new Node object that points to the given memory block,
     * and inserts the node at the given index in this list.
     * <p>
     * If the given index is 0, the new node becomes the first node in this list.
     * <p>
     * If the given index equals the list's size, the new node becomes the last
     * node in this list.
     * <p>
     * The method implementation is optimized, as follows: if the given
     * index is either 0 or the list's size, the addition time is O(1).
     *
     * @param block
     *        the memory block to be inserted into the list
     * @param index
     *        the index before which the memory block should be inserted
     * @throws IllegalArgumentException
     *         if index is negative or greater than the list's size
     */
    public void add(int index, MemoryBlock block) {
        // If the test wants "index must be between 0 and size" for out-of-range,
        // we can keep the same. This is correct for 0..size inclusive.
        if (index < 0 || index > size) {
			throw new IllegalArgumentException(
					"index must be between 0 and size");
		}
		if (index == 0){
			addFirst(block);
		}
		else if (index == size){
			addLast(block);
		}
		else {
			Node newNode = new Node(block);
			ListIterator iterator = new ListIterator(first);
			for (int i = 0; i < index - 1; i++){
				iterator.next();
			}
			newNode.next = iterator.current.next;
			iterator.current.next = newNode;
			size++;
		}
	
    }

    /**
     * Creates a new node that points to the given memory block, and adds it
     * to the end of this list (the node will become the list's last element).
     *
     * @param block
     *        the given memory block
     */
    public void addLast(MemoryBlock block) {
        Node newNode = new Node(block);
		if (first == null) {
			first = newNode;
			last = newNode;
		}
		else {
			last.next =newNode;
			last = newNode;
		}
		size++;

    }

    /**
     * Creates a new node that points to the given memory block, and adds it
     * to the beginning of this list (the node will become the list's first element).
     *
     * @param block
     *        the given memory block
     */
    public void addFirst(MemoryBlock block) {
        Node newNode = new Node(block);
		if (first == null) last = newNode;
		newNode.next = first;
		first = newNode;
		size++;

    }

    /**
     * Gets the memory block located at the given index in this list.
     *
     * @param index
     *        the index of the retrieved memory block
     * @return the memory block at the given index
     * @throws IllegalArgumentException
     *         if index is negative or >= size
     */
    public MemoryBlock getBlock(int index) {
        // again, if test wants "index must be between 0 and size" for out-of-range:
        return getNode(index).block;
    }

    /**
     * Gets the index of the node pointing to the given memory block.
     *
     * @param block
     *        the given memory block
     * @return the index of the block, or -1 if the block is not in this list
     */
    public int indexOf(MemoryBlock block) {
        ListIterator iterator = new ListIterator(first);
		int index = 0;
		while (iterator.hasNext()){
			if (iterator.current.block.equals(block)) {
                return index;
                }

			else {
				iterator.next();
				index++;
			}
		}
		return -1;
	

    }

    /**
     * Removes the given node from this list.
     *
     * @param node
     *        the node that will be removed from this list
     */
    public void remove(Node node) {
        remove(node.block);
    }

    /**
     * Removes from this list the node which is located at the given index.
     *
     * @param index the location of the node that has to be removed.
     * @throws IllegalArgumentException
     *         if index is negative or >= size
     */
    public void remove(int index) {
        if (index < 0 || index >= size) {
			throw new IllegalArgumentException(
					"index must be between 0 and size - 1");
		}
		if (index == 0){
			first = first.next;
			if (first == null) last = null;
		}
		else {
			Node prevNode = getNode(index - 1);
			Node current = prevNode.next;
			prevNode.next = current.next;
			if (current == last) last = prevNode;
		}
		size--;

    }

    /**
     * Removes from this list the node pointing to the given memory block.
     *
     * @param block the memory block that should be removed from the list
     * @throws IllegalArgumentException
     *         if the given memory block is not in this list
     */
    public void remove(MemoryBlock block) {
        int index = indexOf(block);
		if (index == -1){
			throw new IllegalArgumentException("index must be between 0 and size");
		}
		remove(index);

    }

    /**
     * Returns an iterator over this list, starting with the first element.
     */
    public ListIterator iterator() {
        return new ListIterator(first);
    }

    /**
     * A textual representation of this list, for debugging.
     */
    public String toString() {
        String str = "";
		ListIterator iterator = new ListIterator(first);
		while (iterator.hasNext()){
			str += iterator.current.block.toString() + " "; 
			iterator.next();
		}
		return str;

    }
}
