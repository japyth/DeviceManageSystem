package com.ray.resourcemanage.userManage.action;

import com.ray.resourcemanage.entity.SearchEntity;
import com.ray.resourcemanage.entity.SearchResult;
import com.ray.resourcemanage.springSecurity.entity.SysUser;
import com.ray.resourcemanage.userManage.dto.UserDto;
import com.ray.resourcemanage.userManage.service.UserManageService;
import com.ray.resourcemanage.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
@CrossOrigin
public class UserManageAction {
    @Autowired
    UserManageService userManageService;
    @RequestMapping("/getAllUser")
    public BaseResponse getAllUser(@RequestBody UserDto userDto) {
        SearchEntity searchEntity = new SearchEntity();
        String searchValue = userDto.getSearchValue();
        searchEntity.setSearchValue(searchValue);
        searchEntity.setPageIndex(userDto.getPageIndex());
        searchEntity.setPageSize(userDto.getPagesize());
        SearchResult<SysUser> searchResult = userManageService.findByPageAndParams(searchEntity);
        return new BaseResponse(searchResult);
    }

    /*@RequestMapping("/addUser")
    public BaseResponse addUser(SysUser sysUser) {

    }*/

}
