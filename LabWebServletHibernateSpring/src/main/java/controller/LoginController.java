package controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import model.CustomerBean;
import model.CustomerService;

@Controller
@SessionAttributes(names={"user"})
public class LoginController {
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ApplicationContext context;
	
	@RequestMapping("/secure/login.controller")
	public String method(String username, String password, Model model, Locale locale) {
//接收資料
//驗證資料HAHAHAH
		Map<String, String> errors = new HashMap<String, String>();
		model.addAttribute("errors", errors);
		
		if(username==null || username.length()==0) {
			errors.put("username",
					context.getMessage("login.username.required", null, locale));
		}
		if(password==null || password.length()==0) {
			errors.put("password",
					context.getMessage("login.password.required", null, locale));
		}
		
		if(errors!=null && !errors.isEmpty()) {
			return "login.errors";
		}
		
//呼叫model 22222@@@@
		CustomerBean bean = customerService.login(username, password);
		
//根據model執行結果，導向view 33333 14546
		if(bean==null) {
			errors.put("password", "Login failed, please try again.");
			return "login.errors";
		} else {
			model.addAttribute("user", bean);
			return "login.success";			
		}
	}
}
