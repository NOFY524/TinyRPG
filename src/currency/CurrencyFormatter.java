package currency;

public class CurrencyFormatter
{
    public static String format(long value)
    {
        return String.format("%,d", value);
    }

    public static String format_compact(long value)
    {
        if (value >= 1_000_000_000) {
            return String.format("%.1fB", value / 1_000_000_000.0);
        } else if (value >= 1_000_000) {
            return String.format("%.1fM", value / 1_000_000.0);
        } else if (value >= 1_000) {
            return String.format("%.1fK", value / 1_000.0);
        } else {
            return Long.toString(value);
        }
    }
}
