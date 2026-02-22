package tao.trading;

import java.util.ArrayList;
import java.util.List;

public class StochasticUtil {

    public static class StochasticValue {
        public Double percentK;
        public Double percentD;

        public StochasticValue(Double k, Double d) {
            this.percentK = k;
            this.percentD = d;
        }
    }

    /**
     * Calculates Slow Stochastic (%K and %D).
     *
     * @param quotes     QuoteData list in chronological order (oldest → newest)
     * @param kPeriod    Lookback period (typically 14)
     * @param kSmooth    %K smoothing period (typically 3)
     * @param dPeriod    %D period (typically 3)
     * @return List of StochasticValue (nulls where not enough data)
     */
    public static List<StochasticValue> calculateStochastic(
            List<QuoteData> quotes,
            int kPeriod,
            int kSmooth,
            int dPeriod) {

        int size = quotes.size();
        List<StochasticValue> result = new ArrayList<>();

        if (size < kPeriod) {
            return result;
        }

        // Step 1: Raw %K
        List<Double> rawK = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (i < kPeriod - 1) {
                rawK.add(null);
                continue;
            }

            double highestHigh = Double.NEGATIVE_INFINITY;
            double lowestLow = Double.POSITIVE_INFINITY;

            for (int j = i - kPeriod + 1; j <= i; j++) {
                highestHigh = Math.max(highestHigh, quotes.get(j).high);
                lowestLow = Math.min(lowestLow, quotes.get(j).low);
            }

            double close = quotes.get(i).close;
            double k = (highestHigh == lowestLow)
                    ? 0
                    : 100.0 * (close - lowestLow) / (highestHigh - lowestLow);

            rawK.add(k);
        }

        // Step 2: Smoothed %K (SMA)
        List<Double> smoothK = sma(rawK, kSmooth);

        // Step 3: %D (SMA of smoothed %K)
        List<Double> percentD = sma(smoothK, dPeriod);

        // Combine results
        for (int i = 0; i < size; i++) {
            result.add(new StochasticValue(
                    smoothK.get(i),
                    percentD.get(i)
            ));
        }

        return result;
    }

    private static List<Double> sma(List<Double> values, int period) {
        List<Double> sma = new ArrayList<>();
        double sum = 0;

        for (int i = 0; i < values.size(); i++) {
            Double v = values.get(i);

            if (v == null) {
                sma.add(null);
                continue;
            }

            sum += v;

            if (i >= period) {
                Double toRemove = values.get(i - period);
                if (toRemove != null) {
                    sum -= toRemove;
                }
            }

            if (i >= period - 1) {
                sma.add(sum / period);
            } else {
                sma.add(null);
            }
        }
        return sma;
    }
}