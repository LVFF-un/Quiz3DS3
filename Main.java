package ds3.snake;

public class Main {

    public enum Direction {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

        public final int dRow;
        public final int dCol;

        Direction(int dRow, int dCol) {
            this.dRow = dRow;
            this.dCol = dCol;
        }
    }

    public enum MoveResult {
        GAME_OVER, MOVED, ATE_FOOD
    }

    public static class SnakeGame {
        private final Snake snake;
        private Position food;
        private final int boardRows;
        private final int boardCols;

        public SnakeGame(Position initialTail, Position initialHead, int rows, int cols) {
            this.snake = new Snake(initialTail, initialHead);
            this.boardRows = rows;
            this.boardCols = cols;
            generateFood();
        }

        public Snake getSnake() {
            return snake;
        }

        public Position getFood() {
            return food;
        }

        public Position computeNewHead(Direction direction) {
            Position currentHead = snake.getHeadPosition();
            return currentHead.translate(direction.dRow, direction.dCol);
        }

        public boolean foodAt(Position p) {
            return food != null && food.equals(p);
        }

        public void generateFood() {
            for (int r = 0; r < boardRows; r++) {
                for (int c = 0; c < boardCols; c++) {
                    Position p = new Position(r, c);
                    if (!snake.collision(p)) {
                        this.food = p;
                        return;
                    }
                }
            }
            this.food = null;
        }

        /**
         * Lógica exacta del pseudocódigo MOVE(direction):
         * 1. computeNewHead
         * 2. collision (considera la cola actual)
         * 3. addHead (siempre primero, genera tamaño transitorio k -> k+1)
         * 4. if foodAt -> generateFood else removeTail
         */
        public MoveResult move(Direction direction) {
            Position newHead = computeNewHead(direction);

            if (snake.collision(newHead)) {
                return MoveResult.GAME_OVER;
            }

            boolean resized = snake.addHead(newHead);

            if (foodAt(newHead)) {
                generateFood();
                return MoveResult.ATE_FOOD;
            } else {
                snake.removeTail();
                return MoveResult.MOVED;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   QUIZ 03: THE SNAKE CHALLENGE - TRAZA Y JUEGO ");
        System.out.println("=================================================\n");

        runP3Trace();
        System.out.println("\n-------------------------------------------------");
        runGameSimulation();
    }

    /**
     * Reproduce la traza exacta exigida en la Pregunta 3 (f)
     */
    private static void runP3Trace() {
        System.out.println("--- PREGUNTA 3 (f): TRAZA DE MOVIMIENTOS ---");
        Position posA = new Position(0, 0); // Tail inicial
        Position posB = new Position(0, 1); // Head inicial

        Snake snake = new Snake(posA, posB);
        printStepState("Estado Inicial", snake, false);

        // N1 (no come C)
        boolean r1 = snake.addHead(new Position(0, 2)); // C
        snake.removeTail();
        printStepState("N1 (no come C)", snake, r1);

        // N2 (come D)
        boolean r2 = snake.addHead(new Position(0, 3)); // D
        printStepState("N2 (come D)   ", snake, r2);

        // N3 (no come E)
        boolean r3 = snake.addHead(new Position(0, 4)); // E
        snake.removeTail();
        printStepState("N3 (no come E)", snake, r3);

        // N4 (no come F)
        boolean r4 = snake.addHead(new Position(1, 4)); // F
        snake.removeTail();
        printStepState("N4 (no come F)", snake, r4);

        // N5 (come G)
        boolean r5 = snake.addHead(new Position(1, 3)); // G
        printStepState("N5 (come G)   ", snake, r5);

        // N6 (no come H)
        boolean r6 = snake.addHead(new Position(1, 2)); // H
        snake.removeTail();
        printStepState("N6 (no come H)", snake, r6);
    }

    private static void printStepState(String label, Snake snake, boolean resized) {
        System.out.printf("%-15s | Arreglo: %-32s | tail: %d | head: %d | size: %d | capacity: %d | Resize: %b\n",
                label, snake.physicalArrayToString(), snake.getTailIndex(), snake.getHeadIndex(),
                snake.size(), snake.capacity(), resized);
    }

    private static void runGameSimulation() {
        System.out.println("--- SIMULACIÓN DEL PSEUDOCÓDIGO MOVE(direction) ---");
        Position startTail = new Position(2, 1);
        Position startHead = new Position(2, 2);
        SnakeGame game = new SnakeGame(startTail, startHead, 10, 10);

        System.out.println("Comida generada en: (" + game.getFood().row() + ", " + game.getFood().column() + ")");
        printGameState(game, "Estado Inicial");

        Direction[] script = {Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.LEFT};

        for (Direction dir : script) {
            MoveResult result = game.move(dir);
            printGameState(game, "MOVE(" + dir + ") -> " + result);
            if (result == MoveResult.GAME_OVER) {
                System.out.println("¡COLISIÓN DETECTADA! GAME OVER.");
                break;
            }
        }
    }

    private static void printGameState(SnakeGame game, String action) {
        Snake s = game.getSnake();
        System.out.printf("%-22s | Arreglo: %-28s | size: %d | cap: %d | Comida: (%d,%d)\n",
                action, s.physicalArrayToString(), s.size(), s.capacity(),
                game.getFood() != null ? game.getFood().row() : -1,
                game.getFood() != null ? game.getFood().column() : -1);
    }
}