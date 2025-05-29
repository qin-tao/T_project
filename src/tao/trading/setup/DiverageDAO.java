package tao.trading.setup;

public class DiverageDAO {
	public int direction;
	public double triggerPrice;
	public int breakOutDist;
	public String action;
	public int pushStart;
	public int prePos;
	public int prePos_lowest, prePos_highest;
	public int curPos;
	public int curPosEnd;
	public double pullBackSize;
	public int pullBackWidth;
	
	public DiverageDAO(){
	}
	
	public DiverageDAO(int prePos, int curPos) {
		super();
		this.prePos = prePos;
		this.curPos = curPos;
	}

	public DiverageDAO(int dir, int prePos, int curPos) {
		super();
		this.direction = dir;
		this.prePos = prePos;
		this.curPos = curPos;
		this.curPosEnd = curPos;
	}
	
	
}
