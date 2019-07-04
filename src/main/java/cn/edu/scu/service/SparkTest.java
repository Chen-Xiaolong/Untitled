package cn.edu.scu.service;

import cn.edu.scu.dao.EmploymentDao;
import cn.edu.scu.entity.Employment;
import org.apache.spark.api.java.JavaPairRDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;


@Service
public class SparkTest {

    @Autowired
    EmploymentDao employmentDao;

    private SparkConf conf = new SparkConf().setMaster("local").setAppName("MapPractice").set("spark.testing.memory", "838860800");

    private JavaSparkContext ctx = new JavaSparkContext(conf);

    public String test() {
        JavaRDD<Integer> arrRDD1 = ctx.parallelize(Arrays.asList(1, 2, 3, 4, 5));
        JavaRDD<Integer> arrRDD2 = ctx.parallelize(Arrays.asList(4, 5, 6, 7, 8));
        JavaPairRDD<Integer, Integer> result = arrRDD1.cartesian(arrRDD2);

        List<Employment> employments = employmentDao.queryByDutyId(5);
        List<String> all = new ArrayList<>();
        for (Employment employment : employments) {
            all.add(String.valueOf(employment.getSkillId()));
        }

        return Arrays.toString(all.toArray());
    }
}
