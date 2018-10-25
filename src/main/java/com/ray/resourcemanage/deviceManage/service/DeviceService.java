package com.ray.resourcemanage.deviceManage.service;

import com.ray.resourcemanage.deviceManage.dao.IDeviceDao;
import com.ray.resourcemanage.deviceManage.entity.Device;
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
import java.util.List;
import java.util.Map;

/**
 * @Description:
 */
@Service
public class DeviceService {
    @Autowired
    private IDeviceDao deviceDao;

    public void saveDevice(Device device) {
        deviceDao.save(device);
    }

    @Transactional(readOnly = true)
    public SearchResult<Device> findByPageAndParams(SearchEntity searchEntity) {
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        Specification<Device> specification = getWhereClause(searchEntity);
        Page<Device> pageInfo = deviceDao.findAll(specification, new PageRequest(searchEntity.getPageIndex() - 1, searchEntity.getPageSize(),sort));
        SearchResult<Device> result = new SearchResult<>();
        List<Device> list = pageInfo.getContent();
        result.setRows(list);
        result.setTotalPage(pageInfo.getTotalPages());
        result.setTotalCount(pageInfo.getTotalElements());
        return result;
    }

    private Specification<Device> getWhereClause(final SearchEntity searchEntity){
        return (Specification<Device>) (root, query, cb) -> {
            List<Predicate> restrictionList = new ArrayList<>();
            Map<String, Object> map = searchEntity.getSearchOther();
            String deviceStatus = (String) map.get("deviceStatus");
            if(deviceStatus.equals("1")) {
                restrictionList.add(cb.equal(root.get("isBack").as(Boolean.class),true));
            }
            if(deviceStatus.equals("2")) {
                restrictionList.add(cb.equal(root.get("isBack").as(Boolean.class),false));
            }
            String deviceType = (String) map.get("deviceType");
            if(!deviceType.equals("设备类型")){
                restrictionList.add(cb.equal(root.get("deviceType").as(String.class),deviceType));
            }
            String owner = (String) map.get("owner");
            if(!StringUtils.isEmpty(owner)){
                restrictionList.add(cb.equal(root.get("owner").as(String.class),owner));
            }
            if (!StringUtils.isEmpty(searchEntity.getSearchValue())){
                restrictionList.add(cb.or(cb.like(root.get("deviceName").as(String.class), "%" + searchEntity.getSearchValue() + "%"),
                        cb.like(root.get("deviceType").as(String.class), "%" + searchEntity.getSearchValue() + "%"),
                        cb.like(root.get("owner").as(String.class), "%" + searchEntity.getSearchValue() + "%"),
                        cb.like(root.get("borrower").as(String.class), "%" + searchEntity.getSearchValue() + "%"),
                        cb.like(root.get("serialNumber").as(String.class), "%" + searchEntity.getSearchValue() + "%"),
                        cb.like(root.get("remark").as(String.class), "%" + searchEntity.getSearchValue() + "%")));
            }

            Predicate[] pre = new Predicate[restrictionList.size()];
            return query.where(restrictionList.toArray(pre)).getRestriction();
        };
    }

    @Transactional
    public void deleteDevice(Integer deviceId) {
        deviceDao.deleteByDeviceId(deviceId);
    }

}