import java.util.LinkedList;

/**
 * Created by christopher on 08/06/2017.
 */
public class XMLNode {
    public String id;
    public String lat;
    public String lon;
    public transient LinkedList<Tag> tags = new LinkedList<>();

    public boolean containsWater() {
        for (Tag tag: tags) {
            if (tag.containsWater()) return true;
        }
        return false;
    }
}