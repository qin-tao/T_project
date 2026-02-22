package tao.trading;

import java.util.ArrayList;
import java.util.List;

public class ADXUtil {

    public static class ADXValue {
        public Double plusDI;
        public Double minusDI;
        public Double adx;

        public ADXValue(Double plusDI, Double minusDI, Double adx) {
            this.plusDI = plusDI;
            this.minusDI = minusDI;
            this.adx = adx;
        }
    }

    /**
     * Calculates ADX, +DI, -DI
     *
     * @param quotes List of QuoteData (chronological: oldest → newest)
     * @param period ADX period (typically 14)
     */
    public static List<ADXValue> calculateADX(List<QuoteData> quotes, int period) {
        int size = quotes.size();
        List<ADXValue> result = new ArrayList<>();

        if (size <= period) {
            return result;
        }

        double[] tr = new double[size];
        double[] plusDM = new double[size];
        double[] minusDM = new double[size];

        // Step 1: TR, +DM, -DM
        for (int i = 1; i < size; i++) {
            QuoteData curr = quotes.get(i);
            QuoteData prev = quotes.get(i - 1);

            double highDiff = curr.high - prev.high;
            double lowDiff = prev.low - curr.low;

            tr[i] = Math.max(
                    curr.high - curr.low,
                    Math.max(
                            Math.abs(curr.high - prev.close),
                            Math.abs(curr.low - prev.close)
                    )
            );

            plusDM[i] = (highDiff > lowDiff && highDiff > 0) ? highDiff : 0;
            minusDM[i] = (lowDiff > highDiff && lowDiff > 0) ? lowDiff : 0;
        }

        // Step 2: Initial Wilder sums
        double trSum = 0, plusDMSum = 0, minusDMSum = 0;
        for (int i = 1; i <= period; i++) {
            trSum += tr[i];
            plusDMSum += plusDM[i];
            minusDMSum += minusDM[i];
        }

        double smoothedTR = trSum;
        double smoothedPlusDM = plusDMSum;
        double smoothedMinusDM = minusDMSum;

        List<Double> dxValues = new ArrayList<>();

        // Pad initial values
        for (int i = 0; i <= period; i++) {
            result.add(new ADXValue(null, null, null));
        }

        // Step 3: First +DI, -DI, DX
        double plusDI = 100 * (smoothedPlusDM / smoothedTR);
        double minusDI = 100 * (smoothedMinusDM / smoothedTR);
        double dx = 100 * Math.abs(plusDI - minusDI) / (plusDI + minusDI);
        dxValues.add(dx);

        result.add(new ADXValue(plusDI, minusDI, null));

        // Step 4: Wilder smoothing loop
        for (int i = period + 1; i < size; i++) {
            smoothedTR = smoothedTR - (smoothedTR / period) + tr[i];
            smoothedPlusDM = smoothedPlusDM - (smoothedPlusDM / period) + plusDM[i];
            smoothedMinusDM = smoothedMinusDM - (smoothedMinusDM / period) + minusDM[i];

            plusDI = 100 * (smoothedPlusDM / smoothedTR);
            minusDI = 100 * (smoothedMinusDM / smoothedTR);

            dx = 100 * Math.abs(plusDI - minusDI) / (plusDI + minusDI);
            dxValues.add(dx);

            result.add(new ADXValue(plusDI, minusDI, null));
        }

        // Step 5: ADX (EMA of DX with Wilder smoothing)
        double adxSum = 0;
        for (int i = 0; i < period; i++) {
            adxSum += dxValues.get(i);
        }

        double adx = adxSum / period;

        int adxIndex = 2 * period;
        if (adxIndex < result.size()) {
            result.set(adxIndex,
                    new ADXValue(
                            result.get(adxIndex).plusDI,
                            result.get(adxIndex).minusDI,
                            adx
                    ));
        }

        for (int i = adxIndex + 1; i < result.size(); i++) {
            adx = ((adx * (period - 1)) + dxValues.get(i - period)) / period;

            ADXValue prev = result.get(i);
            result.set(i, new ADXValue(prev.plusDI, prev.minusDI, adx));
        }

        return result;
    }
}

/*List<QuoteData> quotes = ...;

List<ADXUtil.ADXValue> adx =
        ADXUtil.calculateADX(quotes, 14);*/