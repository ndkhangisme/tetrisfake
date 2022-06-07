import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Window{
	public static final int WIDTH = 450, HEIGHT = 637;
	
	public static Board board;
	public static Title title;
	public static HowToPlay howToPlay;
	public static JFrame window;
	
	public Window(){
		
		window = new JFrame("Tetris");
		window.setSize(WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setResizable(false);	
		board = new Board(this);
		title = new Title(this);
		howToPlay = new HowToPlay(this);
		
		window.addKeyListener(board);
		window.addMouseMotionListener(title);
		window.addMouseListener(title);
		
		window.add(title);
		
		window.setVisible(true);
	}
	public void startTetris(){
		window.remove(title);
		window.addMouseMotionListener(board);
		window.addMouseListener(board);
		window.add(board);
		board.startGame();
		window.revalidate();
	}
	public void backToMenu(){
		window.remove(board);
		window.remove(howToPlay);
		window.add(title);
		window.setVisible(true);
	}

	public void howToPlay(){
		window.remove(title);
		window.remove(board);
		window.addMouseMotionListener(howToPlay);
		window.addMouseListener(howToPlay);
		window.add(howToPlay);
		window.revalidate();
	}
	
	public void exitGame()
	{   window.remove(title);
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		int kq=JOptionPane.showConfirmDialog(null, "Do you want to exit game?","Notification",JOptionPane.YES_NO_OPTION);
		if(kq==0)
		{	
			System.exit(0);
		}else {
			backToMenu();
		}

	}
	public static void main(String[] args) {
		new Window();
		
	}

}
