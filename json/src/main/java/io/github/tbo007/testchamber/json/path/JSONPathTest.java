package io.github.tbo007.testchamber.json.path;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class JSONPathTest {


    public static void main(String[] args) {
        // firstTry();
        newDoc();
    }

    private static void newDoc() {
        // minimal empty JSON
        DocumentContext jsonContext = JsonPath.parse("{}");
        AtomicInteger currId = new AtomicInteger(4711);
        Map<String,Object> tierpark = new TreeMap<>();
        tierpark.put("currID", currId.get());
        List<Map<String,?>> tiere = new ArrayList<>();
        tiere.add(Map.of("art", "Affe", "id", 4700, "name", "Charlie"));
        tiere.add(Map.of("art", "Schwein", "id", 4701, "name", "RudiRuessel"));
        tierpark.put("tiere", tiere);
        jsonContext.put("$", "tierpark", tierpark);
        List<?> readBackArray = jsonContext.read("$.tierpark.tiere", List.class);
        List<?> tiereNamedCharlie = jsonContext.read("$.tierpark.tiere[?(@.name=='Charlie')]");

        System.out.println(JsonFormatter.prettyPrint(jsonContext.jsonString()));
        System.out.println(readBackArray);
        System.out.println(tiereNamedCharlie.getFirst());
    }

    private static void firstTry() {
        String jsonString = "{\"name\":\"John\",\"alter\":30}";
        DocumentContext jsonContext = JsonPath.parse(jsonString);
        jsonContext.set("$.name", "Jane");
        // jsonContext.set("$.beruf", "Entwickler");
        String changed = jsonContext.jsonString();

        System.out.println(JsonFormatter.prettyPrint(changed));
    }
}
