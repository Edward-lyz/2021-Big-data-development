package main;
import s3_writer.Upload;
import consumer.Consumer;
import producer.Producer;
import filter.Filter;

public class Main {
    public static void main(String args[]){
    //Producer.main(args);
    Filter.splitCSV();
    for(int month=1;month<13;month++){
        Upload.main("2019-"+month+".txt");
    }
    }
}
