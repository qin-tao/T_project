package tao.trading.trend.analysis;

import java.util.Iterator;
import java.util.Vector;

import tao.trading.Constants;
import tao.trading.Indicator;
import tao.trading.MATouch;
import tao.trading.Pattern;
import tao.trading.Push;
import tao.trading.QuoteData;
import tao.trading.dao.PushList;
import tao.trading.strategy.util.Utility;

public class BigTrendAnalysis{
	public int direction;

	public String feature;
	public int start;
	public boolean isTrendy;
	public int lastTrendPushIndex;
	public Vector<Push> pushes = new Vector<Push>();
	public MATouch[] maTouches;
	public QuoteData resistance, support;
	
	
	public String toString( QuoteData[] quotes )
	{
		StringBuffer strBuf = new StringBuffer();
		
		strBuf.append("DIRECTION: " + direction + "\n");
		
		if ( resistance != null )
			strBuf.append("RESISTANCE: " + resistance.high + " " + resistance.time + "\n");
		if ( support != null )
			strBuf.append("SUPPORT: " + support.low + " " + support.time + "\n");
		
		
		Iterator it = pushes.iterator();
		while (it.hasNext())
		{
			Push p = (Push)it.next();
			strBuf.append(((p.direction == Constants.DIRECTION_UP)?"UP  ":"DOWN") + "  start:" + quotes[p.pushStart].time + " end:" + quotes[p.pushEnd].time + " duration:" + p.duration + "\n");
		}
		
		
		//strBuf.append("Num of Touches:" + maTouches.length + "\n");
		
		if (( maTouches != null ) && ( maTouches.length > 0))
		{
			for ( int i = 0; i < maTouches.length; i++)
			{
				strBuf.append("Touch" + (i+1) + ":  " + quotes[maTouches[i].touchBegin].time + "-" + quotes[maTouches[i].touchEnd].time + "\n" );
			}
		}
		
		
		
		return strBuf.toString();
	}
	

	
	public int getLastPushIndex( int direction )
	{
		int lastPush = pushes.size() - 1;
		
		for ( int i = lastPush; i>=0; i--)
		{
			if ((pushes.elementAt(i)).direction == direction)
			{
				return i;
			}
		}
		
		return -1;
		
	}
	

	public Push getLastPush( int direction )
	{
		int lastPush = pushes.size() - 1;
		
		for ( int i = lastPush; i>=0; i--)
		{
			if ((pushes.elementAt(i)).direction == direction)
			{
				return pushes.elementAt(i);
			}
		}
		
		return null;
		
	}

	
	public Push getLastPush()
	{
		return pushes.lastElement();

	}
	
	

	BigTrend calculateTrend( QuoteData[] quotes ){

		double[] ema20 = Indicator.calculateEMA(quotes, 20);

		int barsAbove = 0, barsBelow = 0;
		for ( int i = 0; i < quotes.length; i++)
		{
			if ( quotes[i].low > ema20[i] )
				barsAbove++;
			else if ( quotes[i].high < ema20[i] )
				barsBelow++;
		}

		BigTrend bigTrend = new BigTrend();
		bigTrend.direction = Constants.DIRT_UNKNOWN;
		bigTrend.dir = Constants.DIRECTION_UNKNOWN;

		if (( barsAbove >= 2 ) && ( barsBelow == 0 ))
		{
			bigTrend.direction = Constants.DIRT_UP;
			bigTrend.dir = Constants.DIRECTION_UP;
			bigTrend.state = BigTrend.STATE_UP;
		}
		else if (( barsBelow >= 2 ) && ( barsAbove == 0 ))
		{
			bigTrend.direction = Constants.DIRT_DOWN;
			bigTrend.dir = Constants.DIRECTION_DOWN;
			bigTrend.state = BigTrend.STATE_DOWN;
		}

		return bigTrend;
	}


	
	

	
	
	

}
