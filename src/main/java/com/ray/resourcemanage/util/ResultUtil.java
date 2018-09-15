package com.ray.resourcemanage.util;

import com.ray.resourcemanage.entity.SearchResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class ResultUtil {
    public static BaseResponse authResult(List<GrantedAuthority> authorities, SearchResult<?> searchResult, Authentication authentication) {
        if(authorities.size() == 1 && authorities.get(0).getAuthority().equals(ConstParam.ROLE_VISITOR)){
            String username = "游客";
            return new BaseResponse(searchResult,username,null);
        } else {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            List<String> roles = new ArrayList<>();
            for (GrantedAuthority authority : authorities) {
                roles.add(authority.getAuthority());
            }
            return new BaseResponse(searchResult,username,roles);
        }
    }

}