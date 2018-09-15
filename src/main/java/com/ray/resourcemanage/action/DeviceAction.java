package com.ray.resourcemanage.action;

import com.ray.resourcemanage.constant.ConstResponse;
import com.ray.resourcemanage.dao.IDeviceDao;
import com.ray.resourcemanage.dto.DeviceDto;
import com.ray.resourcemanage.entity.Device;
import com.ray.resourcemanage.entity.SearchEntity;
import com.ray.resourcemanage.entity.SearchResult;
import com.ray.resourcemanage.service.DeviceService;
import com.ray.resourcemanage.service.LogService;
import com.ray.resourcemanage.util.BaseResponse;
import com.ray.resourcemanage.util.RequestUtil;
import com.ray.resourcemanage.util.ResultUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 */
@RestController
@RequestMapping("api/device")
@CrossOrigin
public class DeviceAction {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private LogService logService;

    @Autowired
    private IDeviceDao deviceDao;

    private Lock lock = new ReentrantLock();

    private Log log = LogFactory.getLog(this.getClass());

    @RequestMapping("/getAllDevice")
    public BaseResponse getAllDevice(@RequestBody DeviceDto deviceDto) {
        String deviceStatus = deviceDto.getDeviceStatus();
        String deviceType = deviceDto.getDeviceType();
        String searchValue = deviceDto.getSearchValue();
        Integer pagesize = deviceDto.getPagesize();
        Integer pageIndex = deviceDto.getPageIndex();
        SearchEntity searchEntity = new SearchEntity();
        if (StringUtils.isEmpty(deviceStatus)&&StringUtils.isEmpty(deviceType)) {
            searchEntity.setSearchOther(null);
        } else {
            Map<String, Object> searchOther = new HashMap<>();
            searchOther.put("deviceStatus", deviceStatus);
            searchOther.put("deviceType", deviceType);
            searchEntity.setSearchOther(searchOther);
        }
        if (StringUtils.isEmpty(searchValue)) {
            searchEntity.setSearchValue(null);
        } else {
            searchEntity.setSearchValue(searchValue);
        }
        searchEntity.setPageIndex(pageIndex);
        searchEntity.setPageSize(pagesize);
        SearchResult<Device> searchResult = deviceService.findByPageAndParams(searchEntity);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>)authentication.getAuthorities();
        return ResultUtil.authResult(authorities, searchResult, authentication);
    }

    //添加设备（默认已归还）
    @RequestMapping("/addDevice")
    public BaseResponse addDevice(Device device) {
        device.setUpdateTime(new Date());
        device.setBack(true);
        deviceService.saveDevice(device);
        String ip = RequestUtil.getCurrentIp();
        logService.logInfo("添加了设备名称：{}，序列号：{}，拥有者：{}，ip：{}，备注：{}", device.getDeviceName(), device.getSerialNumber(), device.getOwner(), ip, device.getRemark());

        return new BaseResponse("success");
    }


    //借出设备
    @RequestMapping("/borrowDevice")
    public BaseResponse borrowDevice(Device device) {

        try {

            lock.lock();

            if (device.getBorrower() == null || device.getBorrower().length() == 0) {
                return new BaseResponse(false, ConstResponse.ERROR_CODE, "借出人不能为空");
            }

            Device deviceFromDB = deviceDao.findByDeviceId(device.getDeviceId());

            if (deviceFromDB == null) {
                return new BaseResponse(false, ConstResponse.ERROR_CODE, "device不存在");
            }

            deviceFromDB.setBack(false);
            deviceFromDB.setBorrower(device.getBorrower());
            deviceFromDB.setUpdateTime(new Date());

            deviceService.saveDevice(deviceFromDB);
            String ip = RequestUtil.getCurrentIp();
            logService.logInfo("借出了设备，借出人：{}, 设备名称：{}，序列号：{}，拥有者：{}，ip：{}，备注：{}", deviceFromDB.getBorrower(), device.getDeviceName(), device.getSerialNumber(), device.getOwner(), ip, device.getRemark());

        } catch (Exception e) {
            log.error(e);
            return new BaseResponse(false, ConstResponse.ERROR_CODE, "服务器内部错误");
        } finally {
            lock.unlock();
        }

        return new BaseResponse("success");
    }

    //归还设备
    @RequestMapping("/returnDevice")
    public BaseResponse returnDevice(Device device) {

        try {
            lock.lock();

            Device deviceFromDB = deviceDao.findByDeviceId(device.getDeviceId());

            if (deviceFromDB == null) {
                return new BaseResponse(false, ConstResponse.ERROR_CODE, "device不存在");
            }

            String borrower = deviceFromDB.getBorrower();
            deviceFromDB.setBorrower("");
            deviceFromDB.setBack(true);
            deviceFromDB.setUpdateTime(new Date());

            deviceService.saveDevice(deviceFromDB);
            String ip = RequestUtil.getCurrentIp();
            logService.logInfo("归还了设备，归还人：{}, 设备名称：{}，序列号：{}，拥有者：{}，ip：{}，备注：{}", borrower, device.getDeviceName(), device.getSerialNumber(), device.getOwner(), ip, device.getRemark());

        } catch (Exception e) {
            log.error(e);
            return new BaseResponse(false, ConstResponse.ERROR_CODE, "服务器内部错误");
        } finally {
            lock.unlock();
        }

        return new BaseResponse("success");
    }



    @RequestMapping("/deleteDevice")
    public BaseResponse deleteDevice(Integer deviceId){
        try {

            lock.lock();

            Device deviceFromDB = deviceDao.findByDeviceId(deviceId);
            String ip = RequestUtil.getCurrentIp();
            logService.logInfo("删除了设备，设备名称：{}，序列号：{}，拥有者：{}，ip：{}，备注：{}， 最后更新时间：{}", deviceFromDB.getDeviceName(), deviceFromDB.getSerialNumber(), deviceFromDB.getOwner(), ip, deviceFromDB.getRemark(), deviceFromDB.getUpdateTime());

            deviceService.deleteDevice(deviceId);

        } catch (Exception e) {
            log.error(e);
            return new BaseResponse(false, ConstResponse.ERROR_CODE, "删除错误");
        } finally {
            lock.unlock();
        }

        return new BaseResponse("success");
    }

    //只能先归还再修改，未归还的无法执行修改操作
    @RequestMapping("/modifyDevice")
    public BaseResponse modifyDevice(Device device) {
        try {
            lock.lock();
            if(device.getDeviceName()==null){
                return new BaseResponse(false, ConstResponse.ERROR_CODE, "设备名称不能为空");
            }
            if(device.getOwner()==null){
                return new BaseResponse(false, ConstResponse.ERROR_CODE, "拥有者不能为空");
            }
            Device deviceFromDB = deviceDao.findByDeviceId(device.getDeviceId());

            if (deviceFromDB == null) {
                return new BaseResponse(false, ConstResponse.ERROR_CODE, "device不存在");
            }
            deviceFromDB.setDeviceName(device.getDeviceName());
            deviceFromDB.setOwner(device.getOwner());
            deviceFromDB.setUpdateTime(new Date());
            deviceFromDB.setRemark(device.getRemark());
            deviceFromDB.setDeviceType(device.getDeviceType());
            deviceFromDB.setSerialNumber(device.getSerialNumber());

            deviceService.saveDevice(deviceFromDB);
            String ip = RequestUtil.getCurrentIp();
            logService.logInfo("修改了设备， 设备名称：{}，序列号：{}，拥有者：{}，ip：{}，备注：{}", device.getDeviceName(), device.getSerialNumber(), device.getOwner(), ip, device.getRemark());

        } catch (Exception e) {
            log.error(e);
            return new BaseResponse(false, ConstResponse.ERROR_CODE, "服务器内部错误");
        }finally {
            lock.unlock();
        }
        return new BaseResponse("success");
    }


}