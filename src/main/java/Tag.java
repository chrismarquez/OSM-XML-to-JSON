/**
 * Created by christopher on 08/06/2017.
 */
public class Tag {
    public String k;
    public String v;

    public boolean containsWater() {
        return this.k.toLowerCase().contains("water") && this.v.toLowerCase().contains("water");
    }
}