import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Title extends JPanel implements MouseListener, MouseMotionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int mouseX, mouseY;
	private Rectangle bounds, quitBounds;
	private boolean leftClick = false;
	private BufferedImage title, instructions, play, quit;
	private Window window;
	private BufferedImage[] playButton = new BufferedImage[2], quitButton = new BufferedImage[2];
	private Timer timer;
	
	
	public Title(Window window){
		try {
			title = ImageIO.read(Board.class.getResource("/Title.png"));
			instructions = ImageIO.read(Board.class.getResource("/arroww.png"));
			play = ImageIO.read(Board.class.getResource("/play.png"));
			quit = ImageIO.read(Board.class.getResource("/quit.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		timer = new Timer(1000/60, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
			
		});
		timer.start();
		mouseX = 0;
		mouseY = 0;
		
		playButton[0] = play.getSubimage(0, 0, 100, 50);
		playButton[1] = play.getSubimage(100, 0, 100, 50);
		
		quitButton[0] = quit.getSubimage(0, 0, 100, 50);
		quitButton[1] = quit.getSubimage(100, 0, 100, 50);
		
		
		bounds = new Rectangle(Window.WIDTH/2 + 80, Window.HEIGHT/2 -65, 100, 50);
		
		quitBounds = new Rectangle(Window.WIDTH/2 + 80, Window.HEIGHT/2, 100, 50);
		
		
		this.window = window;
		
		
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(leftClick && bounds.contains(mouseX, mouseY))
			window.startTetris();
		if(leftClick && quitBounds.contains(mouseX, mouseY))
			System.exit(0);

		g.setColor(Color.WHITE);
		
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		g.drawImage(title, Window.WIDTH/2 - title.getWidth()/2, Window.HEIGHT/2 - title.getHeight()/2 - 200, null);
		
		g.drawImage(instructions, Window.WIDTH/2 - instructions.getWidth()/2,
					Window.HEIGHT/2 - instructions.getHeight()/2 + 150, null);
		
		if(bounds.contains(mouseX, mouseY))
			g.drawImage(playButton[1], Window.WIDTH/2 + 80, Window.HEIGHT/2 - 100, null);
		else
			g.drawImage(playButton[0], Window.WIDTH/2 + 80, Window.HEIGHT/2 - 100, null);
		if(quitBounds.contains(mouseX, mouseY))
			g.drawImage(quitButton[0], Window.WIDTH/2 + 80, Window.HEIGHT/2 - 30, null);
		else
			g.drawImage(quitButton[1], Window.WIDTH/2 + 80, Window.HEIGHT/2 - 30, null);
	}
	
	public Window getWindow() {
		return window;
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
}
