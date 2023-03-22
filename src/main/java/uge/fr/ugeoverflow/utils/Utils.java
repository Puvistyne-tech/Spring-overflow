package uge.fr.ugeoverflow.utils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import uge.fr.ugeoverflow.model.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {


    public static List<Tag> createTags() {
        String filename = "src/main/resources/tags";
        List<Tag> tags = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(":");
                Tag.TAG_TYPE tagType = Tag.TAG_TYPE.valueOf(parts[0]);
                String description = parts[1];
                Tag tag = new Tag(tagType, description);
                tags.add(tag);
            }
        } catch (Exception e) {
            return null;
        }
        return tags;
    }

    public static String toHtml(String markdown) {
        Node document = Parser.builder().build().parse(markdown);
        var res = HtmlRenderer.builder().build().render(document);
        System.out.println(res);
        return res;

    }
}
