package cn.acqz.lottery.interfaces.test.vo2dto;

import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @description: 普通对象转换
 */
public class ApiTest01 {

    public void test_vo2dto(User user) {
        UserDTO userDTO = new UserDTO();

    }

    public void test_vo2dto(List<User> userList) {
        for (User user : userList) {
            UserDTO userDTO = new UserDTO();

            BeanUtils.copyProperties(user, userDTO);

        }
    }

    public void test_vo2dto() {
        User user = this.queryUserById();

        UserDTO userDTO = new UserDTO();
    }

    private User queryUserById() {
        return null;
    }

}
