package ds3.snake;

import java.util.NoSuchElementException;

public class DynamicArray<T> {
    private T[] data;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("La capacidad inicial debe ser al menos 1.");
        }
        this.capacity = initialCapacity;
        this.size = 0;
        this.data = (T[]) new Object[initialCapacity];
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    public T get(int index) {
        checkIndex(index);
        return data[index];
    }

    public void set(int index, T value) {
        checkIndex(index);
        data[index] = value;
    }

    public void append(T value) {
        if (size == capacity) {
            resize(2 * capacity);
        }
        data[size] = value;
        size++;
    }

    public T removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("No se puede realizar removeLast sobre una estructura vacía.");
        }
        int lastIndex = size - 1;
        T removedValue = data[lastIndex];
        data[lastIndex] = null; // Evita loitering / memory leak
        size--;
        return removedValue;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        T[] newData = (T[]) new Object[newCapacity];
        // Copia explícita elemento por elemento (sin System.arraycopy ni Arrays.copyOf)
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        this.data = newData;
        this.capacity = newCapacity;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango [0, " + size + "): " + index);
        }
    }
}