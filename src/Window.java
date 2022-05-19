import javax.swing.JFrame;

public class Window{
	//413
	public static final int WIDTH = 450, HEIGHT = 637;
	
	public static Board board;
	public static Title title;
	public static JFrame window;
	
	public Window(){
		
		window = new JFrame("Tetris");
		window.setSize(WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setResizable(false);	
		
		board = new Board(this);
		title = new Title(this);
		
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
		window.addMouseMotionListener(title);
		window.addMouseListener(title);
		window.add(title);
		window.setVisible(true);
	}
	public static void main(String[] args) {
		new Window();
	}

}
