package com.ray.resourcemanage.userManage.action;

import com.ray.resourcemanage.constant.ConstResponse;
import com.ray.resourcemanage.userManage.bean.SysUser;
import com.ray.resourcemanage.userManage.dao.IUserDao;
import com.ray.resourcemanage.userManage.dto.ChangPwdDto;
import com.ray.resourcemanage.userManage.service.UserService;
import com.ray.resourcemanage.util.BaseResponse;
import com.ray.resourcemanage.util.BcryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pwd")
@CrossOrigin
public class PasswordAction {
    @Autowired
    UserService userService;
    @Autowired
    IUserDao userDao;

    @RequestMapping("/changePassword")
    public BaseResponse changePassword(ChangPwdDto changPwdDto) {
        String username = changPwdDto.getUsername();
        String password = changPwdDto.getPassword();
        String newPassword = changPwdDto.getNewPwd();
        SysUser sysUser  = userDao.findByUsername(username);
        if(BcryptUtil.checkBcypt(password,sysUser.getPassword())) {
            sysUser.setPassword((BcryptUtil.bcryptEncode(newPassword)));
            userService.addUser(sysUser);
            return new BaseResponse("success");
        } else {
            return new BaseResponse(false, ConstResponse.ERROR_CODE,"用户密码和初始密码不一致");
        }
    }
}