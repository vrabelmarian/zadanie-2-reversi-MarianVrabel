package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.logic.Logic;

import javax.swing.*;
import java.awt.*;

public class Game {
    private JFrame frame;
    private Logic logic;

    public Game() {
        this.frame = new JFrame("Reversi");
        this.logic = new Logic(frame);
        this.prepareFrame();
        this.createMenu();
        this.frameFinalization();
    }

    private void prepareFrame() {
        frame.setSize(620,730);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }

    private void frameFinalization() {
        frame.add(logic.getGraphic());
        frame.addMouseMotionListener(logic);
        frame.addMouseListener(logic);
        frame.addKeyListener(logic);
        frame.setVisible(true);
    }

    private void createMenu() {
        JPanel menu = new JPanel();
        menu.setLayout(new GridLayout(2,2));
        menu.setBackground(Color.WHITE);

        JButton restartButton = new JButton("RESTART");
        restartButton.addActionListener(logic);
        restartButton.setFocusable(false);

        JSlider slider = new JSlider(JSlider.HORIZONTAL,6,12,6);
        slider.setMajorTickSpacing(2);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setFocusable(false);
        slider.setSnapToTicks(true);
        slider.addChangeListener(logic);

        menu.add(logic.getStateLabel());
        menu.add(logic.getSizeLabel());
        menu.add(restartButton);
        menu.add(slider);

        frame.add(menu,BorderLayout.PAGE_END);
    }
}
