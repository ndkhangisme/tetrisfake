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
import javax.swing.JPanel;
import javax.swing.Timer;

public class HowToPlay extends JPanel implements MouseListener, MouseMotionListener {

	
	private static final long serialVersionUID = 1L;
	private int mouseX, mouseY;
	private Rectangle doneBounds;
	private boolean leftClick = false;
	private BufferedImage done, howto, keyplay;
	private Window window;
	private Timer timer;
	private boolean running = false;
	private BufferedImage[] doneButton = new BufferedImage[2];
	
	public HowToPlay(Window window){
		try {
			done = ImageIO.read(Board.class.getResource("/done.png"));
			howto = ImageIO.read(Board.class.getResource("/howto.png"));
			keyplay = ImageIO.read(Board.class.getResource("/keyplay.png"));
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
	
	doneButton[0] = done.getSubimage(0, 0, 100, 50);
	doneButton[1] = done.getSubimage(100, 0, 100, 50);
	
	
	doneBounds = new Rectangle(Window.WIDTH/2 - 50, Window.HEIGHT/2 + 150, 100, 50);
	
	this.window = window;
	
	}
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);
		
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		g.drawImage(howto, Window.WIDTH/2 - howto.getWidth()/2, Window.HEIGHT/2 - howto.getHeight()/2 - 200, null);
	
		g.drawImage(keyplay, Window.WIDTH/2 - keyplay.getWidth()/2 + 50, Window.HEIGHT/2 - keyplay.getHeight()/2 + 30, null);
		
		if(doneBounds.contains(mouseX, mouseY))
			g.drawImage(doneButton[0], Window.WIDTH/2 - 50, Window.HEIGHT/2 + 120, null);
		else
			g.drawImage(doneButton[1], Window.WIDTH/2 - 50, Window.HEIGHT/2 + 120, null);

		if(leftClick && doneBounds.contains(mouseX, mouseY)) {
			if(running) {
				window.backToMenu();
				running = !running;
			}
		}
		//System.out.println(running);
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
			running = true;
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
