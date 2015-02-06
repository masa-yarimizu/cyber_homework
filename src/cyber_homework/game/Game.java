package cyber_homework.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cyber_homework.map.Stage;
/**
 * マインスイーパのゲームを処理するクラスです
 * @author Masayuki Yarimizu
 *
 */
public class Game {
	
	private final int INPUT_NUMBER = 3;
	private final int MAP_MINIMUM = 5;
	private final int MAP_MAXIMUM = 26;
	private final int MINE_MINIMUM = 5;
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	public void startGame() {
		
		String columnInput;
		String rowInput;
		String mineInput;
		int columnNumber;
		int rowNumber;
		int mineNumber;
		int mineMax;
		String input;
		String inputColumn;
		int inputRow;
		String inputCommand;
		
		try {
			System.out.println("マップの高さを入力してください。(5-26)");
			while (true) {
				rowInput = reader.readLine();
				if (isMapSettingCorrect(rowInput)) {
					rowNumber = Integer.parseInt(rowInput);
					break;
				}
				System.out.println("5から26の数字を入力してください。");
			}

			System.out.println("マップの幅を入力してください。(5-26)");
			while (true) {
				columnInput = reader.readLine();
				if (isMapSettingCorrect(columnInput)) {
					columnNumber = Integer.parseInt(columnInput);
					break;
				}
				System.out.println("5から26の数字を入力してください。");
			}

			mineMax = (int)(rowNumber*columnNumber*0.2);
			System.out.println("地雷の数を指定してください(5-" + mineMax + ")");
			while (true) {
				mineInput = reader.readLine();
				if (isMineNumberCorrect(mineInput, mineMax)) {
					mineNumber = Integer.parseInt(mineInput);
					break;
				}
				System.out.println("規定の範囲内の地雷の数を入力してください。");
			}
			
			//マップの作成
			Stage mstage = new Stage();
			mstage.setDefoultOption(rowNumber, columnNumber, mineNumber);
			mstage.createStage();
			System.out.println("ゲームスタート！");
			//時間測定
			Timer timer = new Timer();
			timer.setTime();

			while(!isGameCleared(mstage.getPlainNumber())){
				
				//マップの表示
				mstage.showStage();
				
				System.out.println("選択する列・行を選んでください");
				//行列入力チェック
				while (true) {
					input = reader.readLine();
					if (isInputMatrix(input,mstage)) {
						inputColumn = input.substring(0, 1);
						inputRow = Integer.parseInt(input.substring(2-1));
						break;
					}
				}
				
				System.out.println("開くならoを、地雷チェックはxを入力してください。");
				//入力チェック
				while (true) {
					inputCommand = reader.readLine();
					if (isInputCorrect(inputCommand)) {
						break;
					}
					System.out.println("oかxを入力してください。");
				}
				
				switch (inputCommand) {
					case "o":
						if (mstage.getStageElement(inputColumn, inputRow).equals("X")) {
							System.out.println("開けません。チェックを外してください。");
							break;
						} else if (!mstage.getStageElement(inputColumn, inputRow).equals("?")) {
							System.out.println("既に開いたマスです。");
							break;
						} else if (mstage.getMinePoint().indexOf(input) != -1) { //地雷チェック
								System.out.println("地雷を踏みました。ゲームオーバー。");
								return;
						} else {
							//周囲の地雷の数を表示
							int aroundNumber = mstage.getAroundMineNumber(input);
							if (aroundNumber == 0) {
								mstage.setStageElement(inputColumn, inputRow, " ");
								mstage.setPlainNumber(mstage.getPlainNumber() -1);
								openNextSquare(inputColumn, inputRow, mstage);
								break;
							} else {
								mstage.setStageElement(inputColumn, inputRow, String.valueOf(aroundNumber));
								mstage.setPlainNumber(mstage.getPlainNumber() -1);
								break;
							}
						}
					case "x":
						if (mstage.getStageElement(inputColumn, inputRow).equals("X")) {
							mstage.setStageElement(inputColumn, inputRow, "?");
						} else if (!mstage.getStageElement(inputColumn, inputRow).equals("?")) {
							System.out.println("既に開いたマスです。");
							break;
						} else {
							mstage.setStageElement(inputColumn, inputRow, "X");
				
						}
				}
				

			}
			
			System.out.println("クリア時間は"+ timer.getPastTime() + "でした。");
			
		} catch (IOException e) {
			System.out.println(e);
		} catch (NumberFormatException e) {
			System.out.println(e);
		}
		
	}
	
