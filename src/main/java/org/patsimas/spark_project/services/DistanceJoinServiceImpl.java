package org.patsimas.spark_project.services;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.patsimas.spark_project.dto.CoordinateDto;
import org.patsimas.spark_project.dto.DistanceJoinDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.patsimas.spark_project.utils.SparkUtils.*;

@Service
@PropertySource("classpath:application.properties")
public class DistanceJoinServiceImpl implements DistanceJoinService {

    @Value("${file.upload-dir}")
    private String fileStorageLocation;

    private static final String APP_NAME = "CSV To DataSet";
    private static final String MASTER = "local[*]";
    private static final String X_MIN = "xMin";

    @Override
    public Set<DistanceJoinDto> fetchDistanceJoins(Double value) {

        int cores = 12;

        SparkSession sparkSession = SparkSession.builder()
                .appName(APP_NAME)
                .master(MASTER)
                .getOrCreate();

        SQLContext sqlContext = new  SQLContext(sparkSession);
        JavaSparkContext sc = new JavaSparkContext(sparkSession.sparkContext());

        Dataset<Row> dfOne = getDf(sparkSession, "data/ais_one_hour", "One");
        Dataset<Row> dfTwo = getDf(sparkSession, "data/ais_one_hour2", "Two");

        Dataset<Row> unionFilteredDf = getUnionFilteredDf(dfOne, dfTwo, value, cores);
        unionFilteredDf.createOrReplaceTempView("initial");

        StructType schema = new  StructType()
                .add(new StructField("_id", DataTypes.StringType, true, Metadata.empty()))
                .add(new StructField("X", DataTypes.DoubleType, true, Metadata.empty()))
                .add(new StructField("Y", DataTypes.DoubleType, true, Metadata.empty()))
                .add(new StructField("dfId", DataTypes.StringType, true, Metadata.empty()))
                .add(new StructField("piece", DataTypes.IntegerType, true, Metadata.empty()));
        Dataset<Row> unionSlices = sqlContext.createDataFrame(new ArrayList<>(), schema);
        double step = getStep(dfOne, dfTwo, value, cores);
        Map<String, Double> map = getMinMaxValues(dfOne, dfTwo, value);
        double xMin = map.get(X_MIN);

        for (int i = 1; i < cores; i ++){

            Dataset<Row> tempDf = sqlContext.sql("select _id, X, Y, dfId, piece + 1 as piece from initial " +
                    "where x >= (" + i + "*" + step + " + " + xMin + " - " + value + ") " +
                    "and x <= (" + i + "*" + step + " + " + xMin + ")");

            unionSlices = unionSlices.union(tempDf);
        }
        Dataset<Row> finalDf = unionFilteredDf.union(unionSlices);

        finalDf.createOrReplaceTempView("final");

        long startTime = System.nanoTime();

        Dataset<Row> results = sqlContext.sql("select distinct a._id, a.X, a.Y, b._id, b.X, b.Y from final a " +
                "inner join final b on a.piece = b.piece " +
                "where ((a.X-b.X)*(a.X-b.X) + (a.Y-b.Y)*(a.Y-b.Y)) <= " + value * value + " and a.dfId != b.dfId");

        long endTime = System.nanoTime();

        results = results.cache();
        List<Row> rows = results.collectAsList();
        sc.parallelize(rows, 3);
        Set<DistanceJoinDto> distanceJoins = rows.parallelStream().map(row -> {

            CoordinateDto coordinateOne = new CoordinateDto(row.getString(0), row.getDouble(1), row.getDouble(2));
            CoordinateDto coordinateTwo = new CoordinateDto(row.getString(3), row.getDouble(4), row.getDouble(5));

            return new DistanceJoinDto(coordinateOne, coordinateTwo);
        }).collect(Collectors.toSet());

        System.out.println("Took " + (endTime - startTime) / 1000000000 + " sec");

        return distanceJoins;
    }

    //https://www.youtube.com/watch?v=YanzUI-30pI
}
