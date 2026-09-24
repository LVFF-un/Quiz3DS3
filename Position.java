package ds3.snake;

public record Position(int row, int column) {
    public Position translate(int dRow, int dColumn) {
        return new Position(this.row + dRow, this.column + dColumn);
    }
}