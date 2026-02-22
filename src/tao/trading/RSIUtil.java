package tao.trading;

import java.util.ArrayList;
import java.util.List;

public class RSIUtil {

    /**
     * Calculates RSI values for the given quotes.
     *
     * @param quotes List of QuoteData in chronological order
     * @param period RSI period (usually 14)
     * @return List of RSI values (null for indexes where RSI cannot be calculated)
     */
    public static List<Double> calculateRSI(List<QuoteData> quotes, int period) {
        List<Double> rsiValues = new ArrayList<>();

        if (quotes == null || quotes.size() <= period) {
            return rsiValues;
        }

        double gainSum = 0.0;
        double lossSum = 0.0;

        // First RSI: simple average gain/loss
        for (int i = 1; i <= period; i++) {
            double change = quotes.get(i).close - quotes.get(i - 1).close;
            if (change > 0) {
                gainSum += change;
            } else {
                lossSum += -change;
            }
        }

        double avgGain = gainSum / period;
        double avgLoss = lossSum / period;

        // Fill initial values with null
        for (int i = 0; i < period; i++) {
            rsiValues.add(null);
        }

        double rs = avgLoss == 0 ? Double.POSITIVE_INFINITY : avgGain / avgLoss;
        double rsi = 100 - (100 / (1 + rs));
        rsiValues.add(rsi);

        // Wilder smoothing
        for (int i = period + 1; i < quotes.size(); i++) {
            double change = quotes.get(i).close - quotes.get(i - 1).close;
            double gain = change > 0 ? change : 0;
            double loss = change < 0 ? -change : 0;

            avgGain = ((avgGain * (period - 1)) + gain) / period;
            avgLoss = ((avgLoss * (period - 1)) + loss) / period;

            rs = avgLoss == 0 ? Double.POSITIVE_INFINITY : avgGain / avgLoss;
            rsi = 100 - (100 / (1 + rs));

            rsiValues.add(rsi);
        }

        return rsiValues;
    }
}