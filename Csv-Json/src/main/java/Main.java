import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String[] employee1 = "1,John,Smith,USA,25".split(",");
        String[] employee2 = "2,Inav,Petrov,RU,23".split(",");
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter("data.csv"))) {
            csvWriter.writeNext(employee1);
            csvWriter.writeNext(employee2);
            csvWriter.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        List<Employee> list = parseCSV(columnMapping, "data.csv");
        String json = listToJson(list);
        writeString(json);

    }

    public static List parseCSV(String[] list, String file) {
        List list1 = new ArrayList();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(list);
            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            list1 = csvToBean.parse();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return list1;

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
