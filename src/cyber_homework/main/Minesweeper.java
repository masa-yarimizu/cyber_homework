package cyber_homework.main;


import cyber_homework.game.Game;

/**
 * マインスイーパを実行するクラスです。
 * @author utprot1
 *
 */
public class Minesweeper {
	
	public static void main(String[] args) {
		
		Game mgame = new Game();
		mgame.startGame();
	}
}
