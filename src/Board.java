import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JPanel;
import javax.swing.Timer;




public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener{
	
	//Assets
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Clip music;
	
	private BufferedImage blocks, background, pause, speaker;
	
	//board dimensions (the playing area)
	
	//private final int boardHeight = 20, boardWidth = 10;
	
	// block size
	
	private final int blockSize = 30;
	
	// field
	
	public static int[][] board = new int[10][20];
	
	// array with all the possible shapes
	
	private Shape[] shapes = new Shape[7];
	
	// currentShape
	
	public static Shape currentShape, nextShape;
	
	
	//fonts
	
	public static Font pixelfont;
	
	// game loop
	
	private Timer looper;
	
	private int FPS = 60;
	
	private int delay = 1000/FPS;
	
	// mouse events variables
	
	private int mouseX, mouseY;
	
	private boolean leftClick = false;
	
	private Rectangle stopBounds, speakerBounds;
	
	private boolean gamePaused = false;
	
	private boolean gameOver = false;
	
	private boolean isMute = false;
	
	// buttons press lapse
	
	private Timer buttonLapse = new Timer(300, new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			buttonLapse.stop();
		}});
	
	// score
	
	public static int score = 0, highScore = 0, scoreToAdd = 0;
	
	
	
	public Board(){
		// load Assets
		blocks = ImageLoader.loadImage("/tiles.png");
		
		background = ImageLoader.loadImage("/bg.png");
		pause = ImageLoader.loadImage("/pauseandplay1.png");
		speaker = ImageLoader.loadImage("/speaker1.png");
		
		music = ImageLoader.LoadSound("/music.wav");
		
		
		music.loop(Clip.LOOP_CONTINUOUSLY);
		
		
		
		mouseX = 0;
		mouseY = 0;
		
		stopBounds = new Rectangle(350, 530, pause.getSubimage(0, 0, 50, 50).getWidth(), pause.getSubimage(0, 0, 50, 50).getHeight() + pause.getSubimage(0, 0, 50, 50).getHeight()/2);
		speakerBounds = new Rectangle(350, 530 - speaker.getSubimage(0, 0, 50, 50).getHeight() - 20,speaker.getSubimage(0, 0, 50, 50).getWidth(),
				speaker.getSubimage(0, 0, 50, 50).getHeight()+ speaker.getSubimage(0, 0, 50, 50).getHeight());
		
		// create game looper
		
		looper = new Timer(delay, new GameLooper());
		
		// create shapes
		
		shapes[0] = new Shape(new int[][]{
			{1, 1, 1, 1}   // I shape;
		}, blocks.getSubimage(0, 0, blockSize, blockSize), this, 1);
		
		shapes[1] = new Shape(new int[][]{
			{1, 1, 1},
			{0, 1, 0},   // T shape;
		}, blocks.getSubimage(blockSize, 0, blockSize, blockSize), this, 2);
		
		shapes[2] = new Shape(new int[][]{
			{1, 1, 1},
			{1, 0, 0},   // L shape;
		}, blocks.getSubimage(blockSize*2, 0, blockSize, blockSize), this, 3);
		
		shapes[3] = new Shape(new int[][]{
			{1, 1, 1},
			{0, 0, 1},   // J shape;
		}, blocks.getSubimage(blockSize*3, 0, blockSize, blockSize), this, 4);
		
		shapes[4] = new Shape(new int[][]{
			{0, 1, 1},
			{1, 1, 0},   // S shape;
		}, blocks.getSubimage(blockSize*4, 0, blockSize, blockSize), this, 5);
		
		shapes[5] = new Shape(new int[][]{
			{1, 1, 0},
			{0, 1, 1},   // Z shape;
		}, blocks.getSubimage(blockSize*5, 0, blockSize, blockSize), this, 6);
		
		shapes[6] = new Shape(new int[][]{
			{1, 1},
			{1, 1},   // O shape;
		}, blocks.getSubimage(blockSize*6, 0, blockSize, blockSize), this, 7);
		
		
	}
	
	private void update(){	
		if(stopBounds.contains(mouseX, mouseY) && leftClick && !buttonLapse.isRunning() && !gameOver)
		{
			buttonLapse.start();
			gamePaused = !gamePaused;
		}
		
		if(speakerBounds.contains(mouseX, mouseY) && leftClick) {
			setMute(!isMute);

			if(isMute()) {
				music.stop();
				
			}
			else {
				music.start();
				music.loop(Clip.LOOP_CONTINUOUSLY);
//				System.out.println("unmute");
				
			}
		}

		if(gamePaused || gameOver)
		{
			return;
		}
		currentShape.update();
	}
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.drawImage(background, 0, 0, null);
		
		for(int row = 0; row < board.length; row++)
		{
			for(int col = 0; col < board[row].length; col++)
			{
				
				if(board[row][col] != 0)
				{
					
					g.drawImage(blocks.getSubimage((board[row][col] - 1)*blockSize, 0, blockSize, blockSize), row*blockSize, col*blockSize, null);
				}				
					
			}
		}
		
		for(int row = 0; row < nextShape.getCoords()[0].length; row++)
		{
			for(int col = 0; col < nextShape.getCoords().length; col++)
			{
				if(nextShape.getCoords()[col][row] != 0)
				{
					g.drawImage(nextShape.getBlock(), row * 30 + 310, col * 30 + 50, null);	
				}
			}		
		}
		currentShape.render(g);
		try {
			pixelfont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/bulky-pixels.regular.ttf")).deriveFont(15f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(stopBounds.contains(mouseX, mouseY))
			if(gamePaused) {
				g.drawImage(pause.getSubimage(50, 0, 50, 50).getScaledInstance(pause.getSubimage(50, 0, 50, 50).getWidth() + 2, pause.getSubimage(50, 0, 50, 50).getHeight() + 2, BufferedImage.SCALE_DEFAULT)
				, stopBounds.x, stopBounds.y, null);
				
			}else
				g.drawImage(pause.getSubimage(0, 0, 50, 50).getScaledInstance(pause.getSubimage(0, 0, 50, 50).getWidth() + 2, pause.getSubimage(0, 0, 50, 50).getHeight() + 2, BufferedImage.SCALE_DEFAULT)
						, stopBounds.x, stopBounds.y, null);
			
		else {
			if(gamePaused) {
				g.drawImage(pause.getSubimage(50, 0, 50, 50), stopBounds.x, stopBounds.y, null);
			}else
				g.drawImage(pause.getSubimage(0, 0, 50, 50), stopBounds.x, stopBounds.y, null);
		}
			
		

		if(speakerBounds.contains(mouseX, mouseY)) {
			//setMute(!isMute);

			if(isMute()) {
				if(speakerBounds.contains(mouseX, mouseY))
					g.drawImage(speaker.getSubimage(50, 0, 50, 50).getScaledInstance(speaker.getSubimage(50, 0, 50, 50).getWidth() + 3, speaker.getSubimage(50, 0, 50, 50).getHeight() + 3,
					BufferedImage.SCALE_DEFAULT), speakerBounds.x , speakerBounds.y, null);
				else
					g.drawImage(speaker.getSubimage(50, 0, 50, 50), speakerBounds.x, speakerBounds.y, null);
			}else
				if(speakerBounds.contains(mouseX, mouseY))
				g.drawImage(speaker.getSubimage(0, 0, 50, 50).getScaledInstance(speaker.getSubimage(0, 0, 50, 50).getWidth() + 3, speaker.getSubimage(0, 0, 50, 50).getHeight() + 3,
				BufferedImage.SCALE_DEFAULT), speakerBounds.x , speakerBounds.y, null);
			else
				g.drawImage(speaker.getSubimage(0, 0, 50, 50), speakerBounds.x, speakerBounds.y, null);
			
		}else {
			if(isMute()) {
				g.drawImage(speaker.getSubimage(50, 0, 50, 50), speakerBounds.x, speakerBounds.y, null);
			}else
				g.drawImage(speaker.getSubimage(0, 0, 50, 50), speakerBounds.x, speakerBounds.y, null);
		}
	
		
		g.setColor(Color.BLACK);
		g.setFont(pixelfont.deriveFont(15f));
		
		g.drawString("SCORE", Window.WIDTH - 125, Window.HEIGHT/2 - 100);
		g.drawString(score+"", Window.WIDTH - 125, Window.HEIGHT/2 - 70);
		g.drawString("BEST", Window.WIDTH - 125, Window.HEIGHT/2 - 40);
		g.drawString(highScore+"", Window.WIDTH - 125, Window.HEIGHT/2 - 10);
		DataHandler.load();
		repaint();
		g.setColor(new Color(0, 0, 0, 100));
		if(gamePaused)
		{
			
			g.fillRect(0, 200, WIDTH + 445, 100);
			g.setColor(Color.WHITE);
			g.setFont(pixelfont.deriveFont(18f));
			g.drawString("GAME PAUSE!!", 120, 240);
			
		}
		if(gameOver)
		{

			g.fillRect(0, 200, WIDTH + 445, 100);
			g.setColor(Color.WHITE);
			g.setFont(pixelfont.deriveFont(18f));
			g.drawString("YOU LOST!!", 150, 240);
			g.drawString("PRESS ENTER KEY TO PLAY AGAIN ", 10, 270);
		}	
		
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(new Color(0, 0, 0, 100));
		
		for(int i = 0; i <= 20; i++)
		{
			g2d.drawLine(0, i*blockSize, 10*blockSize, i*blockSize);
		}
		for(int j = 0; j <= 10; j++)
		{
			g2d.drawLine(j*blockSize, 0, j*blockSize, 20*30);
		}
	}
	
	public void setNextShape(){
		int index = (int)(Math.random()*shapes.length);
		nextShape = new Shape(shapes[6].getCoords(), shapes[index].getBlock(), this, shapes[index].getColor());
	}
	

	
	public void setCurrentShape(){
		currentShape = nextShape;
		setNextShape();
		
		for(int row = 0; row < currentShape.getCoords().length; row ++)
		{
			for(int col = 0; col < currentShape.getCoords()[0].length; col ++)
			{
				if(currentShape.getCoords()[row][col] != 0)
				{
					if(board[currentShape.getX() + row][currentShape.getY() + col] != 0)
						gameOver = true;
				}
			}		
		}
		
	}
	
	
	public int[][] getBoard(){
		return board;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP)
			currentShape.rotateShape();
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			currentShape.setDeltaX(1);
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			currentShape.setDeltaX(-1);
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			currentShape.speedUp();
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			startGame();
		if(e.getKeyCode() == KeyEvent.VK_M) {
			setMute(!isMute);

			if(isMute()) {
				music.stop();
			}
			else {
				music.start();
				music.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}
			

	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			currentShape.speedDown();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void startGame(){
		stopGame();
		setNextShape();
		setCurrentShape();
		gameOver = false;
		looper.start();
		
	}
	public void stopGame(){
		score = 0;
		
		for(int row = 0; row < board.length; row++)
		{
			for(int col = 0; col < board[row].length; col ++)
			{
				board[row][col] = 0;
			}
		}
		looper.stop();
	}
	
	public boolean isMute() {
			return isMute;
	}

	public void setMute(boolean isMute) {
		this.isMute = isMute;
	}
	
	class GameLooper implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			update();
			repaint();
		}
		
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			leftClick = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			leftClick = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
