package tao.trading.setup;

import java.util.ArrayList;
import java.util.Vector;

import tao.trading.Constants;
import tao.trading.Indicator;
import tao.trading.MACD;
import tao.trading.Pattern;
import tao.trading.QuoteData;
import tao.trading.Trade;
import tao.trading.TradeEvent;
import tao.trading.dao.PushHighLow;
import tao.trading.dao.PushList;
import tao.trading.strategy.util.Utility;

public class DiverageSetup {

	
	public static ArrayList<DiverageDAO> detect(QuoteData[] quotesL, double PIP_SIZE){
		int lastbarL = quotesL.length - 1;
		ArrayList<DiverageDAO> hhlls = new ArrayList<DiverageDAO>();
		
		DiverageDAO dv = null;
		DiverageDAO hhll = null;
		
		for ( int i = ((lastbarL-100)<1)?1:(lastbarL-100); i <=lastbarL; i++ ){
			if ( (dv = DiverageSetup.findLastDiverageHigh(quotesL, i-48, i, 2, 0)) != null){
				hhll = dv;
				hhlls.add(hhll);
			}
			if ( (dv = DiverageSetup.findLastDiverageLow(quotesL, i-48, i, 2, 0)) != null){
				hhll = dv;
				hhlls.add(hhll);
			}

			if ( dv == null ){
				if ((quotesL[i].high > quotesL[i-1].high) && ( hhll != null ) && ( hhll.direction == Constants.DIRECTION_UP ) && ( hhll.curPosEnd == i-1)) {
					hhll.curPosEnd = i;
				}
				else if ((quotesL[i].low < quotesL[i-1].low) && ( hhll != null ) && ( hhll.direction == Constants.DIRECTION_DOWN ) && ( hhll.curPosEnd == i-1)) {
					hhll.curPosEnd = i;
				}
			}
		}

		for (DiverageDAO hl: hhlls) {
			if ( hl.direction == Constants.DIRECTION_UP )
				hl.breakOutDist = (int)((quotesL[hl.curPosEnd].high - hl.triggerPrice)/PIP_SIZE);
			else if ( hl.direction == Constants.DIRECTION_DOWN )
				hl.breakOutDist = (int)((hl.triggerPrice - quotesL[hl.curPosEnd].low)/PIP_SIZE);
		}
		
		return hhlls;
	}


