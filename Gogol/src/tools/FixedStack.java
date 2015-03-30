package tools;


public class FixedStack<T> {
	public T[] stack;
	private int size;
	public int top;
	private int popBalance = 0;// its used to see if all the elements have been
								// popped

	public FixedStack(T[] stack) {
		this.stack = stack;
		this.top = 0;
		this.size = stack.length;
	}
	public T getNext() {
		if (top == stack.length)
			top = 0;
		return stack[top];

	}
	public void push(T obj) {
		if (top == stack.length)
			top = 0;
		stack[top] = obj;
		top++;
		if (popBalance < size - 1)
			popBalance++;
	}

	public T pop() {
		
		if (top - 1 < 0)
			top = size;
		top--;
		T ob = stack[top];
		popBalance--;
		return ob;
	}

	public void clear() {
		top = 0;
	}

	public int size() {
		return size;
	}

	public boolean poppedAll() {
		if (popBalance == -1)
			return true;
		return false;
	}
}