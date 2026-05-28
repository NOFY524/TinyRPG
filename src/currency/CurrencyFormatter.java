package currency;

public class CurrencyFormatter
{
    public static String format(long amount)
    {
        return String.format("%,d", amount);
    }

    public static String format_compact(long amount)
    {
        if (amount >= 1_000_000_000) {
            return String.format("%.1fB", amount / 1_000_000_000.0);
        } else if (amount >= 1_000_000) {
            return String.format("%.1fM", amount / 1_000_000.0);
        } else if (amount >= 1_000) {
            return String.format("%.1fK", amount / 1_000.0);
        } else {
            return Long.toString(amount);
        }
    }
}
