package ds3.snake;

public class Snake {
    private Position[] data;
    private int tail;
    private int head;
    private int size;
    private int capacity;

    public Snake(Position initialTail, Position initialHead) {
        this.capacity = 2;
        this.size = 2;
        this.data = new Position[capacity];
        this.data[0] = initialTail;
        this.data[1] = initialHead;
        this.tail = 0;
        this.head = 1;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    public int getTailIndex() {
        return tail;
    }

    public int getHeadIndex() {
        return head;
    }

    public Position getHeadPosition() {
        if (size == 0) return null;
        return data[head];
    }

    public Position get(int logicalIndex) {
        if (logicalIndex < 0 || logicalIndex >= size) {
            throw new IndexOutOfBoundsException("Índice lógico fuera de rango [0, " + size + "): " + logicalIndex);
        }
        return data[(tail + logicalIndex) % capacity];
    }

    /**
     * O(1) Amortizado. Si el arreglo está lleno, duplica la capacidad antes de insertar.
     */
    public boolean addHead(Position p) {
        boolean resized = false;
        if (size == capacity) {
            resize(2 * capacity);
            resized = true;
        }
        if (size == 0) {
            tail = 0;
            head = 0;
        } else {
            head = (head + 1) % capacity;
        }
        data[head] = p;
        size++;
        return resized;
    }

    /**
     * O(1) Peor Caso. Elimina la cola p_0 y actualiza el puntero tail.
     */
    public Position removeTail() {
        if (size == 0) {
            throw new IllegalStateException("La serpiente está vacía.");
        }
        Position removed = data[tail];
        data[tail] = null; // Limpieza de celda
        tail = (tail + 1) % capacity;
        size--;
        if (size == 0) {
            head = -1;
            tail = 0;
        }
        return removed;
    }

    /**
     * Comprueba si la posición colisiona con CUALQUIER parte actual de la serpiente,
     * incluyendo la cola (incluso si está por liberarse).
     */
    public boolean collision(Position p) {
        for (int i = 0; i < size; i++) {
            Position currentCell = get(i);
            if (currentCell.equals(p)) {
                return true;
            }
        }
        return false;
    }

    private void resize(int newCapacity) {
        Position[] newData = new Position[newCapacity];
        // Copia explícita preservando exactamente el orden lógico (0 -> tail, size-1 -> head)
        for (int i = 0; i < size; i++) {
            newData[i] = get(i);
        }
        this.data = newData;
        this.tail = 0;
        this.head = (size == 0) ? -1 : size - 1;
        this.capacity = newCapacity;
    }

    public String physicalArrayToString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < capacity; i++) {
            if (data[i] == null) {
                sb.append("null");
            } else {
                sb.append("(").append(data[i].row()).append(",").append(data[i].column()).append(")");
            }
            if (i < capacity - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}