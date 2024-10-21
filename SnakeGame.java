package com.project_1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    private final int BOARD_WIDTH = 300;
    private final int BOARD_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RANDOM_POSITION = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots; 
    private int foodX;
    private int foodY;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Random random;

    public SnakeGame() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        loadGame();
    }

    private void loadGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 50 - i * DOT_SIZE;
            y[i] = 50;
        }

        random = new Random();
        placeFood();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void placeFood() {
        foodX = random.nextInt(RANDOM_POSITION) * DOT_SIZE;
        foodY = random.nextInt(RANDOM_POSITION) * DOT_SIZE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (inGame) {
            g.setColor(Color.RED);
            g.fillRect(foodX, foodY, DOT_SIZE, DOT_SIZE);

            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN); 
                } else {
                    g.setColor(Color.YELLOW);
                }
                g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg, (BOARD_WIDTH - metr.stringWidth(msg)) / 2, BOARD_HEIGHT / 2);
    }

    private void checkFood() {
        if ((x[0] == foodX) && (y[0] == foodY)) {
            dots++;
            placeFood();
        }
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if ((i > 3) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }

        if (x[0] < 0 || x[0] >= BOARD_WIDTH || y[0] < 0 || y[0] >= BOARD_HEIGHT) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkFood();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (key == KeyEvent.VK_UP && !downDirection) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if (key == KeyEvent.VK_DOWN && !upDirection) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        SnakeGame game = new SnakeGame();

        frame.add(game);
        frame.setResizable(false);
        frame.pack();
        frame.setTitle("Snake Game");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
