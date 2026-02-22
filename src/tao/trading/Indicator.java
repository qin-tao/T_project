package tao.trading;

import java.util.Vector;
import java.util.logging.Logger;

public class Indicator {

	Logger logger = Logger.getLogger("Indicator");

	public void calAll5EMA( Vector<QuoteData> quotes)
	{
		//1) Start by calculating k for the given timeframe. 2 / (22 + 1) = 0,0869
		double k = 2.0 / 6.0;
		
		//2) Add the closing prices for the first 22 days together and divide them by 22.
		double sum = 0;
		for ( int i = 0; i < 5; i++ )
		{
			sum += ((QuoteData)quotes.elementAt(i)).close;
		}
		double ema = sum/5;
		((QuoteData)quotes.elementAt(4)).ema5 = ema;
		
		int size = quotes.size();
		for( int i = 5; i < size; i++ )
		{
			((QuoteData)quotes.elementAt(i)).ema5 = ((QuoteData)quotes.elementAt(i)).close * k + ((QuoteData)quotes.elementAt(i-1)).ema5 * (1 - k);
			
		}
		
	}

	
	public void calAll20EMA( Vector<QuoteData> quotes)
	{
		//1) Start by calculating k for the given timeframe. 2 / (22 + 1) = 0,0869
		double k = 2.0 / 21.0;
		
		//2) Add the closing prices for the first 22 days together and divide them by 22.
		double sum = 0;
		for ( int i = 0; i < 20; i++ )
		{
			sum += ((QuoteData)quotes.elementAt(i)).close;
		}
		double ema = sum/20;
		((QuoteData)quotes.elementAt(19)).ema20 = ema;
		
		int size = quotes.size();
		for( int i = 20; i < size; i++ )
		{
			((QuoteData)quotes.elementAt(i)).ema20 = ((QuoteData)quotes.elementAt(i)).close * k + ((QuoteData)quotes.elementAt(i-1)).ema20 * (1 - k);
			
		}
		
	}
	
	
	
	public void calAll50EMA( Vector<QuoteData> quotes)
	{
		//1) Start by calculating k for the given timeframe. 2 / (22 + 1) = 0,0869
		double k = 2.0 / 51.0;
		
		//2) Add the closing prices for the first 22 days together and divide them by 22.
		double sum = 0;
		for ( int i = 0; i < 50; i++ )
		{
			sum += ((QuoteData)quotes.elementAt(i)).close;
		}
		double ema = sum/50;
		((QuoteData)quotes.elementAt(49)).ema50 = ema;
		
		int size = quotes.size();
		for( int i = 50; i < size; i++ )
		{
			((QuoteData)quotes.elementAt(i)).ema50 = ((QuoteData)quotes.elementAt(i)).close * k + ((QuoteData)quotes.elementAt(i-1)).ema50 * (1 - k);
			
		}
		
	}

	public void calAll200EMA( Vector<QuoteData> quotes)
	{
		//1) Start by calculating k for the given timeframe. 2 / (22 + 1) = 0,0869
		double k = 2.0 / 201.0;
		
		//2) Add the closing prices for the first 22 days together and divide them by 22.
		double sum = 0;
		for ( int i = 0; i < 200; i++ )
		{
			sum += ((QuoteData)quotes.elementAt(i)).close;
		}
		double ema = sum/200;
		((QuoteData)quotes.elementAt(199)).ema200 = ema;
		
		int size = quotes.size();
		for( int i = 200; i < size; i++ )
		{
			((QuoteData)quotes.elementAt(i)).ema200 = ((QuoteData)quotes.elementAt(i)).close * k + ((QuoteData)quotes.elementAt(i-1)).ema200 * (1 - k);
			
		}
		
	}

	
	

