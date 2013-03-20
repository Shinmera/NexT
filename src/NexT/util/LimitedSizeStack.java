package NexT.util;

import java.util.ArrayList;

/**
 * 
 * @author Shinmera
 * @license GPLv3
 * @version 0.0.0
 */
public class LimitedSizeStack<T extends Object> extends ArrayList<T>{
    private final int maxSize;
    
    /**
     * Initializes a new queue with a size limitation. This limitation cannot
     * be changed, you need to create a new queue object to achieve this.
     * @param size 
     */
    public LimitedSizeStack(int size){
        super(size);
        this.maxSize = size;
    }
    
    /**
     * Adds a new element to the queue at index 0. If the queue is full, the
     * last element is removed silently.
     * 
     * @param object
     * @return Always true
     */
    public boolean add(T object){
        add(0, object);
        return true;
    }
    
    /**
     * Adds a new element to the queue at index i. If the queue is full, the
     * last element is removed silently. All elements at i and greater than i
     * are shifted to the right (index+1). If i exceeds the maximum size of this
     * queue, an IllegalArgumentException is thrown.
     * 
     * @param i
     * @param object 
     */
    public void add(int i, T object){
        if(i>=maxSize)throw new IllegalArgumentException("Requested index "+i+" exceeds maximum queue size "+maxSize+".");
        if(isFull()){
            super.remove(maxSize-1);
        }
        super.add(i, object);
    }
    
    /**
     * Returns the topmost object (index 0) on the stack and removes it.
     * @return 
     */
    public T pop(){
        T o = get(0);
        remove(0);
        return(o);
    }
    
    /**
     * Returns whether the queue is full or not.
     * @return 
     */
    public boolean isFull(){return size()==maxSize;}
    
    /**
     * Returns the maximum size this queue has been declared with.
     * @return 
     */
    public int getMaximumSize(){return maxSize;}
}
