package SudokuLogic;

public class Test 
{
	public static void main(String[] args) {
		Game b1 = new Game(Difficulty.EASY);
		System.out.println(b1.toString() + "\n");
		Game b2 = new Game(Difficulty.MEDIUM);
		System.out.println(b2.toString() + "\n");
		Game b3 = new Game(Difficulty.HARD);
		System.out.println(b3.toString() + "\n");
	}
}
