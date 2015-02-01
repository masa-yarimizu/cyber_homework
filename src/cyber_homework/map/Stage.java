package cyber_homework.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/**
 * 
 * @author Masayuki Yarimizu
 *
 */

public class Stage {
	
	private ArrayList<ArrayList<String>> stage;
	private int stageRowNumber;
	private int stageColumnNumber;
	private int mineNumber;
	private String columnMax;
	private int rowMax = stageRowNumber - 1;
	private int plainState;
	//private String[] minePoint = new String[mineNumber];
	private String[] minePoint;
	
	public String getStageElement(String column,int row) {
		return String.valueOf(stage.get(row + 1).get(stage.get(0).indexOf(column)));
	}
	
	public void setStageElement(String column, int row,String a) {
		stage.get(row + 1).set(stage.get(0).indexOf(column), a);
	}
	
	public Integer getPlainState() {
		return plainState;
	}
	
	public void setPlainState(int plainCount) {
		this.plainState = plainCount;
	}
	public void setPlainState() {
		this.plainState = this.stageColumnNumber*this.stageRowNumber - this.mineNumber;
	}
	
	public String getMinePoint() {
		return Arrays.toString(minePoint);
	}
	
	public Integer getRowMax() {
		return rowMax;
	}
	
	public String getColumnMax() {
		return columnMax;
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
		//列の最大値を取得
		this.columnMax = String.valueOf(--alph);
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
		setPlainState();
		setMinePoint(this.mineNumber);
		//地雷を作成
		createMine();

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
	
}
