package cn.edu.scu.service.Impl;

import cn.edu.scu.dao.UserDao;
import cn.edu.scu.dto.UserResult;
import cn.edu.scu.entity.User;
import cn.edu.scu.enums.UserResultEnum;
import cn.edu.scu.service.UserService;
import cn.edu.scu.util.CreateMd5;
import cn.edu.scu.util.CreateSalt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CreateSalt createSalt;

    @Autowired
    private CreateMd5 createMd5;

    @Override
    public UserResult register(String username, String password) {
        if(username.length() < 5||password.equals("")){
            return new UserResult(UserResultEnum.INSUFFICIENT_PARAMETERS);
        }
        int result = userDao.selectCountByUserName(username);
        if (result >= 1) {
            return new UserResult(UserResultEnum.USERNAME_EXIST);
        }
        String salt = createSalt.getSalt();
        String passwordMd5 = createMd5.getMd5ByTwoParameter(password, salt);
        result = userDao.insertOne(username, passwordMd5, salt);
        if (result != 1) {
            return new UserResult(UserResultEnum.UNKNOWN_ERROR);
        } else {
            return new UserResult(UserResultEnum.REGISTER_SUCCESS);
        }
    }

    @Override
    public UserResult login(String username, String password) {
        if(username.equals("")||password.equals("")){
            return new UserResult(UserResultEnum.INSUFFICIENT_PARAMETERS);
        }
        int result = userDao.selectCountByUserName(username);
        if (result != 1) {
            return new UserResult(UserResultEnum.LOGIN_FAIL);
        } else {
            User user = userDao.selectByUserName(username);
            if (!user.getUserPasswordHash().equals(
                    createMd5.getMd5ByTwoParameter(password, user.getUserPasswordSalt()))) {
                return new UserResult(UserResultEnum.LOGIN_FAIL);
            } else {
                user.setUserPasswordHash("");
                user.setUserPasswordSalt("");
//                String userPasswordHash = user.getUserPasswordHash();
//                return new UserResult(1,
//                        createMd5.getMd5ByTwoParameter(username, userPasswordHash), user);
                return new UserResult(UserResultEnum.LOGIN_SUCCESS, user);
            }
        }
    }
}
