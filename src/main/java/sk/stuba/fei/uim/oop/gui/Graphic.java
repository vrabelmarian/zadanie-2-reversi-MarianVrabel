package sk.stuba.fei.uim.oop.gui;

import lombok.Setter;
import sk.stuba.fei.uim.oop.board.Board;

import javax.swing.*;
import java.awt.*;

public class Graphic extends JPanel {
    @Setter
    private Board board;

    public Graphic(Board board) {
        this.board = board;
        this.setBackground(Color.BLACK);
    }

    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        this.board.draw(g);
    }
}
