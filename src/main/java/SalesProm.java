import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SalesProm {

    static String nameTemp;
    static HashMap<String, Double> subtotales = new HashMap<String, Double>();
    static HashMap <String, Double> contProm = new HashMap<String, Double>();
    static double saleTemp;
    static String deptTemp;


    public static void main(String[] args) {

        FileReader fileReader = null;
        try {
            fileReader = new FileReader("sales_array.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JsonReader reader = new JsonReader(fileReader);
        // we call the handle object method to handle the full json object. This
        // implies that the first token in JsonToken.BEGIN_OBJECT, which is
        // always true.
        try {
            //handleObject(reader);
            handleArray(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Map.Entry<String, Double>> entries1 = contProm.entrySet();
        for (Map.Entry<String, Double> entry: subtotales.entrySet()){
            String key = entry.getKey();
            if (contProm.containsKey(key)){
                double v1 = entry.getValue();
                double v2 = contProm.get(key);
                System.out.println("Departamento: " +key+"\nSales promedio: " + (v1/v2)+"\n");
            }
        }

    }

    private static void handleObject(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.BEGIN_ARRAY))
                handleArray(reader);
            else if (token.equals(JsonToken.END_OBJECT)) {
                reader.endObject();
                return;
            } else
                handleNonArrayToken(reader, token);
        }

    }

    public static void handleArray(JsonReader reader) throws IOException {
        reader.beginArray();
        while (true) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.END_ARRAY)) {
                reader.endArray();
                break;
            } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
                handleObject(reader);
            } else if (token.equals(JsonToken.END_OBJECT)) {
                reader.endObject();
            } else
                handleNonArrayToken(reader, token);
        }
    }

    public static void handleNonArrayToken(JsonReader reader, JsonToken token) throws IOException
    {
        double val = 0.0;
        double cont = 0;
        if ( token.equals(JsonToken.NAME) ) {
            nameTemp = reader.nextName();
            System.out.println(nameTemp);
            //System.out.println(reader.nextName());
        }else if (token.equals(JsonToken.STRING)) {
            deptTemp = reader.nextString();
            if (nameTemp.equalsIgnoreCase("department")){
                if (subtotales.containsKey(deptTemp)){
                    double sum = subtotales.get(deptTemp);
                    subtotales.put(deptTemp, sum + saleTemp);
                }else {
                    subtotales.put(deptTemp, saleTemp);
                }
                if (contProm.containsKey(deptTemp)){
                    double contador = contProm.get(deptTemp);
                    contProm.put(deptTemp, contador+1);
                }else {
                    contProm.put(deptTemp, cont+1);
                }
            }
            System.out.println(deptTemp);
        }else if (token.equals(JsonToken.NUMBER)) {
            saleTemp = reader.nextDouble();
            System.out.println(saleTemp);
            //System.out.println(reader.nextDouble());
        }else
            reader.skipValue();
    }
}