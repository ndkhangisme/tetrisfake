import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
public class DataHandler {
	private static Board board;
	public static void load() {
		File file = new File("res/data/score.txt");
		
		try {
			Scanner sc = new Scanner(file);
			board.highScore = sc.nextInt();
			sc.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void save() {
		File file = new File("res/data/score.txt");
		
		try {
			OutputStream str = new FileOutputStream(file);
			try {
				str.write(Integer.toString(board.highScore).getBytes());
				str.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	}
}