	/**
	 * 行列入力チェックをするメソッド
	 * @param input
	 * @return
	 */
	public boolean isInputMatrix(String input, Stage stage) {
		if (input.length() > INPUT_NUMBER) {
			System.out.println("行・列を正しく入力してください。");
			return false;
		}
		
		if (!isRowCorrect(input, stage)) {
			System.out.println("行が有効ではありません。再度入力してください");
			return false;
		}
		if (!isColumnCorrect(input, stage)){
			System.out.println("列が有効でありません。再度入力してください");
			return false;
		}
		return true;
	}
	
	/**
	 * マップの設定の入力をチェックするメソッド
	 * @param input
	 * @return
	 */
	public boolean isMapSettingCorrect(String input) {
		if (!isInputInteger(input)) {
			return false;
		}
		if (Integer.parseInt(input) < MAP_MINIMUM || Integer.parseInt(input) > MAP_MAXIMUM) {
			return false;
		}
		return true;
	}
	
	/**
	 * 地雷の数が問題ないかチェックするメソッド
	 * @param mineInput
	 * @param mineMax
	 * @return
	 */
	public boolean isMineNumberCorrect(String mineInput, int mineMax) {
		if (!isInputInteger(mineInput)) {
			return false;
		}
		if (Integer.parseInt(mineInput) < MINE_MINIMUM || Integer.parseInt(mineInput) > mineMax) {
			return false;
		}
		 return true;
	}	
	/**
	 * ユーザーの行列入力の行部分が有効であるかの判定をするメソッド
	 * @param input
	 * @return
	 */
	
	public boolean isInputInteger(String rowInput) {
		String regex = "^[0-9]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m1 = p.matcher(rowInput);
		return m1.find();
	}
	
	/**
	 * ユーザーの行列入力の列部分が有効であるか(マップの範囲にあるか)の判定をするメソッド
	 * @param input
	 * @param stage
	 * @return
	 */
	public boolean isColumnCorrect(String input,Stage stage) {
		String column = input.substring(0, 1);
		String[] alphList = new String[stage.getStageColumnNumber()];
		char alph = 'a';
		for (int i = 0; i < stage.getStageColumnNumber(); i++) {
			alphList[i] = String.valueOf(alph++);
		}
		if (!Arrays.asList(alphList).contains(column)) {
			return false;
		}
		return true;
	}
	
	/**
	 * ユーザーの行列入力の行部分が有効であるか(マップの範囲にあるか)の判定をするメソッド
	 * @param input
	 * @param stage
	 * @return
	 */
	public boolean isRowCorrect(String input, Stage stage) {
		if ( input.length() != 2 || !isInputInteger(input.substring(2-1))) {
			return false;
		}
		String row = input.substring(2-1);
		String[] numberList = new String[stage.getStageRowNumber()];
		for (int i = 0; i < stage.getStageRowNumber(); i++) {
			numberList[i] = String.valueOf(i);
		}
		if (!Arrays.asList(numberList).contains(row)) {
			return false;
		}
		return true;
	}
	
	/**
	 * ユーザの操作が問題ないかチェックするメソッド
	 * @param input
	 * @return
	 */
	
	public boolean isInputCorrect(String input) {
		if (input.equals("o") || input.equals("x")) {
			return true;
		} else {
			System.out.println("oかxを入力してください");
		}
		return false;
	}
	
	/**
	 * ゲームクリア条件を確認するメソッド
	 * @param plainCount
	 * @return
	 */
	public boolean isGameCleared(int plainCount) {
		if (plainCount == 0) {
			System.out.println("ゲームクリアです。おめでとうございます。");
			return true;
		}
		return false;
	}
	
