package cn.edu.scu.util.impl;

import cn.edu.scu.util.CreateMd5;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;


@Repository
public class CreateMd5Impl implements CreateMd5 {

    @Override
    public String getMd5ByOneParameter(String str) {
        String base = "aeasd@#$24asfasfdfh" + str;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    public String getMd5ByTwoParameter(String str1, String str2) {
        String base = str1 + "Afsd$SFasf%&aa*sd" + str2;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    public String getMd5ByTwoParameterWithrole(String str1, String str2, String role) {
        String base = str1 + "asffgjASH324SDsdfrr" + role + "gfaJAasf$" + str2;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
