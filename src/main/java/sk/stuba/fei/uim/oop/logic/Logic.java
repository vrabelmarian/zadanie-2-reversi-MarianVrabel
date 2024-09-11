package sk.stuba.fei.uim.oop.logic;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.Board;
import sk.stuba.fei.uim.oop.board.Direction;
import sk.stuba.fei.uim.oop.board.Position;
import sk.stuba.fei.uim.oop.gui.Graphic;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Logic extends Adapter {
    public static final String PLAYER = "BLACK";
    public static final String AI = "WHITE";
    private final JFrame frame;
    private Board board;
    @Getter
    private final Graphic graphic;
    private List<Position> legalMoves;
    @Getter
    private JLabel sizeLabel;
    @Getter
    private JLabel stateLabel;
    private Position lastHovered;

    public Logic(JFrame frame) {
        this.frame = frame;
        this.board = new Board(6);
        this.graphic = new Graphic(board);
        this.legalMoves = new ArrayList<>();
        this.prepareLabels();
        this.findLegalMoves(PLAYER);
        this.stateMachine(GameState.PLAYER_MOVE);
    }

    private void prepareLabels() {
        this.sizeLabel = new JLabel();
        sizeLabel.setHorizontalAlignment(JLabel.CENTER);
        sizeLabel.setFocusable(false);
        updateSizeLabel();

        this.stateLabel = new JLabel();
        stateLabel.setHorizontalAlignment(JLabel.CENTER);
        stateLabel.setFocusable(false);
        this.updateStateLabel("BLACK TURN (YOU)");
    }

    private void resize(int size) {
        this.lastHovered =  null;
        this.legalMoves.clear();
        this.board = new Board(size);
        this.graphic.setBoard(this.board);
        this.updateSizeLabel();
        this.preparePlayer();
    }

    private void restart() {
        this.board.restart();
        this.preparePlayer();
    }

    private void preparePlayer() {
        this.findLegalMoves(PLAYER);
        this.updateStateLabel("BLACK TURN (YOU)");
    }

    private void stateMachine(GameState state) {
        switch (state) {
            case PLAYER_MOVE:
                if (legalMoves.isEmpty()) {
                    System.out.println("Player(Black) no moves");
                    this.findLegalMoves(AI);
                    this.stateMachine(legalMoves.isEmpty() ? GameState.GAME_END : GameState.AI_MOVE);
                }
                break;
            case AI_MOVE:
                if (!legalMoves.isEmpty()) {
                    this.decideAI();
                } else {
                    System.out.println("AI(White) no moves");
                    this.findLegalMoves(PLAYER);
                    this.stateMachine(legalMoves.isEmpty() ? GameState.GAME_END : GameState.PLAYER_MOVE);
                }
                break;
            case GAME_END:
                this.getWinner();
                break;
        }
    }

    private void findLegalMoves(String player) {
        this.changeLegalMove(false);
        legalMoves.clear();

        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (Objects.equals(board.getCell(i, j).getColor(), null) && getCellsCaptured(new Position(i,j),player).size() > 0)  {
                    legalMoves.add(new Position(i,j));
                }
            }
        }
        this.changeLegalMove(true);
    }

    private void changeLegalMove(boolean value) {
        for (Position p : legalMoves) {
            board.getCell(p).setLegalMove(value);
        }
    }

    private List<Position> getCellsCaptured(Position pos,String player) {
        List<Position> cells = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            cells.addAll(cellsCapturedInDirection(new Position(pos),dir,player));
        }

        return cells;
    }

    private List<Position> cellsCapturedInDirection(Position currentPos,Direction direction,String player) {
        List<Position> cells = new ArrayList<>();

        String enemy = Objects.equals(player, AI) ? PLAYER : AI;

        currentPos.add(direction);
        while(isCellInBoard(currentPos) && Objects.equals(board.getCell(currentPos).getColor(), enemy)) {
            cells.add(new Position(currentPos));
            currentPos.add(direction);
        }

        if (!isCellInBoard(currentPos) || !Objects.equals(board.getCell(currentPos).getColor(), player)) {
            cells.clear();
        }

        return cells;
    }

    private boolean isCellInBoard(Position pos) {
        return pos.getX() >= 0 && pos.getY() >= 0 && pos.getX() < board.getSize() && pos.getY() < board.getSize();
    }

    private void updateSizeLabel() {
        this.sizeLabel.setText("BOARD SIZE(" + board.getSize() + "x" + board.getSize() + "):");
    }

    public void updateStateLabel(String text) {
        this.stateLabel.setText(text);
    }

    private void playMove(int x, int y,String player) {
        String enemy = Objects.equals(player, AI) ? PLAYER : AI;

        board.paintCell(x,y,player);
        board.getCell(x,y).setLegalMove(false);
        List <Position> capturedCells = getCellsCaptured(new Position(x,y),player);
        for (Position pos : capturedCells) {
            board.getCell(pos).setColor(player);
        }

        this.findLegalMoves(enemy);
        this.stateMachine(Objects.equals(player, AI) ? GameState.PLAYER_MOVE : GameState.AI_MOVE);
    }

    private void decideAI() {
        Position cell = legalMoves.get(0);
        int capturedCount = 0;
        for (Position position : legalMoves) {
            List<Position> cellsCaptured = getCellsCaptured(new Position(position.getX(),position.getY()), AI);
            if (cellsCaptured.size() > capturedCount) {
                capturedCount = cellsCaptured.size();
                cell = position;
            }
        }
        int x = cell.getX();
        int y = cell.getY();

        this.playMove(x,y,AI);
    }

    private void getWinner() {
        int whiteCount = 0;
        int blackCount = 0;
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (Objects.equals(board.getCell(i, j).getColor(), AI)) {
                    whiteCount++;
                }
                if (Objects.equals(board.getCell(i, j).getColor(), PLAYER)) {
                    blackCount++;
                }
            }
        }
        if (whiteCount > blackCount) {
            this.updateStateLabel("WHITE WINS (PC) - B:" + blackCount + " W:"  + whiteCount);
        } else if (blackCount > whiteCount) {
            this.updateStateLabel("BLACK WINS (YOU) - B:" + blackCount + " W:"  + whiteCount);
        } else {
            this.updateStateLabel("DRAW - B:" + blackCount + " W:"  + whiteCount);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                this.frame.dispose();
                System.exit(1);
                break;
            case KeyEvent.VK_R:
                this.restart();
                this.graphic.repaint();
                break;
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        int newSize = ((JSlider) e.getSource()).getValue();
        if (newSize != board.getSize()) {
            this.resize(newSize);
            this.graphic.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(lastHovered != null) {
            board.getCell(lastHovered).setHighlight(false);
            this.graphic.repaint();
        }
        int x = getGridX(e.getX());
        int y = getGridY(e.getY());

        if (x>=0 && x < board.getSize() && y>=0 && y< board.getSize()){
            this.lastHovered =  new Position(x,y);

            if (board.getCell(x,y).isLegalMove()) {
                board.getCell(x,y).setHighlight(true);
                this.graphic.repaint();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = getGridX(e.getX());
        int y = getGridY(e.getY());

        if (x >= 0 && x < board.getSize() && y >= 0 && y < board.getSize()) {
            if (board.getCell(x,y).isLegalMove()) {
                this.playMove(x,y,PLAYER);
                this.graphic.repaint();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.restart();
        this.graphic.repaint();
    }

    private int getGridX (int x) {
        return (x-8 < 0) ? -1 : (x-8)/board.getCellSize();
    }

    private int getGridY (int y) {
        return (y-30 < 0) ? -1 : (y-30)/board.getCellSize();
    }
}
