import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String a = readString("data.json");
        List<Employee> b = jsonToList(a);


        for (int i = 0; i < b.size(); i++) {
            System.out.println(b.get(i).toString());
        }

    }

    public static String readString(String path) {
        StringBuilder string = new StringBuilder();
        try (FileReader fileReader = new FileReader(path)) {
            int i;
            while ((i = fileReader.read()) != -1) {
                string.append((char) i);

            }


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return string.toString();
    }

    public static List<Employee> jsonToList(String jason) {
        JSONParser parser = new JSONParser();
        List<Employee> result = new ArrayList<>();
        try {
            Object obj = parser.parse(jason);
            JSONArray array = (JSONArray) obj;
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            for (int i = 0; i < array.size(); i++) {
                Employee employee = gson.fromJson(array.get(i).toString(), Employee.class);
                result.add(employee);

            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }
        return result;
    }
}