	public static void  printDiverages( ArrayList<DiverageDAO> dvs ){
		/*for (DiverageDAO hl: dvs) {
			System.out.println("Direction:" + hl.direction + "\tdistance:" + hl.direction*dist + "\ttriggerPrice:" + hl.triggerPrice + "\tStartPos:" + quotesL[hl.curPos].time + "\tEndPos:" + quotesL[hl.curPosEnd].time );
		}*/
	
	}

	

	
	
	
	static public DiverageDAO macd_diverage(QuoteData data, QuoteData[] quotes, double[] ema, int pushStart, int direction) {
		
		int lastbar = quotes.length - 1;
		PushList pushList = null;
				
		if (direction == Constants.DIRECTION_UP) {

			pushList = PushSetup.getUp2PushList(quotes, pushStart, lastbar);

			if ((pushList != null ) && ( pushList.phls != null ) && ( pushList.phls.length > 0))
			{	
				//System.out.println(pushList.toString(quotesL, PIP_SIZE));
				PushHighLow[] phls = pushList.phls;
				int lastPushIndex = phls.length - 1;
				PushHighLow lastPush = phls[phls.length - 1]; 
				int numOfPush = phls.length;
				double triggerPrice = quotes[lastPush.prePos].high;
				double lastBreakOut1, lastBreakOut2;
				double lastPullBack1, lastPullBack2;
				int largePushIndex = 0;
				PushHighLow phl = lastPush;
				double pullback =  quotes[phl.prePos].high - phl.pullBack.low;
				pushList.calculatePushMACD(quotes);
				int negative = phls[lastPushIndex].pullBackMACDNegative;

			
				if (negative > 0) {
					DiverageDAO diverageDAO = new DiverageDAO();
					diverageDAO.action = Constants.ACTION_SELL;
					diverageDAO.curPos = lastbar;
					diverageDAO.prePos = phl.prePos;
					diverageDAO.pushStart = pushStart;
					return diverageDAO;
				}
			}
		} else if (direction == Constants.DIRECTION_DOWN) {

			pushList = PushSetup.getDown2PushList(quotes, pushStart, lastbar);

			if ((pushList != null ) && ( pushList.phls != null ) && ( pushList.phls.length > 0))
			{	
				//System.out.println(pushList.toString(quotesL, PIP_SIZE));
				PushHighLow[] phls = pushList.phls;
				int lastPushIndex = phls.length - 1;
				PushHighLow lastPush = phls[phls.length - 1]; 
				int numOfPush = phls.length;
				double triggerPrice = quotes[lastPush.prePos].low;
				double lastBreakOut1, lastBreakOut2;
				double lastPullBack1, lastPullBack2;
				int largePushIndex = 0;
				PushHighLow phl = lastPush;
				double pullback = phl.pullBack.high - quotes[phl.prePos].low;
				pushList.calculatePushMACD(quotes);
				int positive = phls[lastPushIndex].pullBackMACDPositive;

				if (positive > 0) {
					DiverageDAO diverageDAO = new DiverageDAO();
					diverageDAO.action = Constants.ACTION_SELL;
					diverageDAO.curPos = lastbar;
					diverageDAO.prePos = phl.prePos;
					diverageDAO.pushStart = pushStart;
					return diverageDAO;
				}
			}
		}
		return null;
	}

	
	/*****************************************************
	 * 
	 * This returns the furthest peak
	 * 
	 ****************************************************/
	public static DiverageDAO findLastDiverageHigh(QuoteData[] quotes, int start, int lastbar, int gapMinWidth, double diverageSize)
    {
		if ( start < 2 )
			start = 2;

		int dStart = lastbar - 1;
    	DiverageDAO dv = null;
    	next: while( --dStart >= start ){

    		// dStart should be lower than the lastbar
    		if ( quotes[dStart].high > quotes[lastbar].high )
    			continue next;
    		
    		// dStart should be the highest other than last bar
    		for ( int i = dStart+1; i < lastbar; i++)
    			if (( quotes[i].high >= quotes[dStart].high) || ( quotes[i].high > quotes[lastbar].high))
    				continue next;

    		//QuoteData lastHigh = Utility.getHigh(quotes, dStart+1, lastbar-1);
    		//if (( lastHigh.high > quotes[dStart].high ) || ( lastHigh.high > quotes[lastbar].high )) 
    		//	continue;
 
    		if ( lastbar - dStart < gapMinWidth+1)
    			continue next;
    		
    		// counting how many bars before are low
    		int nBarLower = 0;
    		int lowestPos = dStart-1;  // lowest point shows pullback
      		for ( int i = dStart-1; i >=0; i--){
    			if ( quotes[i].high <= quotes[dStart].high){
    				nBarLower++;
    				if (quotes[i].low < quotes[lowestPos].low){
    					lowestPos = i;
    				}
    			}
    			else
    				break;
    		}
      		if (nBarLower <= 2) // at least 2 bars before are lower
      			continue next;
    		
    		QuoteData pullBackLow = Utility.getLow(quotes, dStart+1, lastbar-1);
    		if (( diverageSize > 0 ) && (( quotes[dStart].high - pullBackLow.low ) < diverageSize ))
    			continue;
    		
    		//if ( lastbar - dStart > gapMaxWidth)
    		//	continue;

       		//return new DiverageDAO(dStart, lastbar);
    		DiverageDAO dv1 = new DiverageDAO(Constants.DIRECTION_UP, dStart, lastbar);
    		dv1.pullBackSize = quotes[dStart].high - pullBackLow.low;
    		dv1.pullBackWidth = lastbar - dStart+1;
    		dv1.triggerPrice =  quotes[dStart].high;
    		dv1.prePos_lowest = lowestPos;

    		return dv1;
    		//if (( dv == null ) ||  (dv1.triggerPrice > dv.triggerPrice))
    		//	dv = dv1;

    	}
    	
    	return dv;
     }

	
	/*****************************************************
	 * 
	 * This returns the furthest peak
	 * 
	 ****************************************************/
	public static DiverageDAO findLastDiverageLow(QuoteData[] quotes, int start, int lastbar, int gapMinWidth, double diverageSize )
    {
		if ( start < 2 )
			start = 2;

		int dStart = lastbar - 1;
    	DiverageDAO dv = null;
    	next: while( --dStart >= start ) {

    		// dStart should be lower than the lastbar
    		if ( quotes[dStart].low < quotes[lastbar].low )
    			continue next;
    		
    		// dStart should be the highest other than last bar
    		for ( int i = dStart+1; i < lastbar; i++)
    			if (( quotes[i].low < quotes[dStart].low ) || ( quotes[i].low < quotes[lastbar].low))
    				continue next;
    		//QuoteData lastLow = Utility.getLow(quotes, dStart+1, lastbar-1);
    		//if (( lastLow.low < quotes[dStart].low ) || ( lastLow.low < quotes[lastbar].low ))
    		//	continue;
    		
    		if ( lastbar - dStart < gapMinWidth+1)
    			continue next;

    		// counting how many bars before are higher
    		int nBarHigher = 0;
    		int highestPos = dStart-1;  // highest point shows pullback
      		for ( int i = dStart-1; i >=0; i--){
    			if ( quotes[i].low >= quotes[dStart].low){
    				nBarHigher++;
    				if (quotes[i].high > quotes[highestPos].high){
    					highestPos = i;
    				}
    			}
    			else
    				break;
    		}

      		if (nBarHigher <= 2) 
      			continue next;
      		//if((  quotes[highestPos].high - quotes[dStart].low) < (diverageSize * 0.7))
      		//	continue next;


    		QuoteData pullBackHigh = Utility.getHigh(quotes, dStart+1, lastbar-1);
    		if (( diverageSize > 0 ) && (( pullBackHigh.high - quotes[dStart].low ) < diverageSize ))
    			continue next;
    		
    		DiverageDAO dv1 = new DiverageDAO(Constants.DIRECTION_DOWN, dStart, lastbar);
    		dv1.pullBackSize = pullBackHigh.high - quotes[dStart].low;
    		dv1.pullBackWidth = lastbar - dStart+1;
    		dv1.triggerPrice =  quotes[dStart].low;
    		dv1.prePos_highest = highestPos;
    		
    		return dv1;
    		// use this to return the largest dv
    		//if (( dv == null ) ||  (dv1.triggerPrice < dv.triggerPrice))
    		//	dv = dv1;
    	}
    	
    	return dv;
     }
	
