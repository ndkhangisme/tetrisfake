import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Shape {
	
	private int color;
	
	private int x, y; 
	
	private long time, lastTime;
	
	private int normal = 600, fast = 50;
	
	private int delay;
	
	private BufferedImage block;
	
	private int[][] coords;
	
	private int[][] reference;
	
	private int deltaX;
	
	private static Board board;
	
	private boolean collision = false, moveX = false;
	
	public Shape(int[][] coords, BufferedImage block, Board board, int color){
		this.coords = coords;
		this.block = block;
		this.board = board;
		this.color = color;
		deltaX = 0;
		x = 4;
		y = 0;
		delay = normal;
		time = 0;
		lastTime = System.currentTimeMillis();
		reference = new int[coords.length][coords[0].length];
		
		System.arraycopy(coords, 0, reference, 0, coords.length);
		
	}
	
	public void update(){
		moveX = true;
		time += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
		
		if(collision)
		{
			for(int row = 0; row < coords.length; row ++)
			{
				for(int col = 0; col < coords[0].length; col ++)
				{
					if(coords[row][col] != 0)
						board.getBoard()[x + col][y + row] = color;
				}
			}
			
			checkFullRow(1);
			board.setCurrentShape();
		}
		
		if(!(x + deltaX + coords[0].length > 10) && !(x + deltaX < 0))
		{
			
			for(int row = 0; row < coords.length; row++)
			{
				for(int col = 0; col < coords[row].length; col ++)
				{
					if(coords[row][col] != 0)
					{
						if(board.getBoard()[x + deltaX + col][y + row] != 0)
						{
							moveX = false;
						}
						
					}
				}
			}
			
			if(moveX)
				x += deltaX;
			
		}
		
		if(!(y + 1 + coords.length > 20))
		{
			
			for(int row = 0; row < coords.length; row++)
			{
				for(int col = 0; col < coords[row].length; col ++)
				{
					if(coords[row][col] != 0)
					{
						
						if(board.getBoard()[x +  col][y + 1 + row] != 0)
						{
							collision = true;
						}
					}
				}
			}
			if(time > delay)
				{
					y++;
					time = 0;
				}
		}else{
			collision = true;
		}
		
		deltaX = 0;
	}
	
	public void render(Graphics g){
		
		for(int row = 0; row < coords.length; row ++)
		{
			for(int col = 0; col < coords[0].length; col ++)
			{
				if(coords[row][col] != 0)
				{
					g.drawImage(block, col*30 + x*30, row*30 + y*30, null);	
				}
			}		
		}

	}
	
	public static void checkFullRow(int multiplier) {
		int blocksInRow = 0;

		for (int y = board.getBoard()[0].length - 1; y > 0; y--) {
			for (int x = 0; x < board.getBoard().length; x++) {
				if (board.getBoard()[x][y] > 0) {
					blocksInRow++;
					
				}
				
			}
			if (blocksInRow == 10) {
				board.scoreToAdd += (10 * multiplier);
				delRow(y, multiplier);
				break;
			} else {
				blocksInRow = 0;
			}

		}

		board.score += board.scoreToAdd;
		board.scoreToAdd = 0;

		if (board.score > board.highScore) {
			board.highScore = board.score;
			DataHandler.save();
		}
	}
	
	private static void delRow(int row, int multiplier) {

		for (int i = 0; i < board.getBoard().length; i++) {
			board.getBoard()[i][row] = 0;
			
		}
		//System.out.println("---");
		for (int y = row; y > 1; y--) {
			for (int x = 0; x < board.getBoard().length; x++) {
				board.getBoard()[x][y] = board.getBoard()[x][y - 1];
			}

		}
		
		checkFullRow(multiplier + 1);
	}
	
	
	public  void rotateShape()
	{
		
		int[][] rotatedShape = null;
		
		rotatedShape = transposeMatrix(coords);
		
		rotatedShape = reverseRows(rotatedShape);
		
		if((x + rotatedShape[0].length > 10) || (y + rotatedShape.length > 20))
		{
			return;
		}
		
		for(int row = 0; row < rotatedShape.length; row++)
		{
			for(int col = 0; col < rotatedShape[row].length; col ++)
			{
				if(rotatedShape[row][col] != 0)
				{
					if(board.getBoard()[x + col][y + row] != 0)
					{
						return;
					}
				}
			}
		}
		coords = rotatedShape;
	}

	
    private int[][] transposeMatrix(int[][] matrix){
        int[][] temp = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                temp[j][i] = matrix[i][j];
        return temp;
    }


	
	private int[][] reverseRows(int[][] matrix){
		
		int middle = matrix.length/2;
		
		
		for(int i = 0; i < middle; i++)
		{
			int[] temp = matrix[i];
			
			matrix[i] = matrix[matrix.length - i - 1];
			matrix[matrix.length - i - 1] = temp;
		}
		
		return matrix;
		
	}
	
	
	public int getColor(){
		return color;
	}
	
	public void setDeltaX(int deltaX){
		this.deltaX = deltaX;
	}
	
	public void speedUp(){
		delay = fast;
	}
	
	public void speedDown(){
		delay = normal;
	}
	
	public BufferedImage getBlock(){
		return block;
	}
	
	public int[][] getCoords(){
		return coords;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
}
