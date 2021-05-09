package org.patsimas.spark_project.utils;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;

import static org.apache.spark.sql.functions.*;

public class SparkUtils {

    private static final String X_MIN = "xMin";
    private static final String X_MAX = "xMax";
    private static final String Y_MIN = "yMin";
    private static final String Y_MAX = "yMax";

    public static Dataset<Row> getDf(SparkSession sparkSession, String path, String literal){

        Dataset<Row> df = sparkSession.read()
                .format("csv")
                .option("header", "true")
                .load(path);

        df = df.drop("_c0");
        df = df.withColumn("X", col("X").cast("double"));
        df = df.withColumn("Y", col("Y").cast("double"));
        df = df.withColumn("dfId", lit(literal));
        return df.cache();
    }

    public static Dataset<Row> getUnionFilteredDf(Dataset<Row> dfOne, Dataset<Row> dfTwo, double value, int cores){

        Map<String, Double> map = getMinMaxValues(dfOne, dfTwo, value);
        double xMin = map.get(X_MIN);
        double xMax = map.get(X_MAX);
        double yMin = map.get(Y_MIN);
        double yMax = map.get(Y_MAX);

        Dataset<Row> unionDf = dfOne.union(dfTwo);

        Dataset<Row> unionFilteredDf = unionDf.filter(col("X").gt(xMin)
                .and(col("X").lt(xMax))
                .and(col("Y").gt(yMin))
                .and(col("Y").lt(yMax)));

        unionFilteredDf = unionFilteredDf.withColumn("piece", lit(""));
        unionFilteredDf = unionFilteredDf.withColumn("piece", when(col("X").equalTo(xMin), 0));

        double step = xMax - xMin / cores;

        for (int i = 0; i < cores; i ++)
            unionFilteredDf = unionFilteredDf
                    .withColumn("piece", when(col("X")
                            .gt(i*step + xMin).and(col("X")
                            .leq((i+1)*step + xMin)), 1)
                    .otherwise(col("piece")));

        return unionFilteredDf.cache();
    }

    public static double getStep(Dataset<Row> dfOne, Dataset<Row> dfTwo, double value, int cores){

        Map<String, Double> map = getMinMaxValues(dfOne, dfTwo, value);
        double xMin = map.get(X_MIN);
        double xMax = map.get(X_MAX);

        return xMax - xMin / cores;
    }

    public static Map<String, Double> getMinMaxValues(Dataset<Row> dfOne, Dataset<Row> dfTwo, double value){

        double xOneMin = getMin(dfOne, "X");
        double yOneMin = getMin(dfOne, "Y");
        double xOneMax = getMax(dfOne, "X");
        double yOneMax = getMax(dfOne, "Y");

        double xTwoMin = getMin(dfTwo, "X");
        double yTwoMin = getMin(dfTwo, "Y");
        double xTwoMax = getMax(dfTwo, "X");
        double yTwoMax = getMax(dfTwo, "Y");

        double xMin;
        double xMax;
        double yMin;
        double yMax;

        if (xTwoMin >= xOneMin)
            xMin = xTwoMin - value;
        else
            xMin = xOneMin - value;

        if (xTwoMax >= xOneMax)
            xMax = xOneMax + value;
        else
            xMax = xTwoMax + value;

        if (yTwoMin >= yOneMin)
            yMin = yTwoMin - value;
        else
            yMin = yOneMin - value;

        if (yTwoMax >= yOneMax)
            yMax = yOneMax + value;
        else
            yMax = yTwoMax + value;

        Map<String, Double> map = new HashMap<String, Double>();
        map.put(X_MIN, xMin);
        map.put(X_MAX, xMax);
        map.put(Y_MIN, yMin);
        map.put(Y_MAX, yMax);

        return map;
    }

    private static Double getMin(Dataset<Row> df, String columnName){

        return df.groupBy().min(columnName).collectAsList().get(0).getDouble(0);
    }

    private static Double getMax(Dataset<Row> df, String columnName){

        return df.groupBy().max(columnName).collectAsList().get(0).getDouble(0);
    }

//    private static String dataTypeString(Dataset<Row> df, String columnName) {
//        StructField[] fields = df.schema().fields();
//        String dataType = null;
//        for(StructField field: fields) {
//            if(field.name().equals(columnName)) {
//                dataType =  field.dataType().typeName();
//                break;
//            }
//        }
//        return dataType;
//    }
}
