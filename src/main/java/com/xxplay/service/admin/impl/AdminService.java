package com.xxplay.service.admin.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xxplay.core.base.AppContextUtils;
import com.xxplay.core.exception.ServiceException;
import com.xxplay.core.pojo.PageBean;
import com.xxplay.core.pojo.PageParams;
import com.xxplay.dao.admin.IAdminDao;
import com.xxplay.pojo.admin.Admin;
import com.xxplay.service.admin.IAdminService;
import com.xxplay.service.base.IAdminMenuService;
import com.xxplay.utils.Des;
import com.xxplay.utils.GenerateSequenceUtil;
import com.xxplay.utils.IConstant;

/**
 * 管理员服务 实现类
 * 
 * @file:AdminService.java
 * @package_name:com.xxplay.service.admin.impl
 * @Project:xxplay
 *
 * @Author:陈明
 * @Copyright 陈明   2016 All Rights Reserved.
 * 
 * @data:2016年4月9日
 */
@Service("adminService")
public class AdminService implements IAdminService{
	private static Logger logger = Logger.getLogger(AdminService.class);
	@Resource
	private IAdminDao adminDao;

	@Resource
	private IAdminMenuService adminMenuService;
	
	@Override
	public Admin getAdminByUserName(String userName) {
		return adminDao.queryAdminByUserName(userName);
	}

	@Override
	public Admin adminTranLogin(String userName, String password) throws ServiceException {
		Admin admin = getAdminByUserName(userName);
		if(admin == null){
			throw new ServiceException("EC10001");
		}
		//密码解码
		password = Des.strEnc(password,AppContextUtils.getPropertiesValue("login-key"),null,null);
		checkAdminPassword(password,admin.getPassword());
		
		return admin;
	}

	/**
	 * 判断用户密码是否正确
	 *
	 * @param inputPassword	
	 * 						用户输入密码
	 * @param adminPassword
	 * 						用户登录密码
	 *
	 * @author:陈明
	 * @data : 2016年4月9日
	 */
	private void checkAdminPassword(String inputPassword, String adminPassword) {
		
	}

	@Override
	public PageBean queryAdminListPage(Map<String, String> params) {
		int pageNo = Integer.valueOf(params.get("pageNo"));
		PageParams pageParams = PageParams.getInstance(pageNo,params);
		
		List<Admin> admins = adminDao.queryAdminListPage(pageParams);
		int totalNo = adminDao.queryAdminCountPage(pageParams);
		
		return new PageBean(totalNo, admins, pageNo);
	}

	@Override
	public void addAdmin(Admin admin) throws ServiceException {
		Admin admin1 = getAdminByUserName(admin.getUserName());
		if(admin1 != null){
			throw new ServiceException("","该管理员用户名已经存在了，请重新输入");
		}
		
		admin.setId(GenerateSequenceUtil.generateSequenceNo());
		admin.setCreateTime(new Date());
		admin.setStatus(IConstant.ADMIN_STUTS_USING);		//用户状态：启用 
		
		adminDao.insert(admin);
	}

	@Override
	public void updateAdminStatus(String id, String status) {
		Admin admin = new Admin();
		admin.setId(id);
		admin.setStatus(status);
		
		try {
			adminDao.updateByPrimaryKeySelective(admin);
		} catch (Exception e) {
			logger.error("更改管理员状态失败。管理员编号：" + id);
		}
	}

	@Override
	public void deleteAdmin(String id) {
		//删除管理员的基本信息
		adminDao.deleteByPrimaryKey(id);
		//删除管理员的菜单
		adminMenuService.deleteAdminMenu(id);
	}

	@Override
	public Admin getAdminById(String id) {
		return adminDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateAdmin(Admin admin) {
		try {
			adminDao.updateByPrimaryKeySelective(admin);
		} catch (Exception e) {
			logger.error("修改管理员基本信息失败。管理员编号：" + admin.getId());
		}
	}
}