import com.google.gson.Gson;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by christopher on 25/05/2017.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Use: java -jar parser <input> <output>");
            System.exit(1);
        }
        File file = new File(args[0]);
        PrintWriter writer = new PrintWriter(new FileWriter(args[1]));
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        ParseHandler parseHandler = new ParseHandler(writer);
        long start = System.currentTimeMillis();
        System.out.println("Parsing Started");
        writer.print("{\"nodes\":[");
        parser.parse(file, parseHandler);
        writer.println("]}");
        writer.close();
        long end = System.currentTimeMillis();
        System.out.println("Parsing Finished!");
        System.out.println("Found " + parseHandler.getNodeCount() + " nodes!");
        System.out.println("Ellapsed Seconds: " + ((end - start) / 1000));
        System.out.println("Writing tags to file");
        PrintWriter tagWriter = new PrintWriter(new FileWriter("tags." + args[1]));
        Gson gson = new Gson();
        List<Tag> tags = new ArrayList<>(parseHandler.distinctTags());
        tags.sort(Comparator.comparing(tag -> tag.k));
        for (Tag tag : tags) {
            tagWriter.println(gson.toJson(tag));
        }
        tagWriter.close();
        System.out.println("Writing tags finished!");
    }
}