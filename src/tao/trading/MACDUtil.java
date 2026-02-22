package tao.trading;

import java.util.ArrayList;
import java.util.List;

public class MACDUtil {

    public static class MACDValue {
        public Double macd;
        public Double signal;
        public Double histogram;

        public MACDValue(Double macd, Double signal, Double histogram) {
            this.macd = macd;
            this.signal = signal;
            this.histogram = histogram;
        }
    }

    /**
     * Calculates MACD using closing prices.
     *
     * @param quotes List of QuoteData (chronological: oldest → newest)
     * @param fastPeriod   EMA fast period (typically 12)
     * @param slowPeriod   EMA slow period (typically 26)
     * @param signalPeriod Signal EMA period (typically 9)
     */
    public static List<MACDValue> calculateMACD(
            List<QuoteData> quotes,
            int fastPeriod,
            int slowPeriod,
            int signalPeriod) {

        int size = quotes.size();
        List<MACDValue> result = new ArrayList<>();

        if (size < slowPeriod) {
            return result;
        }

        // Step 1: EMA fast and slow
        List<Double> emaFast = calculateEMA(quotes, fastPeriod);
        List<Double> emaSlow = calculateEMA(quotes, slowPeriod);

        // Step 2: MACD line
        List<Double> macdLine = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (emaFast.get(i) == null || emaSlow.get(i) == null) {
                macdLine.add(null);
            } else {
                macdLine.add(emaFast.get(i) - emaSlow.get(i));
            }
        }

        // Step 3: Signal line (EMA of MACD)
        List<Double> signalLine = calculateEMA(macdLine, signalPeriod);

        // Step 4: Histogram
        for (int i = 0; i < size; i++) {
            Double macd = macdLine.get(i);
            Double signal = signalLine.get(i);

            if (macd == null || signal == null) {
                result.add(new MACDValue(null, null, null));
            } else {
                result.add(new MACDValue(
                        macd,
                        signal,
                        macd - signal
                ));
            }
        }

        return result;
    }

    // ---------- EMA helpers ----------

    private static List<Double> calculateEMA(List<QuoteData> quotes, int period) {
        List<Double> closes = new ArrayList<>();
        for (QuoteData q : quotes) {
            closes.add(q.close);
        }
        return calculateEMA(closes, period);
    }

    private static List<Double> calculateEMA(List<Double> values, int period) {
        List<Double> ema = new ArrayList<>();
        double multiplier = 2.0 / (period + 1);
        double sum = 0;

        for (int i = 0; i < values.size(); i++) {
            double price = values.get(i);

            if (i < period) {
                sum += price;
                ema.add(null);
            } else if (i == period) {
                double sma = sum / period;
                ema.add(sma);
            } else {
                double prevEma = ema.get(i - 1);
                double currentEma = (price - prevEma) * multiplier + prevEma;
                ema.add(currentEma);
            }
        }
        return ema;
    }
}