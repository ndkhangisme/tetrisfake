import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements KeyListener {

	private BufferedImage blocks;
	
	private final  int blockSize = 30;
	
	private final int boardWidth = 10, boardHeight = 20;
	
	private int[][] board = new int [boardHeight][boardWidth];
	
	private Shape[] shapes = new Shape[7];
	
	private Shape currentShape;
	
	private Timer timer;
	
	private final int FPS = 60;
	
	private final int delay = 1000/FPS;
	
	public static Font pixelfont;
	
	private boolean gameOver = false;
	
	public static int score = 0, highScore = 0, scoreToAdd = 0;
	
	public Board() {
		
		try {
			blocks = ImageIO.read(Board.class.getResource("/tiles.png"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		timer = new Timer(delay,new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				update();
				repaint();
			}
		});
		
		timer.start();
		//shapes
		shapes[0] = new Shape(blocks.getSubimage(0, 0, blockSize, blockSize), new int[][] {
			{1, 1, 1, 1}	// shape I
		}, this, 1);
		
		shapes[1] = new Shape(blocks.getSubimage(blockSize, 0, blockSize, blockSize), new int[][] {
			{1, 1, 0},
			{0, 1, 1}	//shape Z
		}, this, 2);
		
		shapes[2] = new Shape(blocks.getSubimage(2 * blockSize, 0, blockSize, blockSize), new int[][] {
			{0, 1, 1},
			{1, 1, 0}	//shape S
		}, this, 3);
		
		shapes[3] = new Shape(blocks.getSubimage(3 * blockSize, 0, blockSize, blockSize), new int[][] {
			{1, 1, 1},
			{0, 1, 0}	// shape T
		}, this, 4);
		
		shapes[4] = new Shape(blocks.getSubimage(4 * blockSize, 0, blockSize, blockSize), new int[][] {
			{1, 1},
			{1, 1}	// shape O
		}, this, 5);
		
		shapes[5] = new Shape(blocks.getSubimage(5 * blockSize, 0, blockSize, blockSize), new int[][] {
			{1, 1, 1},
			{0, 0, 1}	// shape L
		}, this, 6);
		
		shapes[6] = new Shape(blocks.getSubimage(6 * blockSize, 0, blockSize, blockSize), new int[][] {
			{1, 1, 1},
			{1, 0, 0}	// shape J
		}, this, 7);
		
		//currentShape = shapes[4];
		setNextShape();
	}
	
	
	public void update() {
	
		currentShape.update();
		if(gameOver) {
			timer.stop();
		}
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
		currentShape.render(g);
		
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[row].length; col++) {
				if(board[row][col] != 0) {
					g.drawImage(blocks.getSubimage((board[row][col] - 1) * blockSize, 0, blockSize, blockSize), col * blockSize, row * blockSize, null);
				}
			}
		}
		
		
		for(int i = 0; i < boardHeight; i++) {
			g.drawLine(0 , i * blockSize, boardWidth * blockSize, i * blockSize);
		}
		for(int j = 0; j < boardWidth; j++) {
			g.drawLine(j * blockSize + 30 , 0, j * blockSize + 30, boardHeight * blockSize);
		}
		
		try {
			pixelfont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/bulky-pixels.regular.ttf")).deriveFont(15f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		g.setColor(Color.BLACK);
		
		//g.setFont(new Font("Georgia", Font.BOLD, 20));
		g.setFont(pixelfont.deriveFont(20f));
		g.drawString("SCORE", Window.WIDTH - 125, Window.HEIGHT/2);
		g.drawString(score+"", Window.WIDTH - 125, Window.HEIGHT/2 + 30);

	}
	
	public void setNextShape() {
        int index = (int)(Math.random()* shapes.length);
        
        Shape nextShape = new Shape(shapes[index].getBlock(), shapes[4].getCoords(), this, shapes[index].getColor());
        
        currentShape = nextShape;
        
        for (int row = 0; row < currentShape.getCoords().length; row++) {
            for (int col = 0; col < currentShape.getCoords()[row].length; col++) {
            	if(board[row][col + 4] != 0) {
            		//System.out.println("Gameover");
            		gameOver = true;
            	}
            }
        }
	}
	
	public int getBlocksSize() {
		return blockSize;
	}

	public int[][] getBoard(){
		return board;
	}

	@Override
	public void keyPressed(KeyEvent e) {
	
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			currentShape.setDeltaX(-1);
		}else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			currentShape.setDeltaX(1);
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentShape.fast();
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			currentShape.rotateShape();
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentShape.nomalSpeed() ;
		}
		
	}
	

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
	public void addScore() {
		score = score + 10;
		
	}



}
