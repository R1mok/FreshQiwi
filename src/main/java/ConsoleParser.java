import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConsoleParser {

    private String code;
    private String date;

    ConsoleParser(String code, String date) {
       this.code = code;
       this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Префик ссылки
    public final static String LINK = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=";

    public static void main(String[] args) throws Exception {
        // значения берутся из параметров напрямую
        String code = args[0];
        String date = args[1];
        ConsoleParser consoleParser = new ConsoleParser(code, date);
        HttpURLConnection con = consoleParser.openConnection(consoleParser);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        consoleParser.parseDoc(consoleParser.readXml(in));
        in.close();
    }

    private void parseDoc(StringBuilder response) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(response.toString())));
        List<ValCurs> list = new ArrayList<>();
        NodeList nodeList = doc.getElementsByTagName("Valute");
        for (int i = 0; i < nodeList.getLength(); i++) {
            list.add(getCurs(nodeList.item(i)));
        }
        findAndPrintNeededCurs(code, list);
    }

    private void findAndPrintNeededCurs(String neededCurs, List<ValCurs> cursList) {
        // нет обработки через ifPresent()
        ValCurs neededValCurs = cursList.stream().filter(curCurs -> neededCurs.equals(curCurs.getCharCode()))
                .findAny().get();
        System.out.print(
                neededValCurs.getCharCode() + " (" + neededValCurs.getName() + "): " + neededValCurs.getValue()
        );
    }

    private StringBuilder readXml(BufferedReader in) throws IOException {
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        return response;
    }

    private HttpURLConnection openConnection(ConsoleParser consoleParser) throws IOException {
        URL obj = new URL(LINK + consoleParser.getDate());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        return con;
    }

    private static ValCurs getCurs(Node node) {
        ValCurs curs = new ValCurs();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            curs.setCharCode(getTagValue("CharCode", element));
            curs.setValue(getTagValue("Value", element));
            curs.setName(getTagValue("Name", element));
        }
        return curs;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
}
