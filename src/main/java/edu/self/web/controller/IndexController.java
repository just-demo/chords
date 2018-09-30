package edu.self.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"", "/index"})
public class IndexController {
	private static final String INDEX = "index";
	
	@RequestMapping
	public String index(){
		return INDEX;
	}
}
