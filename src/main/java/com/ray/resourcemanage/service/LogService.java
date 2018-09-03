package com.ray.resourcemanage.service;

import com.ray.resourcemanage.dao.ILogDao;
import com.ray.resourcemanage.entity.LogEntity;
import com.ray.resourcemanage.entity.SearchEntity;
import com.ray.resourcemanage.entity.SearchResult;
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
import java.util.Date;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private ILogDao logDao;

    public void logInfo(String message, Object... args) {

        StringBuilder logInfo = new StringBuilder(500);
        for (Object arg : args) {
            String[] strArr = message.split("\\{\\}", 2);
            if (arg == null) {
                arg = " ";
            }
            if (strArr.length > 1) {
                message = strArr[0] + String.valueOf(arg) + strArr[1];
            } else {
                message = strArr[0] + String.valueOf(arg);
            }
        }
        logInfo.append(message);

        LogEntity logEntity = new LogEntity();
        logEntity.setRemark(logInfo.toString());
        logEntity.setUpdateTime(new Date());
        logDao.save(logEntity);
    }

    @Transactional(readOnly = true)
    public SearchResult<LogEntity> findByPageAndParams(SearchEntity searchEntity) {
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        Specification<LogEntity> specification = getWhereClause(searchEntity);
        Page<LogEntity> pageInfo = logDao.findAll(specification, new PageRequest(searchEntity.getPageIndex() - 1, searchEntity.getPageSize(),sort));
        SearchResult<LogEntity> result = new SearchResult<>();
        List<LogEntity> list = pageInfo.getContent();
        result.setRows(list);
        result.setTotalPage(pageInfo.getTotalPages());
        result.setTotalCount(pageInfo.getTotalElements());
        return result;
    }

    private Specification<LogEntity> getWhereClause(final SearchEntity searchEntity){
        return (Specification<LogEntity>) (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();
            if(searchEntity.getStartTime()!=null){
                predicate.add(cb.greaterThanOrEqualTo(root.get("updateTime").as(Date.class), searchEntity.getStartTime()));
            }
            if(searchEntity.getEndTime()!=null){
                predicate.add(cb.lessThanOrEqualTo(root.get("updateTime").as(Date.class), searchEntity.getEndTime()));
            }
            if (!StringUtils.isEmpty(searchEntity.getSearchValue())){
                predicate.add(cb.like(root.get("remark").as(String.class), "%" + searchEntity.getSearchValue() + "%"));
            }
            Predicate[] pre = new Predicate[predicate.size()];
            return query.where(predicate.toArray(pre)).getRestriction();
        };
    }

}
