import com.google.gson.Gson;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by christopher on 08/06/2017.
 */
public class ParseHandler extends DefaultHandler {

    private long nodeCount;
    private long flushes;
    private List<Tag> tags;
    private XMLNodes nodes;
    private XMLNode currentNode;
    private PrintWriter writer;

    public ParseHandler(PrintWriter writer) throws Exception {
        this.tags = new LinkedList<>();
        this.nodes = new XMLNodes();
        this.writer = writer;
        this.flushes = this.nodeCount = 0;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (!qName.equals("node") && !qName.equals("tag")) return;
        if (qName.equals("node")) { // Opening node
            currentNode = new XMLNode();
            currentNode.id = attributes.getValue("id");
            currentNode.lat = attributes.getValue("lat");
            currentNode.lon = attributes.getValue("lon");
        } else {                    // Opening tag
            Tag tag = new Tag();
            tag.k = attributes.getValue("k");
            tag.v = attributes.getValue("v");
            currentNode.tags.add(tag);
            this.tags.add(tag);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if(!qName.equals("node")) return;
        if (currentNode.containsWater()) return;
        nodes.nodes.add(currentNode);
        if (nodes.nodes.size() > 50000) this.flushToFile();
        nodeCount++;
    }

    @Override
    public void endDocument() {
        this.flushRemaining();
    }

    public Set<Tag> distinctTags() {
        Set<Tag> distinct = new TreeSet<>(Comparator.comparing(tag -> tag.k));
        distinct.addAll(this.tags);
        return distinct;
    }

    public long getNodeCount() {
        return this.nodeCount;
    }

    private void flushRemaining() {
        Gson gson = new Gson();
        int i = 0;
        for (XMLNode node: this.nodes.nodes) {
            writer.print(gson.toJson(node));
            if (i++ < nodes.nodes.size() - 1) writer.print(",");
        }
        writer.flush();
    }

    private void flushToFile() {
        System.out.println("Partial File Flush: " + ++flushes);
        Gson gson = new Gson();
        for (XMLNode node: this.nodes.nodes) {
            writer.print(gson.toJson(node));
            writer.print(",");
        }
        writer.flush();
        this.nodes.nodes = new LinkedList<>();
        System.gc();
    }
}