	/**
	 * 周囲に地雷がないマスの隣接するマスを自動的に開くメソッド
	 * @param column
	 * @param row
	 * @param stage
	 */
	private void openNextSquare(String column, int row, Stage stage) {

		char point = column.charAt(0);
		char pointLeft = --point;
		char pointCenter = ++point;
		char pointRight = ++point;
		int aroundNumber;
		
		String point_0 = String.valueOf(pointLeft) + String.valueOf(row - 1);
		String point_1 = String.valueOf(pointLeft) + String.valueOf(row);
		String point_2 = String.valueOf(pointLeft) + String.valueOf(row + 1);
		String point_3 = String.valueOf(pointCenter) + String.valueOf(row - 1);
		String point_4 = String.valueOf(pointCenter) + String.valueOf(row + 1);
		String point_5 = String.valueOf(pointRight) + String.valueOf(row - 1);
		String point_6 = String.valueOf(pointRight) + String.valueOf(row);
		String point_7 = String.valueOf(pointRight) + String.valueOf(row + 1);
		
		
		for (int i = 0; i < 8; i++) {
			
			if (i < 3) {		
				aroundNumber = stage.getAroundMineNumber(String.valueOf(pointLeft) + String.valueOf((row - 1) + i));
				if (aroundNumber == 0) {
					if (!stage.getStageElement(String.valueOf(pointLeft), (row - 1) + i).equals("?")) {
						continue;
					} else {
						stage.setStageElement(String.valueOf(pointLeft), (row - 1) + i, " ");
						stage.setPlainNumber(stage.getPlainNumber() - 1);
						openNextSquare(String.valueOf(pointLeft), (row - 1) + i, stage);
					}
				} else if (aroundNumber == 9) {
					continue;
				} else {
					if (!stage.getStageElement(String.valueOf(pointLeft), (row-1) + i).equals("?")) {
						continue;
					} else {
						stage.setStageElement(String.valueOf(pointLeft), (row - 1) + i, String.valueOf(aroundNumber));
						stage.setPlainNumber(stage.getPlainNumber() - 1);
					}
				}
			} else if (i == 3) {
				aroundNumber = stage.getAroundMineNumber(String.valueOf(pointCenter) + String.valueOf(row - 1));

				if (aroundNumber == 0) {
					if (!stage.getStageElement(String.valueOf(pointCenter), (row-1)).equals("?")) {
						continue;
					} else {
						stage.setStageElement(String.valueOf(pointCenter), (row - 1), " ");
						stage.setPlainNumber(stage.getPlainNumber() - 1);
						openNextSquare(String.valueOf(pointCenter), (row - 1), stage);
					}
				} else if (aroundNumber == 9) {
					continue;
				} else {
					if (!stage.getStageElement(String.valueOf(pointCenter), (row - 1)).equals("?")) {
						continue;
					} else {
						stage.setStageElement(String.valueOf(pointCenter), (row - 1), String.valueOf(aroundNumber));
						stage.setPlainNumber(stage.getPlainNumber() - 1);
					}
				}
			} else if (i == 4) {
				aroundNumber = stage.getAroundMineNumber(String.valueOf(pointCenter) + String.valueOf(row + 1));
				if (aroundNumber == 0) {
					if (!stage.getStageElement(String.valueOf(pointCenter), (row + 1)).equals("?")) {
						continue;
					} else {
						stage.setStageElement(String.valueOf(pointCenter), (row + 1), " ");
						stage.setPlainNumber(stage.getPlainNumber() - 1);
						openNextSquare(String.valueOf(pointCenter), (row + 1), stage);
					}
				} else if (aroundNumber == 9) {
					continue;
				} else {
					if (!stage.getStageElement(String.valueOf(pointCenter), (row + 1)).equals("?")) {
						continue;
					} else {
						stage.setStageElement(String.valueOf(pointCenter), (row + 1), String.valueOf(aroundNumber));
						stage.setPlainNumber(stage.getPlainNumber() - 1);
					}
				}

			} else {
				aroundNumber = stage.getAroundMineNumber(String.valueOf(pointRight) + String.valueOf((row - 6) + i));
				if (aroundNumber == 0) {
					if (!stage.getStageElement(String.valueOf(pointRight), (row - 6) + i).equals("?")) {
						continue;
					} else {
						stage.setStageElement(String.valueOf(pointRight), (row - 6) + i, " ");
						stage.setPlainNumber(stage.getPlainNumber() - 1);
						openNextSquare(String.valueOf(pointRight), (row - 6) + i, stage);
					}
				} else if (aroundNumber == 9) {
					continue;
				} else {
					if (!stage.getStageElement(String.valueOf(pointRight), (row - 6) + i).equals("?")) {
						continue;
					} else {
						stage.setStageElement(String.valueOf(pointRight), (row - 6) + i, String.valueOf(aroundNumber));
						stage.setPlainNumber(stage.getPlainNumber() - 1);
					}
				}
			}
		}
		
	}
}
