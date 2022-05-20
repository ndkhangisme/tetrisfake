import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
	private Rectangle playBounds, quitBounds, reportBounds, htpBounds;
	private boolean leftClick = false;
	private BufferedImage title, howtoplay, play, quit, report;
	private Window window;
	private BufferedImage[] playButton = new BufferedImage[2], quitButton = new BufferedImage[2], htpButton = new BufferedImage[2];
	private Timer timer;
	private boolean running = false;
	
	public Title(Window window){
		try {
			title = ImageIO.read(Board.class.getResource("/Title.png"));
			play = ImageIO.read(Board.class.getResource("/play.png"));
			quit = ImageIO.read(Board.class.getResource("/quit.png"));
			report = ImageIO.read(Board.class.getResource("/report.png"));
			howtoplay = ImageIO.read(Board.class.getResource("/howtoplay.png"));
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
		
		htpButton[0] = howtoplay.getSubimage(0, 0, 100, 50);
		htpButton[1] = howtoplay.getSubimage(100, 0, 100, 50);
		
		
		playBounds = new Rectangle(Window.WIDTH/2 - 50, Window.HEIGHT/2 - 65, 100, 50);
		
		htpBounds = new Rectangle(Window.WIDTH/2 - 50, Window.HEIGHT/2 , 100, 50);
		
		quitBounds = new Rectangle(Window.WIDTH/2 - 50, Window.HEIGHT/2 + 65, 100, 50);
		
		reportBounds = new Rectangle(Window.WIDTH - 40, Window.HEIGHT - 40, 20, 20);
		
		
		this.window = window;
		
		
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);
		
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		g.drawImage(title, Window.WIDTH/2 - title.getWidth()/2, Window.HEIGHT/2 - title.getHeight()/2 - 200, null);
	
		g.drawImage(report, Window.WIDTH - 40, Window.HEIGHT - 60, null);
		
		if(playBounds.contains(mouseX, mouseY))
			g.drawImage(playButton[1], Window.WIDTH/2 - 50, Window.HEIGHT/2 - 100, null);
		else
			g.drawImage(playButton[0], Window.WIDTH/2 - 50, Window.HEIGHT/2 - 100, null);
		
		if(htpBounds.contains(mouseX, mouseY))
			g.drawImage(htpButton[0], Window.WIDTH/2 - 50, Window.HEIGHT/2  - 30, null);
		else
			g.drawImage(htpButton[1], Window.WIDTH/2 - 50, Window.HEIGHT/2 - 30, null);
		
		if(quitBounds.contains(mouseX, mouseY))
			g.drawImage(quitButton[0], Window.WIDTH/2 - 50, Window.HEIGHT/2 + 40, null);
		else
			g.drawImage(quitButton[1], Window.WIDTH/2 - 50, Window.HEIGHT/2 + 40, null);
		
		if(leftClick && playBounds.contains(mouseX, mouseY)) {
			if(running) {
				window.startTetris();
			}
		}

		if(leftClick && quitBounds.contains(mouseX, mouseY)) {
			if(running) {
				window.exitGame();
				running = !running;
			}
			
		}
		
		if(leftClick && htpBounds.contains(mouseX, mouseY)) {
			if(running) {
				window.howToPlay();
				running = !running;
			}
			
		}
		
		if(leftClick && reportBounds.contains(mouseX, mouseY)) {
			if(running) {
				visitSite();
				running = !running;
			}
			
		}

	}
	
	
	public void visitSite() {
		try {
			 Desktop desktop = java.awt.Desktop.getDesktop();
			  URI oURL = new URI("https://tinyurl.com/yxfapnz3");
			  desktop.browse(oURL);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
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
			running = true;
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
