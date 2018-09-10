package com.ray.resourcemanage.userManage.service;

import com.ray.resourcemanage.entity.SearchEntity;
import com.ray.resourcemanage.entity.SearchResult;
import com.ray.resourcemanage.springSecurity.entity.SysUser;
import com.ray.resourcemanage.userManage.dao.IUserManageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserManageService {
    @Autowired
    IUserManageDao userManageDao;

    public void saveUser(SysUser sysUser) {
        userManageDao.save(sysUser);
    }

    @Transactional(readOnly = true)
    public SearchResult<SysUser> findByPageAndParams(SearchEntity searchEntity) {
        Sort sort = new Sort(Sort.Direction.DESC,"userId");
        Specification<SysUser> specification = getWhereClause(searchEntity);
        Page<SysUser> pageInfo = userManageDao.findAll(specification, new PageRequest(searchEntity.getPageIndex() - 1, searchEntity.getPageSize(),sort));
        SearchResult<SysUser> result = new SearchResult<>();
        List<SysUser> list = pageInfo.getContent();
        result.setRows(list);
        result.setTotalPage(pageInfo.getTotalPages());
        result.setTotalCount(pageInfo.getTotalElements());
        return result;
    }

    private Specification<SysUser> getWhereClause(final SearchEntity searchEntity){
        return (Specification<SysUser>) (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (!StringUtils.isEmpty(searchEntity.getSearchValue())){
                predicate.add(cb.like(root.get("username").as(String.class), "%" + searchEntity.getSearchValue() + "%"));
            }

            Predicate[] pre = new Predicate[predicate.size()];
            return query.where(predicate.toArray(pre)).getRestriction();
        };
    }

}
