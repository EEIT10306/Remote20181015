package controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import misc.PrimitiveNumberEditor;
import model.ProductBean;
import model.ProductService;

@Controller
public class ProductController {
	@InitBinder
	public void registerPropertyEditor(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(java.util.Date.class,
				new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
		webDataBinder.registerCustomEditor(double.class,
				"price", new PrimitiveNumberEditor(Double.class, true));
		webDataBinder.registerCustomEditor(int.class,
				new PrimitiveNumberEditor(Integer.class, true));
	}
	
	@Autowired
	private ApplicationContext context;

	@Autowired
	private ProductService productService;
	
	@RequestMapping("/pages/product.controller")
	public String method(ProductBean bean,
			BindingResult bindingResult, String prodaction, Model model, String id) {
//接收資料
//轉換資料
		Map<String, String> errors = new HashMap<>();
		model.addAttribute("errorMsgs", errors);

		if(bindingResult!=null && bindingResult.hasFieldErrors()) {
			if(bindingResult.hasFieldErrors("id")) {
				errors.put("xxx1", "Id必須是整數(FormBean)");
			}
			if(bindingResult.hasFieldErrors("price")) {
				errors.put("xxx2", "Price必須是數字(FormBean)");
			}			
			if(bindingResult.hasFieldErrors("make")) {
				errors.put("xxx3", "Make必須是符合YYYY-MM-DD格式的日期(FormBean)");
			}
			if(bindingResult.hasFieldErrors("expire")) {
				errors.put("xxx4", "Expire必須是整數(FormBean)");
			}
		}
		
//驗證資料
		if("Insert".equals(prodaction) || "Update".equals(prodaction) || "Delete".equals(prodaction)) {
			if(id==null || id.length()==0) {
				Locale locale = LocaleContextHolder.getLocale();
				errors.put("xxx1", context.getMessage(
						"product.id.required", new String[] {prodaction}, locale));
			}
		}

		if(errors!=null && !errors.isEmpty()) {
			return "product.errors";
		}
		
//呼叫資料	，根據Model執行結果，呼叫View
		if("Select".equals(prodaction)) {
			List<ProductBean> result = productService.select(bean);
			model.addAttribute("select", result);
			return "product.select";
			
		} else if("Insert".equals(prodaction)) {
			ProductBean result = productService.insert(bean);
			if(result==null) {
				errors.put("action", "Insert fail");
			} else {
				model.addAttribute("insert", result);
			}
			return "product.errors";
		} else if("Update".equals(prodaction)) {
			ProductBean result = productService.update(bean);
			if(result==null) {
				errors.put("action", "Update fail");
			} else {
				model.addAttribute("update", result);
			}
			return "product.errors";
		} else if("Delete".equals(prodaction)) {
			boolean result = productService.delete(bean);
			if(!result) {
				model.addAttribute("delete", 0);
			} else {
				model.addAttribute("delete", 1);
			}
			return "product.errors";
		} else {
			errors.put("action", "Unknown Action:"+prodaction);
			return "product.errors";
		}
	}
}
