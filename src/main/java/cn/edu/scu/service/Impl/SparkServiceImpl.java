package cn.edu.scu.service.Impl;
import cn.edu.scu.dao.SkillDao;
import cn.edu.scu.dao.UserDao;
import cn.edu.scu.dao.UserSkillDao;
import cn.edu.scu.dto.UserResult;
import cn.edu.scu.entity.User;
import cn.edu.scu.entity.UserSkill;
import cn.edu.scu.enums.UserResultEnum;
import cn.edu.scu.util.CreateMd5;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.*;

@Service
public class SparkServiceImpl {

    @Autowired
    private UserSkillDao userSkillDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SkillDao skillDao;
    @Autowired
    private CreateMd5 createMd5;

    private SparkConf conf = new SparkConf().setMaster("local").setAppName("MapPractice").set("spark.testing.memory", "838860800");
    private JavaSparkContext sc = new JavaSparkContext(conf);
    private String path = SparkServiceImpl.class.getClassLoader().getResource("").getPath();

    public UserResult analyse(String name, String hash, String aimjob){
        if(!isLogin(name, hash)){
            return new UserResult(UserResultEnum.UNLOGINED);
        }
        HashSet<String> proSkillList=new HashSet<>();
        HashSet<String> conSkillList=new HashSet<>();
        HashSet<String> skillList;
        List<String> skill = new ArrayList<>();

        User user = userDao.selectByUserName(name);
        List<UserSkill> userSkills = userSkillDao.queryByUserId(user.getUserId());
        if(userSkills.size() != 0){
            for (UserSkill userSkill : userSkills) {
                skill.add(skillDao.queryBySkillId(userSkill.getSkillId()).getSkillName());
            }
        }

//        JavaRDD<List<String>> transactions = sc.textFile("src/main/resources/"+aimjob+".txt")
//                .map(s -> {
//                    String[] tmp= s.split(",");
//                    List<String> ntmp=new ArrayList<String>();
//                    for(int i=1;i<tmp.length;i++)
//                        ntmp.add(tmp[i]);
//                    return ntmp;
//                });
//        double minSupport = 0.3;
        HashSet<String> hSet = new HashSet<>();
//        FPGrowthModel<String> model = new FPGrowth()
//                .setMinSupport(minSupport)
//                .run(transactions);
        FPGrowthModel<String> model;
        System.out.println(path);
        File file = new File(path+"model/FPTree/" + aimjob);
        if(file.exists()){
            model = (FPGrowthModel<String>)FPGrowthModel.load(sc.sc(), path+"model/FPTree/" + aimjob);
        } else {
            model = createFPTreeModel(aimjob);
        }

        for (FPGrowth.FreqItemset<String> s : model.freqItemsets().toJavaRDD().collect()) {
            //System.out.println("[" + Joiner.on(",").join(s.javaItems()) + "]" + s.freq());
            for (Object x : s.javaItems()) {
                hSet.add((String)x);
            }
        }
        skillList=hSet;
        AssociationRules rule = new AssociationRules()
                .setMinConfidence(0.15);
        JavaRDD<? extends AssociationRules.Rule<?>> run = rule.run(model.freqItemsets().toJavaRDD());

        JavaRDD<AssociationRules.Rule<String>>result = rule.run(model.freqItemsets().toJavaRDD());

//        result.foreach(x ->
//                System.out.println(x));

        List<AssociationRules.Rule<String>> newresult = result.collect();
        for(AssociationRules.Rule<String>x:newresult){
            for(String st:skill)
                if(x.javaAntecedent().toString().contains(st)){
//                    String[] nlist =x.javaAntecedent().toString().split(",");
                    String S=x.javaConsequent().toString();
                    S=S.substring(1,S.length()-1);
                    conSkillList.add(S);
                }
        }
        for(String x :skill){
            if(skillList.contains(x))
                proSkillList.add(x);
        }
        return new UserResult(UserResultEnum.OPERATION_SUCCESS, proSkillList.toString() + "\n" + conSkillList.toString());
    }

    private FPGrowthModel<String> createFPTreeModel(String aimjob){
        FPGrowthModel<String> model = null;
        try {
            JavaRDD<List<String>> transactions = sc.textFile(path+"data/"+aimjob+".txt")
                    .map(s -> {
                        String[] tmp= s.split(",");
                        List<String> ntmp=new ArrayList<String>();
                        for(int i=1;i<tmp.length;i++)
                            ntmp.add(tmp[i]);
                        return ntmp;
                    });
            double minSupport = 0.3;
            HashSet<String> hSet = new HashSet<>();
            model = new FPGrowth()
                    .setMinSupport(minSupport)
                    .run(transactions);
            model.save(sc.sc(), path+"model/FPTree/" + aimjob);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        System.out.println("\n\nFPTree model create succeed!\n\n");
        return model;
    }

    private boolean isLogin(String name, String hash) {
        if (name.equals("") || hash.equals(""))
            return false;
        int result = userDao.selectCountByUserName(name);
        if (result != 1) {
            return false;
        } else {
            User user = userDao.selectByUserName(name);
//            System.out.println(createMd5.getMd5ByTwoParameter(user.getUserName(), user.getUserPasswordHash()));
//            System.out.println(hash);
//            System.out.println(createMd5.getMd5ByTwoParameter(user.getUserName(), user.getUserPasswordHash()).equals(hash));
            return createMd5.getMd5ByTwoParameter(user.getUserName(), user.getUserPasswordHash()).equals(hash);
        }
    }
}