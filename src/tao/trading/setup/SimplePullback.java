package tao.trading.setup;

import tao.trading.Constants;
import tao.trading.Indicator;
import tao.trading.Pattern;
import tao.trading.QuoteData;
import tao.trading.Trade;
import tao.trading.dao.FirstPullBackBreakOutDAO;
import tao.trading.strategy.util.BarUtil;
import tao.trading.strategy.util.Utility;

public class SimplePullback
{
	public static int detect(String symbol, QuoteData[] quotes)
	{
		int lastbar = quotes.length - 1;

		if ( BarUtil.isUpBar(quotes[lastbar-1])){
			int upbarStart = lastbar-1;
			while (BarUtil.isUpBar(quotes[upbarStart-1]))
					upbarStart--;
			
			//only allow 2 upbars max
			if ( upbarStart < lastbar-2 )
				return  Constants.DIRECTION_UNKNOWN;
			
			int downbarEnd = upbarStart -1;
			int downbarStart = downbarEnd;
			
			while ( BarUtil.isDownBar(quotes[downbarStart])) {
				downbarStart--;
			}
			
			double breakOutDownSize = quotes[downbarStart].high - quotes[downbarEnd].low;
			double avgBarSize = BarUtil.averageBarSize(quotes, lastbar-24, lastbar-1);
			if ( breakOutDownSize < 2 * avgBarSize )
				return Constants.DIRECTION_UNKNOWN;
			
			int pullbackLowPoint = Utility.getLow( quotes, downbarEnd, upbarStart ).pos;
			int pullbackHighPoint = Utility.getHigh(quotes,upbarStart, lastbar).pos;
			double pullBackSize = quotes[pullbackHighPoint].high - quotes[pullbackLowPoint].low;
			
			if ( pullBackSize < breakOutDownSize * 0.5 ) {
				System.out.println(symbol + " BreakOutDown: " + quotes[downbarStart].time + "-" + quotes[downbarEnd].time + " size:" + breakOutDownSize);
				return Constants.DIRECTION_DOWN;
			}
			
		}	
		else if (BarUtil.isDownBar(quotes[lastbar-1])){
			int downbarStart = lastbar-1;
			while (BarUtil.isDownBar(quotes[downbarStart-1]))
				downbarStart--;
			
			//only allow 2 downbars max
			if ( downbarStart < lastbar-2 )
				return  Constants.DIRECTION_UNKNOWN;
			
			int upbarEnd = downbarStart -1;
			int upbarStart = upbarEnd;
			
			while ( BarUtil.isUpBar(quotes[upbarStart])) {
				upbarStart--;
			}
			
			double breakOutUpSize =  quotes[upbarEnd].high - quotes[upbarStart].low;
			double avgBarSize = BarUtil.averageBarSize(quotes, lastbar-24, lastbar-1);
			if ( breakOutUpSize < 2 * avgBarSize )
				return Constants.DIRECTION_UNKNOWN;
			
			//System.out.println("11111");
			
			int pullbackHighPoint = Utility.getHigh( quotes, upbarEnd, downbarStart ).pos;
			int pullbackLowPoint = Utility.getLow(quotes,downbarStart, lastbar).pos;
			double pullBackSize = quotes[pullbackHighPoint].high - quotes[pullbackLowPoint].low ;

			//System.out.println(pullBackSize + " " + breakOutUpSize);

			
			if ( pullBackSize < breakOutUpSize * 0.5 ) {
				System.out.println(symbol + " BreakOutUp: " + quotes[upbarStart].time + "-" + quotes[upbarEnd].time + " size:" + breakOutUpSize);
				return Constants.DIRECTION_UP;
			}
		}
		
		return Constants.DIRECTION_UNKNOWN;
	}


	
}
