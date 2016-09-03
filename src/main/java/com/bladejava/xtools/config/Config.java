package com.bladejava.xtools.config;

import com.blade.config.ApplicationConfig;
import com.blade.config.BaseConfig;
import com.blade.ioc.annotation.Component;
import com.blade.view.ViewSettings;
import com.blade.view.template.JetbrickTemplateEngine;

@Component
public class Config implements BaseConfig{

	@Override
	public void config(ApplicationConfig arg0) {
		JetbrickTemplateEngine templateEngine = new JetbrickTemplateEngine();
		ViewSettings.$().templateEngine(templateEngine);
		
	}

}
