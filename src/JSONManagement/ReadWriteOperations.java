package JSONManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ReadWriteOperations {
    public static void writeFileWithObject(String fileName, JSONObject jsonObject, boolean writeAtEnd){
        try {
            FileWriter fileWriter = new FileWriter(fileName, writeAtEnd);
            fileWriter.write(jsonObject.toString(4));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFileWithArray(String fileName, JSONArray jsonArray, boolean writeAtEnd){
        try {
            FileWriter fileWriter = new FileWriter(fileName, writeAtEnd);
            fileWriter.write(jsonArray.toString(4));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONTokener readFile(String fileName){
        JSONTokener tokener = null;

        try {
            tokener = new JSONTokener(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tokener;
    }
}
