package cn.edu.scu.service.Impl;
import cn.edu.scu.dao.DutyDao;
import cn.edu.scu.dao.SkillDao;
import cn.edu.scu.dao.UserDao;
import cn.edu.scu.dao.UserSkillDao;
import cn.edu.scu.dto.UserResult;
import cn.edu.scu.entity.Duty;
import cn.edu.scu.entity.Skill;
import cn.edu.scu.entity.User;
import cn.edu.scu.entity.UserSkill;
import cn.edu.scu.enums.UserResultEnum;
import cn.edu.scu.util.CreateMd5;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.apache.spark.mllib.util.MLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.io.File;
import java.net.URL;
import java.util.*;

import static java.lang.Double.valueOf;

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
    @Autowired
    private DutyDao dutyDao;

    private SparkConf conf = new SparkConf().setMaster("local").setAppName("MapPractice").set("spark.testing.memory", "1073741824");
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
//        System.out.println(path);
        File file = new File(path+"model/FPTree/" + aimjob);
        if(file.exists()){
            System.out.println("\n\n\nModel existed\n\n\n");
            model = (FPGrowthModel<String>)FPGrowthModel.load(sc.sc(), path+"model/FPTree/" + aimjob);
        } else {
            System.out.println("\n\n\nCreate model\n\n\n");
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
//        JavaRDD<? extends AssociationRules.Rule<?>> run = rule.run(model.freqItemsets().toJavaRDD());

        JavaRDD<AssociationRules.Rule<String>>result = rule.run(model.freqItemsets().toJavaRDD());

//        result.foreach(x ->
//                System.out.println(x));
        boolean IsTCPIP=false;
        for(String rex:skill)
            if(rex.equals("TCP/IP"))IsTCPIP=true;
        String combine = new String();
        for(String rex:skill)
            combine+=","+rex;

        String[] confstr = new String[5];
        Double[] confdou = new Double[5];

        List<AssociationRules.Rule<String>> newresult = result.collect();
        for(AssociationRules.Rule<String>x:newresult){
            if(judge(x.javaAntecedent().toString(),combine)){
                double conf=x.confidence();
                boolean flag=false;
                String S=x.javaConsequent().toString();
                S=S.substring(1,S.length()-1);
                if(IsTCPIP==true&&S.equals("IP"))continue;
                if(S.equals("计算机"))continue;
                for(String j :skill){
                    if(j.equals(S))
                        flag=true;
                }
                if(flag==true)continue;
                for(int i=0;i<5;i++)
                    if(confstr[i]!=null&&confstr[i].equals(S)){
                        flag=true;
                        break;
                    }
                if(flag==true)continue;
                for(int i=0;i<5;i++)
                    if(confdou[i]==null){
                        confdou[i]=conf;
                        confstr[i]=S;
                        flag=true;
                        break;
                    }
                if(flag==true)continue;
                double setmin=1.0;
                int posmin=5;
                for(int i=0;i<5;i++)
                    if(confdou[i]<setmin){
                        posmin=i;
                        setmin=confdou[i];
                    }
                if(confdou[posmin]<conf){
                    confdou[posmin]=conf;
                    confstr[posmin]=S;
                }
            }
        }

        if(conSkillList.size()==0){
            int lack=5;
            for(String ext:skillList){
                if(lack==0)break;
                lack--;
                conSkillList.add(ext);
            }
        }
        for(int i=0;i<5;i++)
            if(confstr[i]!=null){
                conSkillList.add(confstr[i]);
            }

        for(String x :skill){
            if(skillList.contains(x))
                proSkillList.add(x);
        }
        return new UserResult(UserResultEnum.OPERATION_SUCCESS, proSkillList.toString() + "\n" + conSkillList.toString());
    }

    public UserResult recommend(String name, String hash){
        if(!isLogin(name, hash)){
            return new UserResult(UserResultEnum.UNLOGINED);
        }
        User user = userDao.selectByUserName(name);
        List<UserSkill> userSkills = userSkillDao.queryByUserId(user.getUserId());
        String input = "";
        if(userSkills.size() != 0){
            for (UserSkill userSkill : userSkills) {
                input += userSkill.getSkillId() + ":" + userSkill.getProficiency() + " ";
            }
            input = input.substring(0, input.length()-1);
        }


        List<Skill> skills = skillDao.queryAll();
//        List<String> skill_name_list = new ArrayList<>();
//        for (Skill skill : skills) {
//            skill_name_list.add(skill.getSkillName());
//        }
//        String [] skill_name = (String[])skill_name_list.toArray();
        List<String> duty_name_list = new ArrayList<>();
        List<Duty> duties = dutyDao.queryAll();
        for (Duty duty : duties) {
            duty_name_list.add(duty.getDutyName());
        }
        String [] joblist = new String[duty_name_list.size()];
        duty_name_list.toArray(joblist);




        String[] skill_num =input.split(" ");
        int attributes[] =new int[skill_num.length];
        double values[] =new double[skill_num.length];
        for(int i=0;i<skill_num.length;i++){
//            String []temp = skill_num[i].split(":");
            double temp1= valueOf(skill_num[i].split(":")[0]);
            double temp2= valueOf(skill_num[i].split(":")[1]);
            attributes[i] = (int)temp1;
            values[i] = temp2;
        }
        double []  d = new double[skills.size()];
        int temp =0;
        for(int i=0;i<skills.size();i++){
            if(temp<skill_num.length){
                if(i==attributes[temp]-1){
                    d[i] = values[temp];
                    temp+=1;
                }
            }
            else d[i]=0;
        }
        Vector test = Vectors.dense(d);


        NaiveBayesModel model;
        File file = new File(path + "model/NaiveBayes");
        if(file.exists()){
            System.out.println("\n\n\nModel existed\n\n\n");
            model = NaiveBayesModel.load(sc.sc(), path + "model/NaiveBayes");
        } else {
            System.out.println("\n\n\nCreate model\n\n\n");
            model = createNaiveBayesModel();
        }


        Vector v = model.predictProbabilities(test);

        //各标签的概率
        double[] job_label_probability = v.toArray();

        //最大概率的三个标签
        int []label = get_max3_label(job_label_probability.clone());

        //星级
        int []star_level=new int[3];
        for(int i=0;i<3;i++){
            int t1 =label[i];
            if(job_label_probability[t1]>=0.5)
                star_level[i] = 5;
            else if(job_label_probability[t1]<0.5&&job_label_probability[t1]>=0.1)
                star_level[i] = 4;
            else if(job_label_probability[t1]<0.1&&job_label_probability[t1]>=0.01)
                star_level[i] = 3;
            else if(job_label_probability[t1]<0.01&&job_label_probability[t1]>=0.001)
                star_level[i] = 2;
            else
                star_level[i] = 1;

        }

        String result = "";
        Duty duty = dutyDao.queryByDutyId(label[0] + 1);
        result += duty.getDutyName()+":"+duty.getDescription()+":"+star_level[0] + "|";
        duty = dutyDao.queryByDutyId(label[1] + 1);
        result += duty.getDutyName()+":"+duty.getDescription()+":"+star_level[1] + "|";
        duty = dutyDao.queryByDutyId(label[2] + 1);
        result += duty.getDutyName()+":"+duty.getDescription()+":"+star_level[2];

        return new UserResult(UserResultEnum.OPERATION_SUCCESS, result);

    }

    public FPGrowthModel<String> createFPTreeModel(String aimjob){
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
            File file = new File(path+"model/FPTree/" + aimjob);
            if(file.exists()){
                delFile(file);
            }
            model.save(sc.sc(), path+"model/FPTree/" + aimjob);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return model;
    }

    public NaiveBayesModel createNaiveBayesModel(){
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc.sc(), path + "data/final_data.txt").toJavaRDD();
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.99, 0.01});
        JavaRDD<LabeledPoint> trainData = splits[0].distinct();
        JavaRDD<LabeledPoint> testData = splits[1].distinct();

        //模型训练
        final NaiveBayesModel model = NaiveBayes.train(trainData.rdd());

        JavaPairRDD<Double, Double> predictionAndLabel = testData.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));


        //由测试数据得到模型分类精度
        double accuracy = predictionAndLabel.filter(pl -> pl._1().equals(pl._2())).count() / (double) testData.count();

        System.out.println("模型精度为："+accuracy);

        //取出三个概率最大的label，有一个符合即为正确
        JavaPairRDD<Vector, Double> probability_label = testData.mapToPair(p -> new Tuple2<>(model.predictProbabilities(p.features()), p.label()));
        double acc = probability_label.filter(f->{
            double[] a = f._1().toArray();
            double[] c = a.clone();
            int [] b = get_max3_label(c);
            double label1 = f._2;
            int label =(int)label1;
            return b[0]==label||b[1]==label|b[2]==label;
        }).count()/(double) testData.count();


