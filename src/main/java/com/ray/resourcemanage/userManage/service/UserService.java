package com.ray.resourcemanage.userManage.service;

import com.ray.resourcemanage.entity.SearchEntity;
import com.ray.resourcemanage.entity.SearchResult;
import com.ray.resourcemanage.userManage.dao.IUserDao;
import com.ray.resourcemanage.userManage.bean.SysUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private IUserDao userDao;
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = userDao.findByUsername(username);
        if (sysUser == null) {
            log.error("未查询到用户" + username + "的信息");
        }
        return sysUser;
    }

    @Transactional(readOnly = true)
    public SearchResult<SysUser> findByPageAndParams(SearchEntity searchEntity) {
        Sort sort = new Sort(Sort.Direction.DESC, "userId");
        Specification<SysUser> specification = getWhereClause(searchEntity);
        Page<SysUser> pageInfo = userDao.findAll(specification, PageRequest.of(searchEntity.getPageIndex() - 1, searchEntity.getPageSize(), sort));
        SearchResult<SysUser> result = new SearchResult<>();
        List<SysUser> list = pageInfo.getContent();
        result.setRows(list);
        result.setTotalPage(pageInfo.getTotalPages());
        result.setTotalCount(pageInfo.getTotalElements());
        return result;
    }

    private Specification<SysUser> getWhereClause(final SearchEntity searchEntity) {
        return (Specification<SysUser>) (root, query, cb) -> {
            List<Predicate> restrictionList = new ArrayList<>();
            if (!StringUtils.isEmpty(searchEntity.getSearchValue())) {
                restrictionList.add(cb.like(root.get("username").as(String.class), "%" + searchEntity.getSearchValue() + "%"));
            }
            Predicate[] pre = new Predicate[restrictionList.size()];
            return query.where(restrictionList.toArray(pre)).getRestriction();
        };
    }
}