	static public double[] calculateEMA( double[] value, int days )
	{
		int size = value.length;

		//1) Start by calculating k for the given timeframe. 2 / (22 + 1) = 0,0869
		double k = 2.0 / ((double)days + 1.0);
		
		//2) Add the closing prices for the first 22 days together and divide them by 22.
		double sum = 0;
		for ( int i = 0; i < days; i++ )
		{
			sum += value[i];
		}
		double ema = sum/days;
		
		double[] emas = new double[size];
		emas[days-1] = ema;
		
		for( int i = days; i < size; i++ )
		{
			emas[i] = value[i] * k + emas[i-1]* (1-k);
		}
		
		return emas;
		
	}

	static public double[] calculateEMA( double[] value, int days, int startPos )
	{
		int size = value.length;

		//1) Start by calculating k for the given timeframe. 2 / (22 + 1) = 0,0869
		double k = 2.0 / ((double)days + 1.0);
		
		//2) Add the closing prices for the first 22 days together and divide them by 22.
		double sum = 0;
		for ( int i = startPos; i < days+ startPos; i++ )
		{
			sum += value[i];
		}
		double ema = sum/days;
		
		double[] emas = new double[size];
		emas[days + startPos -1] = ema;
		
		for( int i = days + startPos; i < size; i++ )
		{
			emas[i] = value[i] * k + emas[i-1]* (1-k);
		}
		
		return emas;
		
	}

	
	

	
	static public double[] calculateEMA( Vector<QuoteData> quotes, int days )
	{
		int size = quotes.size();

		//1) Start by calculating k for the given timeframe. 2 / (22 + 1) = 0,0869
		double k = 2.0 / ((double)days + 1.0);
		
		//2) Add the closing prices for the first 22 days together and divide them by 22.
		double sum = 0;
		for ( int i = 0; i < days; i++ )
		{
			sum += ((QuoteData)quotes.elementAt(i)).close;
		}
		double ema = sum/days;
		
		double[] emas = new double[size];
		emas[days-1] = ema;
		
		for( int i = days; i < size; i++ )
		{
			emas[i] = ((QuoteData)quotes.elementAt(i)).close * k + emas[i-1]* (1-k);
			//((QuoteData)quotes.elementAt(i)).ema50 = ((QuoteData)quotes.elementAt(i)).close * k + ((QuoteData)quotes.elementAt(i-1)).ema50 * (1 - k);
		}
		
		return emas;
		
	}
	

	static public double[] calculateEMA( Object[] quotes, int days )
	{
		int size = quotes.length;

		//1) Start by calculating k for the given timeframe. 2 / (22 + 1) = 0,0869
		double k = 2.0 / ((double)days + 1.0);
		
		//2) Add the closing prices for the first 22 days together and divide them by 22.
		double sum = 0;
		for ( int i = 0; i < days; i++ )
		{
			sum += ((QuoteData)quotes[i]).close;
		}
		double ema = sum/days;
		
		double[] emas = new double[size];
		emas[days-1] = ema;
		
		for( int i = days; i < size; i++ )
		{
			emas[i] = ((QuoteData)quotes[i]).close * k + emas[i-1]* (1-k);
			//((QuoteData)quotes.elementAt(i)).ema50 = ((QuoteData)quotes.elementAt(i)).close * k + ((QuoteData)quotes.elementAt(i-1)).ema50 * (1 - k);
		}
		
		return emas;
		
	}

	static public double[] calculateEMA_mid( QuoteData[] quotes, int days )
	{
		int size = quotes.length;

		//1) Start by calculating k for the given timeframe. 2 / (22 + 1) = 0,0869
		double k = 2.0 / ((double)days + 1.0);
		
		//2) Add the closing prices for the first 22 days together and divide them by 22.
		double sum = 0;
		for ( int i = 0; i < days; i++ )
		{
			//sum += ((QuoteData)quotes[i]).close;
			sum += (quotes[i].high + quotes[i].low)/2.0;
		}
		double ema = sum/days;
		
		double[] emas = new double[size];
		emas[days-1] = ema;
		
		for( int i = days; i < size; i++ )
		{
			emas[i] = (quotes[i].high + quotes[i].low)/2 * k + emas[i-1]* (1-k);
		}
		
		return emas;
		
	}
	
	
	
