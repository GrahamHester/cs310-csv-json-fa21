package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full; //List of string arrays
            full = reader.readAll(); //Read CSV into List of String Arrays
            
            Iterator<String[]> iterator;
            iterator = full.iterator(); //Read one line at a time
            
            //--------INSERT YOUR CODE HERE------------
            

            //JSON Obj will become final format
            JSONObject jsonObj = new JSONObject();
            
            
            JSONArray columnHead = new JSONArray(); //Column Headers 
            JSONArray rowHead = new JSONArray(); //Row Headers (Student ID)
            JSONArray data = new JSONArray(); //Data in block (Grades)
            
            
            JSONArray temp;
            String[] record = iterator.next(); //Get first row
            
            for(int i = 0; i < record.length; i++) { //For each column Header in the current row
                columnHead.add(record[i]); //Add it to the column headers array
            }
            
            while(iterator.hasNext()) { //While there is data being read
                temp = new JSONArray(); //temp info
                record = iterator.next(); //Get next row (line) in file
                rowHead.add(record[0]); //Add the first item in the row to the row header - Student ID
                for(int i = 1; i < record.length; i++) { //For each item past the row header - Student Grades
                    int stringHolder = Integer.parseInt(record[i]); //Convert to integer representation
                    temp.add(stringHolder); //add converted string to temp variable
                }
                data.add(temp); //Add the converted data to the data array
            }
            
            //Put all Arrays into jsonObj
            jsonObj.put("rowHeaders", rowHead);
            jsonObj.put("colHeaders", columnHead);
            jsonObj.put("data", data);
            
            //Put results into result String
            results = JSONValue.toJSONString(jsonObj);
            }        
        
        catch(Exception e) { e.printStackTrace(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");
            
            //-----------INSERT YOUR CODE HERE-------------
            
            JSONParser parse = new JSONParser(); //Parse obj
            JSONObject jsonObj = (JSONObject)parse.parse(jsonString); //Parse JSON structured data
            
            JSONArray rowHead = (JSONArray)jsonObj.get("rowHeaders"); //Put row headers into JSON Array
            JSONArray columnHead = (JSONArray)jsonObj.get("colHeaders");// "" column ""
            JSONArray data = (JSONArray)jsonObj.get("data"); //"" data into JSON Array
            
            JSONArray temp; //temp container array
            String[] record = new String[columnHead.size()]; //Row with same length as # of columns
            
            
            //----This block sets the first row of the CSV file which is only Column Headers----
            for(int i = 0; i < columnHead.size(); i ++) { //For the # of column headers
                record[i] = (String) columnHead.get(i); //Put each into the same index point in the first row of the file
            }
            csvWriter.writeNext(record); //record that row in the record
            
            
            //-----This block formats the rest of the data into rows----
            for(int i = 0; i < data.size(); i++) { //for # of items in data array
                
                temp = (JSONArray) data.get(i); //store the set of grades into temp and type cast into JSONArray
                record = new String[temp.size() + 1]; //create row with size one larger than data length to include ID -->  ID, "123", "234", "345"
                record[0] = (String) rowHead.get(i); //put the student ID in the first column
                
                for(int j = 0; j < record.length - 1; j++) { //for every grade in the row (every column except ID)
                    record[j + 1] = Long.toString((Long)temp.get(j));//Convert grades to strings
                }
                csvWriter.writeNext(record); //Add the row to the record
                
            }
            results = writer.toString(); 
        }
        
        catch(Exception e) { e.printStackTrace(); }
        
        return results.trim();
        
    }

}