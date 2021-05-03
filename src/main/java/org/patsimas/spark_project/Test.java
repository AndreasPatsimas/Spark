package org.patsimas.spark_project;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Test {

    public static void main(String [] args){

        SparkSession sparkSession = SparkSession.builder()
                .appName("CSV to Dataset")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> df = sparkSession.read()
                .format("csv")
                .option("header", "true")
                .load("C:/data/ais_one_hour.csv");

        for (String column : df.columns()){
            System.out.println("aris: " + column);
        }
        df.show(5);
    }
}