	static public double[] calculateSMA(  Object[] quotes, int days )
	{
		int size = quotes.length;

		double[] smas = new double[size];
		
		for( int i = days; i < size; i++ )
		{
			double sum = 0.0;
			for ( int j = i; j > i-days; j--)
			{
				sum += ((QuoteData)quotes[j]).close;
			}
			
			smas[i] = sum/(double)days;
		}
		
		return smas;
		
	}
	

	static public double[] calculateSMA(  double[] values, int days )
	{
		int size = values.length;

		double[] smas = new double[size];
		
		for( int i = days; i < size; i++ )
		{
			double sum = 0.0;
			for ( int j = i; j > i-days; j--)
			{
				sum += values[j];
			}
			
			smas[i] = sum/(double)days;
		}
		
		return smas;
		
	}

	
	
	/*
	public static double[] calculateStochastics( Vector<QuoteData> quotes, int period )
	{
		int size = quotes.size();
		double[] k = new double[size];
		
		//%K = (Today's closing price - lowest low during the last [period] days) ÷ 
		// (highest high during the last [period] days - lowest low during the last [period] days)
		
		for( int i = period; i < size; i++ )
		{
			double high = 0.0;
			double low = 999;
			for ( int j = i; j >= i-period; j--)
			{
				if (((QuoteData)quotes.elementAt(j)).high > high )
					high = ((QuoteData)quotes.elementAt(j)).high;
				if (((QuoteData)quotes.elementAt(j)).low < low )
					low = ((QuoteData)quotes.elementAt(j)).low;
			}
			k[i] = 100*(((QuoteData)quotes.elementAt(i)).close - low )/(high-low); 
		}
		
		return k;
		
	}*/
	
	
	public static double[] calculateStochastics( Vector<QuoteData> quotes, int period )
	{
		return calculateStochastics(quotes.toArray(), period);


	}

	public static double[] calculateStochastics( Object[] quotes, int period )
	{
		int size = quotes.length;
		double[] k = new double[size];
		
		//%K = (Today's closing price - lowest low during the last [period] days) ÷ 
		// (highest high during the last [period] days - lowest low during the last [period] days)
		
		for( int i = period; i < size; i++ ){
			double high = 0.0;
			double low = 999;
			for ( int j = i; j > i-period; j--)
			{
				if (((QuoteData)quotes[j]).high > high )
					high = ((QuoteData)quotes[j]).high;
				if (((QuoteData)quotes[j]).low < low )
					low = ((QuoteData)quotes[j]).low;
			}
			k[i] = 100*(((QuoteData)quotes[i]).close - low )/(high-low); 
		}

		return calculateEMA( k, 3, 3 );
		
	}
	
	
	static public MACD[] calculateMACD( QuoteData[] quotes )
	{
		/*
		MACD: (12-day EMA - 26-day EMA) 
		Signal Line: 9-day EMA of MACD
		MACD Histogram: MACD - Signal Line
		*/
		
		int size = quotes.length;
		
		double[] close = new double[size];
		for ( int i = 0; i < size; i++ )
			close[i] = ((QuoteData)quotes[i]).close;
		
		double[] macd = new double[size];
		double[] ema_12 = calculateEMA( close, 12 );
		double[] ema_26 = calculateEMA( close, 26 );
		for ( int i = 0; i < size; i++ )
			macd[i] = ema_12[i] - ema_26[i];
		
		double[] signal_line = calculateEMA( macd, 9 );	
		
		MACD[] macd_ind = new MACD[size];
		for ( int i = 0; i < size; i++)
		{
			macd_ind[i] = new MACD();
			macd_ind[i].macd = macd[i];
			macd_ind[i].signalLine = signal_line[i];
			macd_ind[i].histogram = macd[i] - signal_line[i];
		}
		
		return macd_ind;
		
	}

	
}
