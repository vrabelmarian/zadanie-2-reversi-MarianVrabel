package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import sk.stuba.fei.uim.oop.logic.Logic;

import java.awt.*;

public class Board {
    @Getter
    private final int size;
    @Getter
    private int cellSize;
    private Cell[][] board;

    public Board(int size) {
        this.size = size;
        this.calculateCellSize();
        this.initializeBoard();
    }

    private void calculateCellSize() {
        if(size == 12) {
            this.cellSize = 50;
        }
        else if (size == 10) {
            this.cellSize = 60;
        }
        else if (size == 8) {
            this.cellSize = 75;
        }
        else {
            this.cellSize = 100;
        }
    }

    private void initializeBoard() {
        board = new Cell[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = new Cell(col,row,cellSize,null);
            }
        }
        this.setTokens();
    }

    private void setTokens() {
        int mid = size/2;
        board[mid-1][mid-1].setColor(Logic.AI);
        board[mid][mid].setColor(Logic.AI);
        board[mid-1][mid].setColor(Logic.PLAYER);
        board[mid][mid-1].setColor(Logic.PLAYER);
    }

    public void draw(Graphics g) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col].draw(g);
            }
        }
    }

    public Cell getCell(int x,int y) {
        return this.board[y][x];
    }

    public Cell getCell(Position pos) {
        return this.board[pos.getY()][pos.getX()];
    }

    public void paintCell(int x,int y,String color) {
        board[y][x].setColor(color);
    }

    public void restart() {
        this.initializeBoard();
    }
}
