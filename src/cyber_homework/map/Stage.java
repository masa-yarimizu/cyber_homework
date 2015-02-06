package cyber_homework.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


/**
 * マインスイーパのマップのクラスです。
 * @author Masayuki Yarimizu
 *
 */

public class Stage {
	
	private ArrayList<ArrayList<String>> stage;
	private int stageRowNumber;
	private int stageColumnNumber;
	private int mineNumber;
	private String columnMax;
	private int rowMax;
	private int plainNumber;
	private String[] minePoint;
	private HashMap<String, Integer> answerMap;
	
	public String getStageElement(String column,int row) {
		return String.valueOf(stage.get(row + 1).get(stage.get(0).indexOf(column)));
	}
	
	public void setStageElement(String column, int row,String a) {
		stage.get(row + 1).set(stage.get(0).indexOf(column), a);
	}
	
	public Integer getPlainNumber() {
		return plainNumber;
	}
	
	public void setPlainNumber(int plainCount) {
		this.plainNumber = plainCount;
	}
	public void setPlainNumber() {
		this.plainNumber = this.stageColumnNumber*this.stageRowNumber - this.mineNumber;
	}
	
	public String getMinePoint() {
		return Arrays.toString(minePoint);
	}
	
	public Integer getRowMax() {
		return rowMax;
	}
	
	public void setRowMax() {
		this.rowMax = this.stageRowNumber - 1;
	}
	
	public String getColumnMax() {
		return columnMax;
	}
	
	public void setColumnMax(String column) {
		this.columnMax = column;
	}
	
	public void setStageColumnNumber(int columnNumber) {
		this.stageColumnNumber = columnNumber;
	}
	
	public Integer getStageColumnNumber() {
		return stageColumnNumber;
	}
	
	public void setStageRowNumber(int rowNumber) {
		this.stageRowNumber = rowNumber;
	}
	
	public Integer getStageRowNumber() {
		return stageRowNumber;
	}
	
	public void setMineNumber(int mineNumber) {
		this.mineNumber = mineNumber;
	} 
	
	public Integer getMineNumber() {
		return mineNumber;
	}
	
	public void setMinePoint(int mineNumber) {
		this.minePoint = new String[mineNumber];
	}
	
	public Integer getAroundMineNumber(String input) {
		if (answerMap.get(input) == null) {
			return 9;
		} else {
			return answerMap.get(input);
		}
	}
	
	
	public void setDefoultOption(int rowNumber, int columnNumber, int mineNumber) {
		this.stageRowNumber = rowNumber;
		this.stageColumnNumber = columnNumber;
		this.mineNumber = mineNumber;
	}
	
	/**
	 * 画面に表示するマップの作成
	 * @return
	 */
	
	public void createStage() {
		this.stage = new ArrayList<ArrayList<String>>();
		ArrayList<String> header = new ArrayList<String>();
		//マップのヘッダーを作成
		char alph = 'a';
		header.add("  ");
		for (int i = 0; i < stageColumnNumber; i++) {
			header.add(String.valueOf(alph++));
		}
		//列の最大値を設定
		setColumnMax(String.valueOf(--alph));
		//列の最大値を設定
		setRowMax();
		//this.columnMax = String.valueOf(--alph);
		//ヘッダーを格納
		stage.add(header);
		//開かれていない部分を"？"で埋める。
		for (int i = 0; i < stageRowNumber; i++) {
			ArrayList<String> row = new ArrayList<String>();
			if (i < 10) {
				row.add(" " + String.valueOf(i));
			} else {
				row.add(String.valueOf(i));
			}
			for (int j = 0; j < stageColumnNumber; j++) {
				row.add("?");
			}
			stage.add(row);
		}
		//平地を作成
		setPlainNumber();
		setMinePoint(this.mineNumber);
		//地雷を作成
		createMine();
		//答えの配列を作成
		createAnswerMap();
	}
	
	/**
	 * 画面に表示するマップの描画
	 */
	public void showStage() {
		for (ArrayList<String> test: this.stage) {
			System.out.println(String.join(" ", test));
		}
	}
	
	/**
	 * 地雷の位置を決めるメソッド
	 */
	private void createMine() {
		//マップの全マスを保管したリストを作成
		ArrayList<String> allPoint = new ArrayList<String>();
		char alph = 'a';
		for (int i = 0; i < stageColumnNumber; i++ ) {
			for (int j = 0; j < stageRowNumber; j++) {
				allPoint.add(String.valueOf(alph) + String.valueOf(j));
			}
			alph++;
		}
		//マップの全マスを保管したリストのシャッフルして先頭から取り出す。
		Collections.shuffle(allPoint);
		for (int k = 0; k < mineNumber; k ++) {
			minePoint[k] = allPoint.get(k);
		}
	}
	/**
	 *　マスの周囲の地雷の数をハッシュマップに登録するメソッド
	 */
	private void createAnswerMap() {
		try {
			//全マスの行列をハッシュマップに登録
			answerMap = new HashMap<String, Integer>();
			char alph = 'a';
			for (int i = 0; i < stageColumnNumber; i++) {
				for (int j = 0; j < stageRowNumber; j++) {
					answerMap.put(String.valueOf(alph) + String.valueOf(j), 0);
				}
				alph++;
			}
			//ハッシュマップにマスの周囲の地雷の数を登録する
			for (int k = 0; k < mineNumber; k++) {
				char columnAlph = minePoint[k].charAt(0);
				String column = minePoint[k].substring(0,1);
				int row = Integer.parseInt(minePoint[k].substring(1, 2));
				int counter;
				if (row == 0) {
					if (column.equals("a")) {
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
						counter = answerMap.get(String.valueOf(++columnAlph) + String.valueOf(row));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
					} else if (column.equals(columnMax)) {
						counter = answerMap.get(String.valueOf(--columnAlph) + String.valueOf(row));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
						counter = answerMap.get(String.valueOf(++columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
					} else {
						counter = answerMap.get(String.valueOf(--columnAlph) + String.valueOf(row));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
						counter = answerMap.get(String.valueOf(++columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
						counter = answerMap.get(String.valueOf(++columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row), ++counter);
					}
				} else if (row == rowMax) {
					if (column.equals("a")) {
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
						counter = answerMap.get(String.valueOf(++columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row), ++counter);
					} else if (column.equals(columnMax)) {
						counter = answerMap.get(String.valueOf(--columnAlph) + String.valueOf(row));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
						counter = answerMap.get(String.valueOf(++columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
					} else {
						counter = answerMap.get(String.valueOf(--columnAlph) + String.valueOf(row));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
						counter = answerMap.get(String.valueOf(++columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
						counter = answerMap.get(String.valueOf(++columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row), ++counter);
					}
				} else {
					if (column.equals("a")) {
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
						counter = answerMap.get(String.valueOf(++columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
					} else if (column.equals(columnMax)) {
						counter = answerMap.get(String.valueOf(--columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
						counter = answerMap.get(String.valueOf(++columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
					} else {
						counter = answerMap.get(String.valueOf(--columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
						counter = answerMap.get(String.valueOf(++columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
						counter = answerMap.get(String.valueOf(++columnAlph) + String.valueOf(row - 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row - 1), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row), ++counter);
						counter = answerMap.get(String.valueOf(columnAlph) + String.valueOf(row + 1));
						answerMap.put(String.valueOf(columnAlph) + String.valueOf(row + 1), ++counter);
					}
				}
			}
		} catch (NumberFormatException e) {
			System.out.println(e);
		}
			
	}
	
}
