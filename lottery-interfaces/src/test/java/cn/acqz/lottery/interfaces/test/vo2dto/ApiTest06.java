package cn.acqz.lottery.interfaces.test.vo2dto;

import cn.acqz.lottery.interfaces.test.vo2dto.bbb.User;

/**
 * @description: 引入包名转换
 */
public class ApiTest06 {

    public void test_vo2dto(User user) {

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserNickName(user.getUserNickName());
        userDTO.setUserHead(user.getUserHead());



    }

}
