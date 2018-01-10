package com.xzchaoo.learn.freemarker;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * @author xzchaoo
 * @date 2018/1/7
 */
public class FreemarkerTest {
	@Test
	public void test1() throws IOException, TemplateException {
		Configuration cfg = new Configuration(Configuration.getVersion());
		cfg.setDefaultEncoding("utf-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);
		cfg.setDirectoryForTemplateLoading(new File("templates"));
		Template temp = cfg.getTemplate("test.ftlh");

// Create the root hash. We use a Map here, but it could be a JavaBean too.
		Map<String, Object> root = new HashMap<>();
		root.put("user", "Big Joe");
		OutputStreamWriter out = new OutputStreamWriter(System.out);
		temp.process(root, out);
		out.flush();
	}
}