//        取出概率最大的三个标签
        probability_label.foreach(f->{
            double[] a = f._1().toArray();
            double[] c = a.clone();
            int [] b = get_max3_label(c);
            System.out.println(b[0]+":"+a[b[0]]+" "+b[1]+":"+a[b[1]]+" "+b[2]+":"+a[b[2]]+"->"+f._2);
        });
        System.out.println("准确度:"+acc);
//        });

        File file = new File(path+"model/NaiveBayes");
        if(file.exists()){
            delFile(file);
        }
        model.save(sc.sc(), path+"model/NaiveBayes");


        return model;
    }


    //获取概率最大的三个标签进行输出
    public static int[] get_max3_label(double [] a){
        int max1 = 0;
        int max2 = 0;
        int max3 = 0;
        double temp = a[0];
        for(int i =0;i<a.length;i++){
            if(a[i]>temp){
                temp = a[i];
                max1 = i;
            }
        }
        a[max1] = a[max1]-temp;
        temp = a[0];
        for(int i =0;i<a.length;i++){
            if(a[i]>temp){
                temp = a[i];
                max2 = i;
            }
        }
        a[max2] = a[max2]-temp;
        temp = a[0];
        for(int i =0;i<a.length;i++){
            if(a[i]>temp){
                temp = a[i];
                max3 = i;
            }
        }
        int d[] = {max1,max2,max3};
        return d;

    }



    private boolean judge(String connection,String combine){
        String[] ret = connection.substring(1,connection.length()-1).split(", ");
        for(String res:ret){
            if(!combine.contains(res))
                return false;
        }
        return true;
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

    private static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }

}