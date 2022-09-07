package kutschke.generation.LSystems;

import java.util.HashMap;
import java.util.Map;

public class LExpander {

    private Map<Character, String> rules = new HashMap<Character, String>();

    public LExpander(Map<Character, String> rules) {
        this.rules = rules;
    }

    public String expand(String s) {
        StringBuilder bldr = new StringBuilder();
        for (Character c : s.toCharArray()) {
            if (rules.containsKey(c)) {
                bldr.append(rules.get(c));
            } else {
                bldr.append(c);
            }
        }
        return bldr.toString();
    }

}
