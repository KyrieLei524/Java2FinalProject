package com.example.springproject.prepareation;

import java.util.*;

public class textAnalyse {
    public static void main(String[] args) {
        String s = "小学生每天写家庭作业不超过60分钟，初中不超过90分钟\n" +
                "\n" +
                "《方案》要求，中小学校要严格依照课标教学，严禁擅自调整教育部和我市制定的课程计划，严禁挤占品德、艺术、体育、科学、综合实践活动以及国家要求开设的专题教育等课程。\n" +
                "\n" +
                "作业方面，严格控制学生家庭作业量，摒弃机械性、重复性和惩罚性作业。小学一、二年级不得布置书面家庭作业，三至六年级每天家庭作业总量控制在60分钟以内，初中每天家庭作业总量控制在90分钟以内。高中要结合实际合理安排作业时间。严格执行“一科一辅”规定，严禁使用未经区县教育行政部门审查备案的学习类APP。指导学生合理使用电子产品，严禁学生将手机带入课堂。\n" +
                "\n" +
                "同时，严格控制在校学习时间，学生上午上课时间小学不早于08:30，中学不早于08:00，学生下午上课时间不早于14:00（边远山区学校可酌情调整）。不得强制性要求走读生参加早、晚自习。严禁利用晚自习统一教学。保证学生睡眠时间，小学生每天不少于10小时，初中生每天不少于9小时，高中生每天不少于8小时。";
        forwardWord(s);

    }


    //选出指定位数的词
    public static Map<String, Integer> forwardWord(String content){
        Map<String, Integer> sortMap = new HashMap<>();
//        Map<String, Integer> sortMap = new TreeMap<>();
        content = content.replaceAll("[,、，。.\"“”'‘’:：1-9A-Za-z;；\\s\\n]","");
        Set<String> strList = getStrList(content); //切分文本 返回词集
        for (String word : strList){
            Integer integer = wordFreq(content, word);
            sortMap.put(word,integer);
        }
        sortMap = sortByValueDescending(sortMap); //降序排序
        int rankNum = 0;

        System.out.println("出现频率最高的前十个词！！！");
        for (Map.Entry<String,Integer> entry : sortMap.entrySet()){
            if (rankNum > 9){
                break;
            }
            System.out.println("关键词：" + entry.getKey()+"\t"+"词频:" + entry.getValue());
            rankNum ++;
        }
        return sortMap;

    }

    //降序排序
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
            {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * 字符串切分
     * @param inputString
     * @return
     */
    public static Set<String> getStrList(String inputString) {
        Set<String> dataSet = new HashSet<String>();
        for (int index = 0; index < inputString.length(); index++) {
            for (int i = index+2; i < inputString.length()+1; i++ ){
                String childStr = inputString.substring(index,i);
                dataSet.add(childStr);
            }

        }
        return dataSet;
    }


    //计算指定词在文本中出现的次数
    public static Integer wordFreq(String allStr,String appointStr){
        int i = allStr.indexOf(appointStr);
        int count = 0;
        for (int x = i; x <= allStr.length()-appointStr.length(); x++){
            if (allStr.substring(x,x + appointStr.length()).equals(appointStr)){
                count++;
            }
        }
        return count;
    }



}
