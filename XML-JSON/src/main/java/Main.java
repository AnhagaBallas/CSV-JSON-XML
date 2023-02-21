import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("staff");
            document.appendChild(root);

            root.appendChild(getEmploee(document, "1", "John", "Smith", "USA", "25"));
            root.appendChild(getEmploee(document, "2", "Inav", "Petreov", "ru", "23"));


            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("staff.xml"));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(domSource, streamResult);


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        List list = parseXML("staff.xml");
        String json = listToJson(list);
        writeString(json);

    }

    public static Node getEmploee(Document doc, String id, String firstName, String lastName, String country, String age) {
        Element employee = doc.createElement("Employee");
        employee.appendChild(getEmployeeElements(doc, "id", id));
        employee.appendChild(getEmployeeElements(doc, "firstName", firstName));
        employee.appendChild(getEmployeeElements(doc, "lastName", lastName));
        employee.appendChild(getEmployeeElements(doc, "country", country));
        employee.appendChild(getEmployeeElements(doc, "age", age));
        return employee;

    }

    public static Node getEmployeeElements(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    public static List parseXML(String file) {
        List<Employee> employeeList = new ArrayList<>();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(file);
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Employee employee1 = new Employee();
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element employee = (Element) node;

                    employee1.id = (Long.parseLong(employee.getElementsByTagName("id").item(0).getTextContent()));
                    employee1.firstName = (employee.getElementsByTagName("firstName").item(0).getTextContent());
                    employee1.lastName = (employee.getElementsByTagName("lastName").item(0).getTextContent());
                    employee1.country = (employee.getElementsByTagName("country").item(0).getTextContent());
                    employee1.age = (Integer.parseInt(employee.getElementsByTagName("age").item(0).getTextContent()));

                    employeeList.add(employee1);


                }
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return employeeList;
    }

    public static String listToJson(List list) {
        String json;
        Type listType = new TypeToken<List>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json) {
        try (FileWriter fileWriter = new FileWriter("data.json")) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
