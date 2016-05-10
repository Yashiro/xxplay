package com.xxplay.listener;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * CommonsMultipartResolver,监听文件上传
 * 
 * @file:CommonsMultipartResolverExt.java
 * @package_name:com.xxplay.listener
 * @Project:xxplay
 *
 * @Author:陈明
 * @Copyright 陈明   2016 All Rights Reserved.
 * 
 * @data:2016年5月1日 上午12:07:32
 */
public class MultipartParsingResult extends CommonsMultipartResolver {
	
	public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
		String encoding = "utf-8";
		FileUpload fileUpload = prepareFileUpload(encoding);
		final HttpSession session = request.getSession();
		fileUpload.setProgressListener(new ProgressListenerImpl(session));
		try {
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
			return parseFileItems(fileItems, encoding);
		} catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(),ex);
		} catch (FileUploadException ex) {
			throw new MultipartException("Could not parse multipart servlet request", ex);
		}
	}
}