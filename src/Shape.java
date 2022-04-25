import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Shape {
	
	private BufferedImage block;
	private int[][] coords;
	private Board board;
	
	private int deltaX = 0;
	
	private int color;
	
	private int x, y;
	
	private boolean collision = false ;
	private boolean moveX = false ;
	private int nomalSpeed = 600;
	private int fast = 50;
	private int currentSpeed;
	
	private long time, lastTime;
	
	public Shape(BufferedImage block, int[][] coords, Board board, int color) {
		
		this.block = block;
		this.coords = coords;
		this.board = board;
		this.color = color;
		
		currentSpeed = nomalSpeed;
		time = 0;
		lastTime = System.currentTimeMillis();
		
		x = 4;
		y = 0;
	}
	
	public void update() {
		time += System.currentTimeMillis() - lastTime; 
		lastTime = System.currentTimeMillis();
		
		if(collision) {
			
			for(int row = 0; row < coords.length; row++) {
				for(int col = 0; col < coords[row].length; col++) {
					if(coords[row][col] != 0) {
						board.getBoard()[y + row][x + col] = color;
					}
				}
			}
			
			checkLine();
			board.addScore();
			//checkFullRow(1);
			board.setNextShape();
		}
		
		
		
		if(!(x + deltaX + coords[0].length > 10) && !(x + deltaX < 0)) {
			
			for(int row = 0; row < coords.length; row++) {
				for(int col = 0; col < coords[row].length; col++) {
					if(coords[row][col] != 0) {
						if(board.getBoard()[y + row][x + deltaX + col] != 0) {
							moveX = false;
						}
					}
				}
			}
			
			if(moveX) {
				x += deltaX;
			}
			
		}
		
		if(!(y + 1 + coords.length > 20)) {
			
			for(int row = 0; row < coords.length; row++) {
				for(int col = 0; col < coords[row].length; col++) {
					if(coords[row][col] != 0) {
						if(board.getBoard()[y + row + 1][col + x] != 0) {
							collision = true;
						}
					}
				}
			}
			
			
			if(time > currentSpeed) {
			
				y++;
				time = 0;
			}
		}else{
			collision = true;
		}
		
		
				
		
		deltaX = 0;
		moveX = true;
	}
	
	public void render(Graphics g) {
		
		for(int row = 0; row < coords.length; row ++) {
			for(int col = 0; col < coords[row].length; col++) {
				if(coords[row][col] != 0) {
					g.drawImage(block,col * board.getBlocksSize() + x * board.getBlocksSize(), row * board.getBlocksSize() + y * board.getBlocksSize(), null);
					
				}
			}
		}
	}
	
	private void checkLine() {
		int score = 0;
		int height = board.getBoard().length - 1;
		
		for(int i = height; i > 0; i--) {
			int count = 0;
			for(int j = 0; j < board.getBoard()[0].length; j++) {
				if(board.getBoard()[i][j] != 0) {
					count ++;
				}
				board.getBoard()[height][j] = board.getBoard()[i][j];
			}
			if(count < board.getBoard()[0].length) {
				height--;
			}
		}

		
	}
	
	public void rotateShape() {
	
		if(collision) {
			return;
		}
		
		int[][] rotatedShape = null;
		
		rotatedShape = getTransposeMatrix(coords);
		
		rotatedShape = getReverseMatrix(rotatedShape);
		
		if ((x + rotatedShape[0].length > 10) || (y + rotatedShape.length > 20)) {
            return;
        }

        for (int row = 0; row < rotatedShape.length; row++) {
            for (int col = 0; col < rotatedShape[row].length; col++) {
                if (rotatedShape[row][col] != 0) {
                    if(board.getBoard()[y + row][x + col] != 0) {
                    	return;
                    }
                }
            }
        }
        coords = rotatedShape;
	}
	
	private int[][] getTransposeMatrix(int[][] matrix){
		int[][] temp = new int[matrix[0].length][matrix.length];
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++) {
				temp[j][i] = matrix[i][j];
			}
		}
		return temp;
	} 
	
	private int[][] getReverseMatrix(int[][] matrix){
		
		int middle = matrix.length / 2;
		
		for(int i = 0; i < middle; i++) {
			int[] temp = matrix[i];
			matrix[i] = matrix[matrix.length - i - 1];
			matrix[matrix.length - i - 1] = temp;
		}
		return matrix;
	} 
	
	public void setDeltaX(int deltaX) {
		this.deltaX = deltaX;
	}
	
	public void nomalSpeed() {
		currentSpeed = nomalSpeed;
	}
	
	public void fast() {
		currentSpeed = fast;
	}

	public BufferedImage getBlock() {
		return block;
	}

	public int[][] getCoords() {
		return coords;
	}
	
	public int getColor() {
		return color;
	}
	
}
