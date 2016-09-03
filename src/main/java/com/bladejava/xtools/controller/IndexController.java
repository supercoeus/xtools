package com.bladejava.xtools.controller;

import com.blade.annotation.Controller;
import com.blade.annotation.Route;
import com.blade.view.ModelAndView;

@Controller
public class IndexController {

	@Route("/")
	public ModelAndView index(ModelAndView mav){
		mav.setView("index.html");
		return mav;
	}
	
}
