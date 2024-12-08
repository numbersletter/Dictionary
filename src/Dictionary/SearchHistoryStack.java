package Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the stack-like data structure behind the search history. The primary field is the List storing
 * each element. Member methods are included to easily add elements / clear the stack.
 * Recently pushed elements are at the beginning of the stack (so first indices). If the element being pushed
 * exists, it is moved to the top of the stack.
 */

public class SearchHistoryStack<T>
{
    private final List<T> stack;
    private final int capacity;

    /**
     * Initializes the search history stack with a given capacity (10 for this program). The capacity is immutable.
     * @param capacity capacity of the stack
     */
    public SearchHistoryStack(int capacity)
    {
        this.capacity = capacity;
        this.stack = new ArrayList<>();
    }

    /**
     * Pushes element 'value' to stack. If the word already exists in the stack, this element moves to the top of the
     * stack. Removes oldest element if stack already full to make room.
     * @param value
     */
    public void push(T value)
    {
        if(stack.contains(value))
        {
            stack.remove(value);
        }

        // remove excess element if at max size
        if(stack.size() == capacity)
        {
            stack.remove(stack.size() - 1);
        }

        stack.add(0, value);
    }

    /**
     * Returns and removes the last element in the stack.
     * @return last element
     * @throws IllegalStateException if the stack is currently empty
     */
    public T pop() throws IllegalStateException
    {
        if(stack.isEmpty())
            throw new IllegalStateException("Cannot pop empty stack");
        return stack.remove(stack.size() - 1);
    }

    /**
     * Determines if the stack is occupied or empty.
     * @return true if the stack is empty
     */
    public boolean isEmpty()
    {
        return stack.isEmpty();
    }

    /**
     * Returns a safe array containing all the elements in the stack. This array is safe due to the implementation
     * of List.toArray() - a new array is allocated so modifications to the array is not reflected in the list
     * here and vice versa.
     * @return stack in the form of an array
     */
    public T[] stackToArray(Class<T> type)
    {
        @SuppressWarnings("unchecked")
        T[] typeArray = (T[]) java.lang.reflect.Array.newInstance(type, stack.size());
        return (T[]) stack.toArray(typeArray);
    }



}
