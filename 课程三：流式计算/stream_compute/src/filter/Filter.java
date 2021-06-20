package filter;

import java.io.*;
import java.io.FileReader;
import java.io.IOException;

public class Filter {
    public static void splitCSV() {

        String csvFile = "/Volumes/HD/Onedrive/STUDY/大三下/实训/Exercise_3/stream_compute/hotel_stay.csv";
        String line = "";
        String cvsSplitBy = ",";

         try(BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                //System.out.println(data[1]);
                int month= Integer.parseInt(data[1].substring(data[1].indexOf("/")+1,data[1].lastIndexOf("/")));
                //System.out.println(month);
                if(month>0&&month<13){
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter("/Volumes/HD/Onedrive/STUDY/大三下/实训/Exercise_3/stream_compute/splits/"+"2019-"+month+".txt",true));
                        out.write(line);
                        out.newLine();
                        out.close();
                    } catch (IOException e) {
                    }
                }
                //System.out.println(data[0]+data[1]+data[2]+data[3]+data[4]);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

