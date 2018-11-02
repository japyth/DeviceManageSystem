package com.ray.resourcemanage.userManage.action;

import com.ray.resourcemanage.constant.ConstResponse;
import com.ray.resourcemanage.entity.SearchEntity;
import com.ray.resourcemanage.entity.SearchResult;
import com.ray.resourcemanage.userManage.bean.SysRole;
import com.ray.resourcemanage.userManage.bean.SysUser;
import com.ray.resourcemanage.userManage.dao.IRoleDao;
import com.ray.resourcemanage.userManage.dao.IUserDao;
import com.ray.resourcemanage.userManage.dto.*;
import com.ray.resourcemanage.userManage.service.UserService;
import com.ray.resourcemanage.util.BaseResponse;
import com.ray.resourcemanage.util.BcryptUtil;
import com.ray.resourcemanage.util.ConstParam;
import com.ray.resourcemanage.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/admin")
@CrossOrigin
public class UserAction {
    @Autowired
    UserService userService;
    @Autowired
    IUserDao userDao;
    @Autowired
    IRoleDao roleDao;

    @RequestMapping("/getAllUser")
    public BaseResponse getAllUser(@RequestBody UserReqDto userReqDto) {
        String searchValue = userReqDto.getSearchValue();
        Integer pagesize = userReqDto.getPagesize();
        Integer pageIndex = userReqDto.getPageIndex();
        SearchEntity searchEntity = new SearchEntity();
        if (StringUtils.isEmpty(searchValue)) {
            searchEntity.setSearchValue(null);
        } else {
            searchEntity.setSearchValue(searchValue);
        }
        searchEntity.setPageIndex(pageIndex);
        searchEntity.setPageSize(pagesize);
        SearchResult<SysUser> searchResult = userService.findByPageAndParams(searchEntity);
        List<SysUser> sysUsers = searchResult.getRows();
        List<UserResDto> userResDtos = new ArrayList<>();
        if (sysUsers != null && sysUsers.size() > 0) {
            for (SysUser sysUser : sysUsers) {
                UserResDto userResDto = new UserResDto();
                userResDto.setUserId(sysUser.getUserId());
                userResDto.setUsername(sysUser.getUsername());
                Set<SysRole> sysRoleSet = sysUser.getSysRoleSet();
                StringBuilder roleBuilder = new StringBuilder();
                String roles = "";
                if (sysRoleSet != null && sysRoleSet.size() > 0) {
                    for (SysRole sysRole : sysRoleSet) {
                        roleBuilder.append(sysRole.getRemark()).append(",");
                    }
                    roles = roleBuilder.toString();
                    roles = roles.substring(0, roles.length() - 1);
                }
                userResDto.setRoles(roles);
                userResDtos.add(userResDto);
            }
        }
        SearchResult<UserResDto> searchResultRes = new SearchResult<>();
        searchResultRes.setRows(userResDtos);
        searchResultRes.setTotalCount(searchResult.getTotalCount());
        searchResultRes.setTotalPage(searchResult.getTotalPage());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        return ResultUtil.authResult(authorities, searchResultRes, authentication);
    }

    @RequestMapping("/addUser")
    public BaseResponse getAllUser(@RequestBody AddUserDto addUserDto) {
        if (addUserDto != null) {
            SysUser user = new SysUser();
            user.setUsername(addUserDto.getUsername());
            user.setHaveDevice(false);
            user.setPassword(BcryptUtil.bcryptEncode(ConstParam.INIT_PASSWORD));
            Set<SysRole> roles = new HashSet<>();
            if (addUserDto.getAdminAuth()) {
                roles.add(roleDao.findByRoleName("admin"));
            }
            if (addUserDto.getUserAuth()) {
                roles.add(roleDao.findByRoleName("user"));
            }
            user.setSysRoleSet(roles);
            userService.addUser(user);
            return new BaseResponse("success");
        } else {
            return new BaseResponse(false);
        }
    }

    @RequestMapping("/modifyUser")
    public BaseResponse getAllUser(@RequestBody ModifyUserDto modifyUserDto) {
        if (modifyUserDto != null) {
            SysUser userFromDb = userDao.findByUserId(modifyUserDto.getUserId());
            userFromDb.setUsername(modifyUserDto.getUsername());
            userFromDb.setHaveDevice(false);
            Set<SysRole> roles = new HashSet<>();
            if (modifyUserDto.isAdminAuth()) {
                roles.add(roleDao.findByRoleName("admin"));
            }
            if (modifyUserDto.isUserAuth()) {
                roles.add(roleDao.findByRoleName("user"));
            }
            userFromDb.setSysRoleSet(roles);
            userService.addUser(userFromDb);
            return new BaseResponse("success");
        } else {
            return new BaseResponse(false);
        }
    }

    @RequestMapping("/deleteUser")
    public BaseResponse getAllUser(@RequestBody DeleteUserDto deleteUserDto) {
        if (deleteUserDto != null) {
            String username = deleteUserDto.getUsername();
            SysUser sysUser = userDao.findByUsername(username);
            userDao.deleteById(sysUser.getUserId());
            return new BaseResponse("success");
        } else {
            return new BaseResponse(false);
        }
    }


}