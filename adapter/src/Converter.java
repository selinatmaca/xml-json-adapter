import org.json.JSONObject;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;
public class Converter {
    public static boolean isXmlValid(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

            // Kök etiket
            Element root = doc.getDocumentElement();
            NodeList children = root.getChildNodes();

            // Altında en az bir element var mı kontrolü
            boolean hasElementChild = false;
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    hasElementChild = true;
                    System.out.println(hasElementChild);
                    break;
                }
            }

            return hasElementChild;
        } catch (Exception e) {
            return false;
        }
    }


    // XML'i JSON'a çevir
    public static JSONObject xmlToJson(Element element) {
        JSONObject result = new JSONObject();
        NodeList children = element.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node instanceof Element) {
                Element child = (Element) node;
                result.put(child.getTagName(), child.getTextContent().trim());
            }
        }
        return result;
    }
    // JSON'u XML'e çevir
    public static String jsonToXml(JSONObject json) {
        StringBuilder xmlBuilder = new StringBuilder();
        for (Object key : json.keySet()) {
            Object value = json.get((String) key);
            xmlBuilder.append("<").append(key).append(">")
                    .append(value)
                    .append("</").append(key).append(">\n");
        }
        return xmlBuilder.toString();
    }


}
