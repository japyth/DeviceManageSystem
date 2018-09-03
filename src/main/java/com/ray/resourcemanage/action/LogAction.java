package com.ray.resourcemanage.action;

import com.ray.resourcemanage.entity.LogEntity;
import com.ray.resourcemanage.entity.SearchEntity;
import com.ray.resourcemanage.entity.SearchResult;
import com.ray.resourcemanage.service.LogService;
import com.ray.resourcemanage.util.BaseResponse;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;

@RestController
@RequestMapping("api/logs")
@CrossOrigin
public class LogAction {

    @Autowired
    private LogService logService;

    private Log log = LogFactory.getLog(this.getClass());

    @RequestMapping("/getLogPage")
    public BaseResponse getLogPage(String startTime, String endTime, String searchValue, Integer pagesize, Integer pageIndex){
        SearchEntity searchEntity = new SearchEntity();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try{
            if(StringUtils.isEmpty(startTime)){
                searchEntity.setStartTime(null);
            } else {
                searchEntity.setStartTime(simpleDateFormat.parse(startTime));
            }
            if(StringUtils.isEmpty(endTime)){
                searchEntity.setEndTime(null);
            } else {
                searchEntity.setEndTime(simpleDateFormat.parse(endTime));
            }
            if(StringUtils.isEmpty(searchValue)){
                searchEntity.setSearchValue(null);
            } else {
                searchEntity.setSearchValue(searchValue);
            }
            searchEntity.setPageIndex(pageIndex);
            searchEntity.setPageSize(pagesize);
        }catch (Exception e){
            log.error(e);
        }
        SearchResult<LogEntity> searchResult = logService.findByPageAndParams(searchEntity);
        return new BaseResponse(searchResult);
    }

}
