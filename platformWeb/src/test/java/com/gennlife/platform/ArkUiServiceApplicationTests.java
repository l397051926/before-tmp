package com.gennlife.platform;

import com.gennlife.platform.util.RedisUtil;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ArkUiServiceApplicationTests {

	@Test
	public void contextLoads() {
	}
	@Autowired
	private MockMvc mockMvc; // 模拟MVC对象，通过MockMvcBuilders.webAppContextSetup(this.wac).build()初始化。

	@Autowired
	private WebApplicationContext wac; // 注入WebApplicationContext

//    @Autowired
//    private MockHttpSession session;// 注入模拟的http session
//
//    @Autowired
//    private MockHttpServletRequest request;// 注入模拟的http request\

	/*@Before // 在测试开始前初始化工作
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}*/
	@Test
	public void test01(){

		try {
			String content = "{\"uid\":\"61eb90b6-20ce-4d01-a12e-b36f827e3d5d\",\"conditionStr\":\"asdfasdfasdf\",\"conditionName\":\"asdasdfa\"}";
			ResultActions abc = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/search/StoreSearchCondition").content(content))
					.andExpect(MockMvcResultMatchers.status().isOk());
			System.out.println(abc.andReturn().getResponse().getContentAsString());
		} catch (Exception e) {

			e.printStackTrace();
		}
	}


}
