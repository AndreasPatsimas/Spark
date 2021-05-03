package org.patsimas.spark_project.services;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.patsimas.spark_project.dto.DistanceJoinDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static org.patsimas.spark_project.utils.SparkUtils.getDf;
import static org.patsimas.spark_project.utils.SparkUtils.getUnionFilteredDf;

@Service
@PropertySource("classpath:application.properties")
public class DistanceJoinServiceImpl implements DistanceJoinService {

    @Value("${file.upload-dir}")
    private String fileStorageLocation;

    private static final String APP_NAME = "CSV To DataSet";
    private static final String MASTER = "local[*]";

    @Override
    public Set<DistanceJoinDto> fetchDistanceJoins(String fileNameOne, String fileNameTwo, Long value) {

        SparkSession sparkSession = SparkSession.builder()
                .appName(APP_NAME)
                .master(MASTER)
                .getOrCreate();

        SQLContext sqlContext = new  SQLContext(sparkSession);

        Dataset<Row> dfOne = getDf(sparkSession, fileStorageLocation + fileNameOne, "One");

        Dataset<Row> dfTwo = getDf(sparkSession, fileStorageLocation + fileNameTwo, "Two");

        Dataset<Row> unionFilteredDf = getUnionFilteredDf(dfOne, dfTwo, value);
        unionFilteredDf.createOrReplaceTempView("yo");
        sqlContext.sql("select * from yo where X = -4.50088 and Y = 48.35698").show();

        return new HashSet<>();
    }
}
