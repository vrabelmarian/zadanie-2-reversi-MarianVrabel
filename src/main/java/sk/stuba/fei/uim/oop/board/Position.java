package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    public void add(Direction dir) {
        this.x += dir.getX();
        this.y += dir.getY();
    }
}
