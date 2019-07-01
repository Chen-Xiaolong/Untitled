package cn.edu.scu.service;

import org.apache.spark.api.java.JavaPairRDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;


@Service
public class SparkTest {
    private SparkConf conf = new SparkConf().setMaster("local").setAppName("MapPractice");
    private JavaSparkContext ctx = new JavaSparkContext(conf);

    public String test() {
        JavaRDD<Integer> arrRDD1 = ctx.parallelize(Arrays.asList(1, 2, 3, 4, 5));
        JavaRDD<Integer> arrRDD2 = ctx.parallelize(Arrays.asList(4, 5, 6, 7, 8));
        JavaPairRDD<Integer, Integer> result = arrRDD1.cartesian(arrRDD2);
        return result.collect().toString();
    }
}