	/*****************************************************
	 * 
	 * This returns the closes peak
	 * 
	 ****************************************************/
	public static DiverageDAO findLastDiverageHighClosest(QuoteData[] quotes, int start, int lastbar, int preBarsLow, /*int gapMaxWidth,*/ double diverageSize ){
		if ( start < 0 )
			start = 2;

		int dStart = lastbar - 1;
    	while( --dStart >= start ){

    		// dStart should be lower than the lastbar
    		if ( quotes[dStart].high > quotes[lastbar].high )
    			return null;// continue;
    		
    		// dStart should be the highest other than last bar
    		QuoteData lastHigh = Utility.getHigh(quotes, dStart+1, lastbar-1);
    		if ( lastHigh.high > quotes[dStart].high )
    			continue;
    		if ( lastHigh.high > quotes[lastbar].high )
    			continue;  // not the highest
    		
    		//System.out.println("high found at " + quotes[dStart].time + " " + quotes[dStart].high + " " + quotes[lastbar].time + " " + quotes[lastbar].high);
    		//if ("20241124  18:00:00".equals(quotes[dStart].time ))
    		//	System.out.println("here");
    		
    		QuoteData pullBackLow = Utility.getLow(quotes, dStart+1, lastbar);
    		DiverageDAO dv1 = new DiverageDAO(dStart, lastbar);
    		dv1.triggerPrice = quotes[dStart].high;
    		dv1.pullBackSize = quotes[dStart].high - pullBackLow.low;
    		dv1.pullBackWidth = lastbar - dStart - 1;
    		if (( dv1.pullBackSize < diverageSize )/* || ( dv1.pullBackWidth > gapMaxWidth )*/)
    			continue;

    		// n bar before dStart should be lower
    		//int preBarsLow = 2; //dv1.pullBackWidth;   // the bar below should be at least the pullback width
    		int nBarLower = 0;
      		for ( int i = dStart-1; i >=0; i--)
    			if ( quotes[i].high < quotes[dStart].high)
    				nBarLower++;
    			else
    				break;
    		if (nBarLower <= preBarsLow)   
      			continue;
    		
    		return dv1;
    	}
    	
    	return null;
     }

	
	/*****************************************************
	 * 
	 * This returns the closes peak
	 * 
	 ****************************************************/
	public static DiverageDAO findLastDiverageLowClosest(QuoteData[] quotes, int start, int lastbar, int prevBarsHigh, /*int gapMaxWidth,*/ double diverageSize ){
		if ( start < 0 )
			start = 2;
		
    	int dStart = lastbar - 1;
    	while ( --dStart >= start ){

    		// dStart should be lower than the lastbar
    		if ( quotes[dStart].low < quotes[lastbar].low )
    			return null;  //continue;
    		
    		// dStart should be the highest other than last bar
    		QuoteData lastLow = Utility.getLow(quotes, dStart+1, lastbar-1);
    		if ( lastLow.low < quotes[dStart].low )
    			continue;
    		if ( lastLow.low < quotes[lastbar].low )
    			continue;  // not the lowest
    		
    		QuoteData pullBackHigh = Utility.getHigh(quotes, dStart+1, lastbar);
    		DiverageDAO dv1 = new DiverageDAO(dStart, lastbar);
    		dv1.triggerPrice = quotes[dStart].low;
    		dv1.pullBackSize = pullBackHigh.high - quotes[dStart].low;
    		dv1.pullBackWidth = lastbar - dStart - 1;
    		if (( dv1.pullBackSize < diverageSize )/* || ( dv1.pullBackWidth > gapMaxWidth )*/)
    			continue;

    		// n bar before dStart should be lower
    		//int prevBarsHigh = 2; //dv1.pullBackWidth;   // the bar below should be at least the pullback width
    		int nBarHigher = 0;
      		for ( int i = dStart-1; i >=0; i--)
    			if ( quotes[i].low > quotes[dStart].low)
    				nBarHigher++;
    			else
    				break;

      		if (nBarHigher <= prevBarsHigh ) 
      			continue;

    		return dv1;
    	}
    	
    	return null;
     }
	
	
	
}
