package cn.edu.scu.util;

public interface CreateMd5 {

    String getMd5ByOneParameter(String str);

    String getMd5ByTwoParameter(String str1, String str2);

    String getMd5ByTwoParameterWithrole(String str1, String str2, String role);

}
