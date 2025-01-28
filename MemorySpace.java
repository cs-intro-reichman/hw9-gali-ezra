/**
 * Represents a managed memory space. The memory space manages a list of allocated 
 * memory blocks, and a list free memory blocks. The methods "malloc" and "free" are 
 * used, respectively, for creating new blocks and recycling existing blocks.
 */
/**
 * Represents a managed memory space. The memory space manages a list of allocated 
 * memory blocks, and a list of free memory blocks. The methods "malloc" and "free" are 
 * used, respectively, for creating new blocks and recycling existing blocks.
 */
public class MemorySpace {
    
    // A list of the memory blocks that are presently allocated
    private LinkedList allocatedList;

    // A list of memory blocks that are presently free
    private LinkedList freeList;

    /**
     * Constructs a new managed memory space of a given maximal size.
     * 
     * @param maxSize
     *            the size of the memory space to be managed
     */
    public MemorySpace(int maxSize) {
        // initializes an empty list of allocated blocks.
        allocatedList = new LinkedList();
        // Initializes a free list containing a single block that represents
        // the entire memory. The base address of this single initial block is
        // zero, and its length is the given memory size.
        freeList = new LinkedList();
        freeList.addLast(new MemoryBlock(0, maxSize));
    }

    /**
     * Allocates a memory block of a requested length (in words). 
     * Returns the base address of the allocated block, or -1 if unable to allocate.
     * 
     * This implementation scans the freeList, looking for the first free memory block 
     * whose length is at least 'length'. If such a block is found, the method:
     *   (1) Creates a new block with that base address and 'length'.
     *   (2) Appends the new block to allocatedList.
     *   (3) Updates the found block's baseAddress and length accordingly.
     *   (4) If the found block's length becomes 0, removes it from freeList.
     * Returns the newBlock.baseAddress if successful; otherwise returns -1.
     */
    public int malloc(int length) {
        ListIterator iterator = new ListIterator(freeList.getFirst());
        while (iterator.hasNext()) {
            if (iterator.current.block.length >= length) {
                MemoryBlock newBlock = new MemoryBlock(iterator.current.block.baseAddress, length);
                allocatedList.addLast(newBlock);

                // Update the free block
                iterator.current.block.baseAddress += length;
                iterator.current.block.length -= length;

                // If there's no space left in that free block, remove it
                if (iterator.current.block.length == 0) {
                    freeList.remove(iterator.current.block);
                }
                return newBlock.baseAddress;
            }
            iterator.next();
        }
        return -1; // no suitable block found
    }

    /**
     * Frees the memory block whose base address equals the given address.
     * This implementation deletes that block from the allocatedList and adds
     * it at the end of the free list. 
     * 
     * @param address the starting address of the block to free
     */
    public void free(int address) {
        // You might want to change how you handle the case where allocatedList is empty:
        if (allocatedList.getSize() == 0) {
            // If the tests require a certain error message, update below:
            throw new IllegalArgumentException("index must be between 0 and size");
        }
        ListIterator iterator = new ListIterator(allocatedList.getFirst());
        while (iterator.hasNext()) {
            if (iterator.current.block.baseAddress == address) {
                allocatedList.remove(iterator.current.block);
                freeList.addLast(iterator.current.block);
                return;
            }
            iterator.next();
        }
        // Might want to handle the case if address not found with an exception or no-op
        // e.g. throw new IllegalArgumentException("address not in allocated list");
    }

    /**
     * A textual representation of the free list and the allocated list of this memory space,
     * for debugging purposes.
     */
    public String toString() {
        return freeList.toString() + "\n" + allocatedList.toString();
    }

    /**
     * Performs defragmentation of this memory space.
     * Normally, called by malloc when it fails, but here it is separate.
     */
    public void defrag() {
        ListIterator iterator = new ListIterator(freeList.getFirst());
        MemoryBlock zeroBlock = new MemoryBlock(0, 0);
        MemoryBlock currentBlock;
        ListIterator subIterator;
        int sumOfZeroBlocks = 0;

        while (iterator.hasNext()) {
            currentBlock = iterator.current.block;
            subIterator = new ListIterator(freeList.getFirst());
            while (!currentBlock.equals(zeroBlock) && subIterator.hasNext()) {
                if (!currentBlock.equals(subIterator.current.block) && 
                    !subIterator.current.block.equals(zeroBlock)) {

                    // merges consecutive free blocks if addresses match end-to-start
                    if (currentBlock.baseAddress + currentBlock.length == subIterator.current.block.baseAddress) {
                        iterator.current.block = new MemoryBlock(
                            currentBlock.baseAddress, 
                            currentBlock.length + subIterator.current.block.length
                        );
                        subIterator.current.block = zeroBlock;
                        currentBlock = iterator.current.block;
                        sumOfZeroBlocks++;
                    }
                    else if (subIterator.current.block.baseAddress + subIterator.current.block.length == currentBlock.baseAddress) {
                        subIterator.current.block = new MemoryBlock(
                            subIterator.current.block.baseAddress, 
                            currentBlock.length + subIterator.current.block.length
                        );
                        iterator.current.block = zeroBlock;
                        currentBlock = iterator.current.block;
                        sumOfZeroBlocks++;
                    }
                }
                subIterator.next();
            }
            iterator.next();
        }
        // remove all zeroBlocks (dummy blocks) that we merged
        for(int i = 0; i < sumOfZeroBlocks; i++){
            freeList.remove(zeroBlock);
        }
    }
}
