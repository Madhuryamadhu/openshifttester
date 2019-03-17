package com.ownproj.invoice.controllers;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ownproj.invoice.container.CommonBean;
import com.ownproj.invoice.container.CommonDAO;




@Controller
public class CommonController {
	private static final Logger logger = LogManager.getLogger();

	//login page
	@RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
	public String login(@ModelAttribute("userForm") CommonBean bean,HttpServletRequest request, HttpServletResponse response,HttpSession session,Model model) {
		logger.info("Inside submitAction method:-"+bean.toString());
		CommonDAO dao= new CommonDAO();
		try {

			 if(bean.getUsername().equals("password")&&bean.getPassword().equals("password")) { 
				bean.setStatus(0);
				session.setAttribute("user", "Pradeep");
			}else{
				bean.setStatus(1); 
				model.addAttribute("status", 1+"");
				return "login";
			}
			 

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("returning from submitAction method:-"+bean.toString());
		return "formPage";
	}

	
	
	
	//FormPage
	@RequestMapping(value = "/Submit", method = RequestMethod.POST)
	public @ResponseBody CommonBean submitAction(@RequestBody CommonBean bean,HttpServletRequest request, HttpServletResponse response) {
		logger.info("Inside submitAction method:-"+bean.toString());
		CommonDAO dao= new CommonDAO();
		try {

			if(dao.submitAction(bean,request.getSession())) { 
				bean.setStatus(0); 
			}else{
				bean.setStatus(1); 
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("returning from submitAction method:-"+bean.toString());
		return bean;
	}

	@RequestMapping(value = "/loadProductDropDown", method = RequestMethod.POST)
	public @ResponseBody CommonBean loadProductDropDOwn(@RequestBody CommonBean bean,HttpServletRequest request, HttpServletResponse response) {
		logger.info("Inside loadProductDropDown method:-"+bean.toString());
		CommonDAO dao= new CommonDAO();
		try {
			if(dao.loadProducts(bean, request.getSession())) {
				bean.setStatus(0);
			}else {
				bean.setStatus(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("returning from loadProductDropDown method:-"+bean.toString());
		return bean;
	}

	
	//Details Table Page
	@RequestMapping(value = "/downloadPDF", method = RequestMethod.POST)
	public void downloadPDF(CommonBean bean,HttpServletRequest request, HttpServletResponse response) {
		logger.info("Inside downloadPDF method:-"+bean.toString());
		InputStream in = null;
		ServletOutputStream out = null;
		try {
			logger.info("Download Started......");
			String file =bean.getPdfFullPath(); 
			in = new BufferedInputStream(new FileInputStream(file)); 
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition","inline; filename="+bean.getPdfFileName()+""); 
			out =response.getOutputStream(); 
			IOUtils.copy(in, out); 
			response.flushBuffer();
			logger.info("Download Ended......");
		} catch (Exception e) {
		}finally{
			try {
				if(in != null) {
					in.close();
				}
				if(out != null) {
					out.close();
				}

			} catch (Exception e) {
				logger.error(" | Exception Occured "+e.getMessage(), e);
			}catch (Throwable e) {
				logger.error(" | Throwable Occured "+e.getMessage(), e);
			}
		}

		logger.info("returning from downloadPDF method:-"+bean.toString());
	}

	
	
	//View Products Page
	@RequestMapping(value = "/loadViewProducts", method = RequestMethod.POST)
	public String loadViewProducts(CommonBean bean,HttpServletRequest request, HttpServletResponse response) {
		logger.info("Inside viewProducts method:-"+bean.toString());
		//CommonDAO dao= new CommonDAO();
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("returning from viewProducts method:-"+bean.toString());
		return "viewProducts";
	}
	
	@RequestMapping(value = "/viewProduct", method = RequestMethod.POST)
	public @ResponseBody CommonBean viewProduct(@RequestBody CommonBean bean,HttpServletRequest request, HttpServletResponse response) {
		logger.info("Inside viewProduct method:-"+bean.toString());
		CommonDAO dao= new CommonDAO();
		try {
			if(dao.viewProduct(bean, request.getSession())) {
				bean.setStatus(0);
			}else {
				bean.setStatus(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("returning from viewProduct method:-"+bean.toString());
		return bean;
	}
	
	
	@RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
	public @ResponseBody CommonBean deleteProduct(@RequestBody CommonBean bean,HttpServletRequest request, HttpServletResponse response) {
		logger.info("Inside deleteProduct method:-"+bean.toString());
		CommonDAO dao= new CommonDAO();
		try {
			if(dao.deleteProduct(bean, request.getSession())) {
				bean.setStatus(0);
			}else {
				bean.setStatus(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("returning from deleteProduct method:-"+bean.toString());
		return bean;
	}
	
	@RequestMapping(value = "/hideProduct", method = RequestMethod.POST)
	public @ResponseBody CommonBean hideProduct(@RequestBody CommonBean bean,HttpServletRequest request, HttpServletResponse response) {
		logger.info("Inside hideProduct method:-"+bean.toString());
		CommonDAO dao= new CommonDAO();
		try {
			if(dao.hideProduct(bean, request.getSession())) {
				bean.setStatus(0);
			}else {
				bean.setStatus(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("returning from hideProduct method:-"+bean.toString());
		return bean;
	}
	
	@RequestMapping(value = "/loadCreateProduct", method = RequestMethod.POST)
	public String loadCreateProduct(CommonBean bean,HttpServletRequest request, HttpServletResponse response) {
		logger.info("Inside loadCreateProduct method:-"+bean.toString());
		return "createProduct";
	}
	
	@RequestMapping(value = "/createProduct", method = RequestMethod.POST)
	public @ResponseBody CommonBean createProduct(@RequestBody CommonBean bean,HttpServletRequest request, HttpServletResponse response) {
		logger.info("Inside createProduct method:-"+bean.toString());
		CommonDAO dao= new CommonDAO();
		try {
			if(dao.createProduct(bean, request.getSession())) {
				bean.setStatus(0);
			}else {
				bean.setStatus(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("returning from createProduct method:-"+bean.toString());
		return bean;
	}
	
	
	@RequestMapping(value = "/getDetailsForModify", method = RequestMethod.POST)
	public String getDetailsForModify(@ModelAttribute("userForm")CommonBean bean,HttpServletRequest request, HttpServletResponse response,Map<String, Object> model) {
		logger.info("Inside getDetailsForModify method:-"+bean.toString());
		CommonDAO dao= new CommonDAO();
		try {
			if(dao.getDetailsForModify(bean, request.getSession())) {
				bean.setStatus(0);
			}else {
				bean.setStatus(1);
			}
			model.put("userForm", bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("returning from getDetailsForModify method:-"+bean.toString());
		return "modifyProduct";
	}
	
	@RequestMapping(value = "/modifyProduct", method = RequestMethod.POST)
	public @ResponseBody CommonBean modifyProduct(@RequestBody CommonBean bean,HttpServletRequest request, HttpServletResponse response) {
		logger.info("Inside modifyProduct method:-"+bean.toString());
		CommonDAO dao= new CommonDAO();
		try {
			if(dao.modifyProduct(bean, request.getSession())) {
				bean.setStatus(0);
			}else {
				bean.setStatus(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("returning from modifyProduct method:-"+bean.toString());
		return bean;
	}
	
	
}
