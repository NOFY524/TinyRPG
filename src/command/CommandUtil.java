package command;

import java.util.List;
import java.util.Set;

public class CommandUtil
{
    public static List<String> partialMatch(String input, Set<String> candidates)
    {
        return candidates.stream()
                .filter(s -> s.startsWith(input))
                .toList();
    }

    public static List<String> partialMatch(String input, List<String> candidates)
    {
        return candidates.stream()
                .filter(s -> s.startsWith(input))
                .toList();
    }
}
