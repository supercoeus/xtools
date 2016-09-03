package com.bladejava.xtools.controller;

import com.blade.annotation.Controller;
import com.blade.annotation.Route;
import com.blade.view.ModelAndView;

@Controller("ascii")
public class AsciiController {

	@Route("img2ascii")
	public ModelAndView img2ascii(ModelAndView mav){
		mav.setView("ascii/img2ascii.html");
		return mav;
	}

	@Route("text2ascii")
	public ModelAndView text2ascii(ModelAndView mav){
		mav.setView("ascii/text2ascii.html");
		return mav;
	}
	
}
