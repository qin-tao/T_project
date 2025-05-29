package tao.trading.strategy.tm;

import tao.trading.Trade;

public class FirstPullBackAfterBreakOutTest {

	public static void main(String[] args) {
		FirstPullBackAfterBreakOutTest m = new FirstPullBackAfterBreakOutTest();
		m.startTest();
	}

	void startTest() {
		FirstPullBackAfterBreakOut pb_stg = new FirstPullBackAfterBreakOut();
		Trade t = pb_stg.detect2_new(data);
	}
	
}
