package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.logic.Logic;

import java.awt.*;
import java.util.Objects;

public class Cell {
    private static final int OFFSET = 5;
    private final Position position;
    private final int size;
    @Getter
    @Setter
    private String color;
    @Getter
    @Setter
    private boolean legalMove;
    @Setter
    private boolean highlight;

    public Cell(int x,int y, int cellSize, String cellColor) {
        this.position = new Position(x * cellSize,y * cellSize);
        this.size = cellSize;
        this.color = cellColor;
        this.legalMove = false;
        this.highlight = false;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(position.getX() + 1, position.getY() + 1,size - 1,size - 1);

        if (Objects.equals(color, Logic.AI)) {
            this.drawOval(g,Color.WHITE);
        }
        else if (Objects.equals(color, Logic.PLAYER)) {
            this.drawOval(g,Color.BLACK);
        }

        if (legalMove) {
            this.drawOval(g,Color.GRAY);
        }

        if (highlight) {
            g.setColor(Color.BLACK);
            g.drawRect(position.getX()+1, position.getY()+1,size-2,size-2);
            g.drawRect(position.getX()+2, position.getY()+2,size-4,size-4);
        }
    }

    private void drawOval(Graphics g,Color color) {
        g.setColor(color);
        g.fillOval(position.getX() + OFFSET, position.getY() + OFFSET,size - 2*OFFSET,size - 2*OFFSET);
    }
}
