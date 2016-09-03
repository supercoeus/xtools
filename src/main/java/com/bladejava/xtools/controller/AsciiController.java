package com.bladejava.xtools.controller;

import com.blade.annotation.Controller;
import com.blade.annotation.Route;
import com.blade.view.ModelAndView;

@Controller("ascii")
public class AsciiController {

	@Route("/")
	public ModelAndView index(ModelAndView mav){
		mav.setView("ascii/index.html");
		return mav;
	}
	
}
