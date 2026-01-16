package tao.trading.setup;

import tao.trading.QuoteData;

public class BarTrendBarDTO {
	public QuoteData trendBar;
	public int direction;
	public boolean newTrend;
	public double triggerPrice;
	public int triggerPos;
	public String action;
	public int begin;
	public int end;
	public int trendType;

	public double open;
	public double close;
	public double high;
	public double low;
	
	public double triggerBarSize;
	public double avg4BarSize;
	
	public BarTrendBarDTO( int trend, QuoteData trendBar) {
		this.trendBar = trendBar;
		this.direction = trend;
	}

	public BarTrendBarDTO( int trendType, int trend, QuoteData trendBar) {
		this.trendBar = trendBar;
		this.direction = trend;
		this.trendType = trendType;
	}

	public BarTrendBarDTO( String action, QuoteData trendBar) {
		super();
		this.trendBar = trendBar;
		this.action = action;
	}

	public BarTrendBarDTO(int direction) {
		super();
		this.direction = direction;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}
	
	public int getSize( QuoteData[] quotes, double PIP_SIZE){
		return (int) Math.abs((quotes[begin].open - quotes[end].close)/PIP_SIZE);
	}

	public double getSize(){
		return  Math.abs(high - low);
	}
	
	public int getLength(){
		return end - begin + 1;
	}

	public String toString(QuoteData[] quotes, double PIP_SIZE) {
		return "BarTrendBarDTO [direction=" + direction + ", newTrend=" + newTrend + ", begin=" + quotes[begin].time + ", end=" + quotes[end].time
				+ ", open=" + quotes[begin].open + ", close=" + quotes[end].close + ", size=" + getSize(quotes, PIP_SIZE) + ", length=" + getLength() +"]";
	}

	
}
