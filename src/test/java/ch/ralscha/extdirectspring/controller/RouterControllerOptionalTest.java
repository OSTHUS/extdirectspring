/**
 * Copyright 2010-2014 Ralph Schaer <ralphschaer@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.ralscha.extdirectspring.controller;

import static org.fest.assertions.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.ralscha.extdirectspring.bean.ExtDirectPollResponse;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import ch.ralscha.extdirectspring.bean.SSEvent;
import ch.ralscha.extdirectspring.provider.RemoteProviderSimpleNamed.ResultObject;
import ch.ralscha.extdirectspring.provider.RemoteProviderTreeLoad.Node;
import ch.ralscha.extdirectspring.provider.Row;

import com.fasterxml.jackson.core.type.TypeReference;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:/testApplicationContext.xml")
public class RouterControllerOptionalTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@BeforeClass
	public static void beforeTest() {
		Locale.setDefault(Locale.US);
	}

	@Before
	public void setupMockMvc() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void testMethod1() {
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method1",
				"method1() called-1-3.100-requestParameter", 1, 3.1, "requestParameter");

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method1",
				"method1() called--1-3.100-requestParameter2", null, 3.1,
				"requestParameter2");

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method1",
				"method1() called-2-3.141-str", 2, null, "str");

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method1",
				"method1() called--1-3.141-str22", null, null, "str22");

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method1",
				"method1() called--1-3.141-null", null, null, null);

	}

	@Test
	public void testMethod2() {
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method2",
				"one", "one");

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method2",
				"default", new Object[] { null });
	}

	@Test
	public void testMethod4() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("header", "headerValue");
		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method4", "1;v;headerValue", 1, "v");
	}

	@Test
	public void testMethod5() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("header", "headerValue");
		headers.add("anotherName", "headerValue1");
		headers.add("anotherName", "headerValue2");

		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method5", "11;headerValue1", 11);
	}

	@Test
	public void testMethod6a() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("header", "headerValue");
		headers.add("anotherName", "headerValue1");

		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method6", "headerValue1");
	}

	@Test
	public void testMethod6b() throws Exception {
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method6",
				"default");
	}

	@Test
	public void testMethod7a() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("header", "headerValue");
		ControllerUtil.sendAndReceiveWithSession(mockMvc, headers,
				"remoteProviderOptional", "method7", "headerValue");
	}

	@Test
	public void testMethod7b() throws Exception {
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method7",
				"default");
	}

	@Test
	public void testMethod8a() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("last", "lastHeader");

		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method8", "100;default1;default2;lastHeader", 100);
	}

	@Test
	public void testMethod8b() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("last", "lastHeader");
		headers.add("header2", "2ndHeader");

		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method8", "100;default1;2ndHeader;lastHeader", 100);
	}

	@Test
	public void testMethod8c() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("last", "last");
		headers.add("header1", "1st");
		headers.add("header2", "2nd");

		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method8", "100;1st;2nd;last", 100);
	}

	@Test
	public void testMethod9() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("intHeader", "2");
		headers.add("booleanHeader", "true");

		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method9", "2;true");
		ControllerUtil.sendAndReceiveWithSession(mockMvc, headers,
				"remoteProviderOptional", "method9", "2;true");
	}

	@Test
	public void testMethod10And11() throws Exception {
		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method10",
		// "Ralph;one-two-;10", "Ralph", new String[] { "one", "two" }, 10);
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method10",
				"Ralph;one-;11", "Ralph", new String[] { "one" }, 11);
		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method10",
		// "Ralph;;12", "Ralph", new String[] {}, 12);
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method10",
				"Ralph;;13", "Ralph", null, 13);

		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method11",
		// "aStr;1+2+;20", "aStr", new int[] { 1, 2 }, 20);
		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method11",
		// "aStr;1+2+3+;21", "aStr", new int[] { 3, 1, 2 }, 21);
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method11",
				"aStr;3+;22", "aStr", new int[] { 3 }, 22);
		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method11",
		// "aStr;;23", "aStr", new int[] {}, 23);
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method11",
				"aStr;;24", "aStr", null, 24);
	}

	@Test
	public void testMethod12And13() throws Exception {
		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method12",
		// "Ralph;one-two-;10", "Ralph", new String[] { "one", "two" }, 10);

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method12",
				"Ralph;one-;11", "Ralph", new String[] { "one" }, 11);

		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method12",
		// "Ralph;;12", "Ralph", new String[] {}, 12);

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method12",
				"Ralph;;13", "Ralph", null, 13);

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method13",
				"aStr;1+2+;20", "aStr", 20, new int[] { 1, 2 });

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method13",
				"aStr;3+1+2+;21", "aStr", 21, new int[] { 3, 1, 2 });

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method13",
				"aStr;3+;22", "aStr", 22, new int[] { 3 });

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method13",
				"aStr;;23", "aStr", 23, new int[] {});

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method13",
				"aStr;;24", "aStr", 24, null);
	}

	@Test
	public void testMethod16() {
		List<Cookie> cookies = new ArrayList<Cookie>();
		cookies.add(new Cookie("intCookie", "1"));
		cookies.add(new Cookie("booleanCookie", "true"));
		ControllerUtil.sendAndReceive(mockMvc, null, cookies, "remoteProviderOptional",
				"method16", "1;true", (Object[]) null);
		ControllerUtil.sendAndReceive(mockMvc, null, null, "remoteProviderOptional",
				"method16", "-1;false", (Object[]) null);
	}

	@Test
	public void testMethod17() {
		ControllerUtil.sendAndReceive(mockMvc, null, null, "remoteProviderOptional",
				"method17", "theDefaultValue", (Object[]) null);
		ControllerUtil.sendAndReceive(mockMvc, null,
				Collections.singletonList(new Cookie("stringCookie", "str")),
				"remoteProviderOptional", "method17", "str", (Object[]) null);
	}

	@Test
	public void testMethod18() {
		ControllerUtil.sendAndReceive(mockMvc, null,
				Collections.singletonList(new Cookie("nameOfTheCookie", "cookieValue")),
				"remoteProviderOptional", "method18", "cookieValue", (Object[]) null);
		ControllerUtil.sendAndReceive(mockMvc, null, null, "remoteProviderOptional",
				"method18", "default", (Object[]) null);
	}

	@Test
	public void testMethod19() {
		ControllerUtil.sendAndReceive(mockMvc, null,
				Collections.singletonList(new Cookie("stringCookie", "aString")),
				"remoteProviderOptional", "method19", "aString", (Object[]) null);
		ControllerUtil.sendAndReceive(mockMvc, null, null, "remoteProviderOptional",
				"method19", Void.TYPE, (Object[]) null);
	}

	@Test
	public void testPoll1a() throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("id", "2");

		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll1", "opoll1", params, null);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll1");
		assertThat(resp.getData()).isEqualTo("Result: 2");
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

	@Test
	public void testPoll1b() throws Exception {

		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll1", "opoll1", null, null);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("exception");
		assertThat(resp.getName()).isEqualTo("opoll1");
		assertThat(resp.getData()).isNull();
		assertThat(resp.getMessage()).isEqualTo("Server Error");
		assertThat(resp.getWhere()).isNull();
	}

	@Test
	public void testPoll2a() throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("id", "7");

		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll2", "opoll2", params, null);
		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll2");
		assertThat(resp.getData()).isEqualTo(Integer.valueOf(14));
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

	@Test
	public void testPoll2b() throws Exception {
		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll2", "opoll2", null, null, null, true);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll2");
		assertThat(resp.getData()).isEqualTo(Integer.valueOf(4));
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

	@Test
	public void testPoll3a() throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("id", "3");

		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll3", "opoll3", params, null);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll3");
		assertThat(resp.getData()).isEqualTo(Integer.valueOf(6));
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

	@Test
	public void testPoll3b() throws Exception {
		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll3", "opoll3", null, null);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll3");
		assertThat(resp.getData()).isNull();
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

	@Test
	public void testPoll4() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("header", "headerValue");

		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll4", "opoll4", null, headers);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll4");
		assertThat(resp.getData()).isEqualTo("100;dummy;headerValue");
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

	@Test
	public void testPoll5() throws Exception {
		List<Cookie> cookies = new ArrayList<Cookie>();
		cookies.add(new Cookie("cookie", "cookieValue"));

		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll5", "opoll5", null, null, cookies);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll5");
		assertThat(resp.getData()).isEqualTo("23;dummy;cookieValue");
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

	@Test
	public void testNamed1() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("d", 2.1);
		params.put("s", "aString");
		params.put("i", 30);
		ControllerUtil.sendAndReceiveNamed(mockMvc, "remoteProviderOptional",
				"namedMethod1", "namedMethod1() called-30-2.100-aString", params);

		params = new HashMap<String, Object>();
		params.put("i", 20);
		params.put("de", 2.1);
		params.put("s", "aString");
		ControllerUtil.sendAndReceiveNamed(mockMvc, "remoteProviderOptional",
				"namedMethod1", "namedMethod1() called-20-3.141-aString", params);

		params = new HashMap<String, Object>();
		params.put("i", 20);
		params.put("s", "aString");
		ControllerUtil.sendAndReceiveNamed(mockMvc, "remoteProviderOptional",
				"namedMethod1", "namedMethod1() called-20-3.141-aString", params);

		params = new HashMap<String, Object>();
		params.put("i", "30");
		params.put("s", 100.45);
		params.put("d", "3.141");
		ControllerUtil.sendAndReceiveNamed(mockMvc, "remoteProviderOptional",
				"namedMethod1", "namedMethod1() called-30-3.141-100.45", params);

		params = new HashMap<String, Object>();
		params.put("s", "aString");
		ControllerUtil.sendAndReceiveNamed(mockMvc, "remoteProviderOptional",
				"namedMethod1", "namedMethod1() called--1-3.141-aString", params);

		params = new HashMap<String, Object>();
		ControllerUtil.sendAndReceiveNamed(mockMvc, "remoteProviderOptional",
				"namedMethod1", "namedMethod1() called--1-3.141-default", params);
	}

	@Test
	public void testNamed2() {
		ResultObject expectedResult = new ResultObject("Miller", 10, Boolean.TRUE);
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("lastName", expectedResult.getName());
		params.put("theAge", expectedResult.getAge());
		params.put("active", expectedResult.getActive());
		ResultObject result = (ResultObject) ControllerUtil.sendAndReceiveNamed(mockMvc,
				"remoteProviderOptional", "namedMethod2", ResultObject.class, params);
		assertThat(result).isEqualTo(expectedResult);

		expectedResult = new ResultObject("Ralph", 21, Boolean.FALSE);
		params = new LinkedHashMap<String, Object>();
		params.put("lastName", expectedResult.getName());
		params.put("active", expectedResult.getActive());
		result = (ResultObject) ControllerUtil.sendAndReceiveNamed(mockMvc,
				"remoteProviderOptional", "namedMethod2", ResultObject.class, params);
		assertThat(result).isEqualTo(expectedResult);

		expectedResult = new ResultObject("Joe", 21, Boolean.TRUE);
		params = new LinkedHashMap<String, Object>();
		params.put("lastName", expectedResult.getName());
		result = (ResultObject) ControllerUtil.sendAndReceiveNamed(mockMvc,
				"remoteProviderOptional", "namedMethod2", ResultObject.class, params);
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void testNamed3() {
		List<Cookie> cookies = new ArrayList<Cookie>();
		cookies.add(new Cookie("aSimpleCookie", "ralph"));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("i", 99L);
		ControllerUtil.sendAndReceiveNamed(mockMvc, null, cookies,
				"remoteProviderOptional", "namedMethod3", "99:ralph", params);

		params = new HashMap<String, Object>();
		params.put("i", 101L);
		ControllerUtil.sendAndReceiveNamed(mockMvc, null, null, "remoteProviderOptional",
				"namedMethod3", "101:defaultCookieValue", params);

		params = new HashMap<String, Object>();
		ControllerUtil.sendAndReceiveNamed(mockMvc, null, null, "remoteProviderOptional",
				"namedMethod3", "100:defaultCookieValue", params);
	}

	@Test
	public void testNamed4() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("aSimpleHeader", "theHeaderValue");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bd", new BigDecimal("1.1"));
		ControllerUtil.sendAndReceiveNamed(mockMvc, headers, null,
				"remoteProviderOptional", "namedMethod4", "1.1:theHeaderValue", params);

		params = new HashMap<String, Object>();
		params.put("bd", new BigDecimal("1.2"));
		ControllerUtil.sendAndReceiveNamed(mockMvc, null, null, "remoteProviderOptional",
				"namedMethod4", "1.2:defaultHeaderValue", params);

		params = new HashMap<String, Object>();
		ControllerUtil.sendAndReceiveNamed(mockMvc, null, null, "remoteProviderOptional",
				"namedMethod4", "3.141:defaultHeaderValue", params);
	}

	@Test
	public void testSse1() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "1");
		params.put("dummy", "sr");

		List<SSEvent> events = ControllerUtil.performSseRequest(mockMvc,
				"remoteProviderOptional", "sse1", params, null, null);
		assertThat(events).hasSize(1);
		SSEvent event = events.get(0);

		assertThat(event.getEvent()).isNull();
		assertThat(event.getComment()).isNull();
		assertThat(event.getData()).startsWith("1:sr");
		assertThat(event.getId()).isNull();
		assertThat(event.getRetry()).isNull();

		params = new HashMap<String, String>();
		params.put("id", "2");

		events = ControllerUtil.performSseRequest(mockMvc, "remoteProviderOptional",
				"sse1", params, null, null);
		assertThat(events).hasSize(1);
		event = events.get(0);

		assertThat(event.getEvent()).isNull();
		assertThat(event.getComment()).isNull();
		assertThat(event.getData()).startsWith("2:default_value");
		assertThat(event.getId()).isNull();
		assertThat(event.getRetry()).isNull();

		events = ControllerUtil.performSseRequest(mockMvc, "remoteProviderOptional",
				"sse1", null, null, null);
		assertThat(events).hasSize(1);
		event = events.get(0);

		assertThat(event.getEvent()).isNull();
		assertThat(event.getComment()).isNull();
		assertThat(event.getData()).startsWith("-1:default_value");
		assertThat(event.getId()).isNull();
		assertThat(event.getRetry()).isNull();
	}

	@Test
	public void testSse2() throws Exception {
		List<Cookie> cookies = new ArrayList<Cookie>();
		cookies.add(new Cookie("cookie", "cookieValue"));
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "11");

		List<SSEvent> events = ControllerUtil.performSseRequest(mockMvc,
				"remoteProviderOptional", "sse2", params, null, cookies);
		assertThat(events).hasSize(1);
		SSEvent event = events.get(0);

		assertThat(event.getEvent()).isNull();
		assertThat(event.getComment()).isNull();
		assertThat(event.getData()).isEqualTo("11;cookieValue");
		assertThat(event.getId()).isEqualTo("2");
		assertThat(event.getRetry()).isNull();

		events = ControllerUtil.performSseRequest(mockMvc, "remoteProviderOptional",
				"sse2", params, null, null);
		assertThat(events).hasSize(1);
		event = events.get(0);

		assertThat(event.getEvent()).isNull();
		assertThat(event.getComment()).isNull();
		assertThat(event.getData()).isEqualTo("11;defaultCookieValue");
		assertThat(event.getId()).isEqualTo("2");
		assertThat(event.getRetry()).isNull();
	}

	@Test
	public void testSse3() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("requestHeader", "requestHeader");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "12");

		List<SSEvent> events = ControllerUtil.performSseRequest(mockMvc,
				"remoteProviderOptional", "sse3", params, headers, null);
		assertThat(events).hasSize(1);
		SSEvent event = events.get(0);

		assertThat(event.getEvent()).isNull();
		assertThat(event.getComment()).isNull();
		assertThat(event.getData()).isEqualTo("12;requestHeader");
		assertThat(event.getId()).isEqualTo("3");
		assertThat(event.getRetry()).isNull();

		events = ControllerUtil.performSseRequest(mockMvc, "remoteProviderOptional",
				"sse3", params, null, null);
		assertThat(events).hasSize(1);
		event = events.get(0);

		assertThat(event.getEvent()).isNull();
		assertThat(event.getComment()).isNull();
		assertThat(event.getData()).isEqualTo("12;defaultRequestHeaderValue");
		assertThat(event.getId()).isEqualTo("3");
		assertThat(event.getRetry()).isNull();
	}

	@Test
	public void testStoreRead1() {
		Map<String, Object> readRequest = new HashMap<String, Object>();
		readRequest.put("id", 10);
		readRequest.put("query", "name");

		ExtDirectStoreResult<Row> storeResponse = (ExtDirectStoreResult<Row>) ControllerUtil
				.sendAndReceive(mockMvc, "remoteProviderOptional", "storeRead1",
						new TypeReference<ExtDirectStoreResult<Row>>() {
							// nothing here
						}, readRequest);

		assertThat(storeResponse.getTotal()).isEqualTo(50L);
		assertThat(storeResponse.getRecords()).hasSize(50);
		int ix = 0;
		for (Row row : storeResponse.getRecords()) {
			assertThat(row.getName()).startsWith("name: " + ix + ":10;en");
			ix += 2;
		}

		readRequest = new HashMap<String, Object>();
		readRequest.put("query", "name");

		storeResponse = (ExtDirectStoreResult<Row>) ControllerUtil.sendAndReceive(
				mockMvc, "remoteProviderOptional", "storeRead1",
				new TypeReference<ExtDirectStoreResult<Row>>() {
					// nothing here
				}, readRequest);

		assertThat(storeResponse.getTotal()).isEqualTo(50L);
		assertThat(storeResponse.getRecords()).hasSize(50);
		ix = 0;
		for (Row row : storeResponse.getRecords()) {
			assertThat(row.getName()).startsWith("name: " + ix + ":20;en");
			ix += 2;
		}
	}

	@Test
	public void testStoreRead2() {

		HttpHeaders headers = new HttpHeaders();
		headers.add("requestHeader", "rValue");

		List<Cookie> cookies = new ArrayList<Cookie>();
		cookies.add(new Cookie("cookie", "cValue"));

		Map<String, Object> readRequest = new HashMap<String, Object>();
		readRequest.put("query", "name");

		ExtDirectStoreResult<Row> storeResponse = (ExtDirectStoreResult<Row>) ControllerUtil
				.sendAndReceive(mockMvc, headers, cookies, "remoteProviderOptional",
						"storeRead2", new TypeReference<ExtDirectStoreResult<Row>>() {
							// nothing here
						}, readRequest);

		assertThat(storeResponse.getTotal()).isEqualTo(50L);
		assertThat(storeResponse.getRecords()).hasSize(50);
		int ix = 0;
		for (Row row : storeResponse.getRecords()) {
			assertThat(row.getName()).startsWith("name: " + ix + ":cValue:rValue");
			ix += 2;
		}

		readRequest = new HashMap<String, Object>();
		readRequest.put("query", "name");

		storeResponse = (ExtDirectStoreResult<Row>) ControllerUtil.sendAndReceive(
				mockMvc, "remoteProviderOptional", "storeRead2",
				new TypeReference<ExtDirectStoreResult<Row>>() {
					// nothing here
				}, readRequest);

		assertThat(storeResponse.getTotal()).isEqualTo(50L);
		assertThat(storeResponse.getRecords()).hasSize(50);
		ix = 0;
		for (Row row : storeResponse.getRecords()) {
			assertThat(row.getName()).startsWith(
					"name: " + ix + ":defaultCookie:defaultHeader");
			ix += 2;
		}
	}

	@Test
	public void testTreeLoad1() {
		Map<String, Object> requestParameters = new LinkedHashMap<String, Object>();
		requestParameters.put("node", "root");

		List<Cookie> cookies = new ArrayList<Cookie>();
		cookies.add(new Cookie("theCookie", "value"));

		List<Node> nodes = (List<Node>) ControllerUtil.sendAndReceive(mockMvc, false,
				null, cookies, null, "remoteProviderOptional", "treeLoad1", false,
				new TypeReference<List<Node>>() {/* nothinghere */
				}, requestParameters);

		String appendix = ":defaultValue2;value;true;true;true;en";

		assertThat(nodes).hasSize(5).containsSequence(
				new Node("n1", "Node 1" + appendix, false),
				new Node("n2", "Node 2" + appendix, false),
				new Node("n3", "Node 3" + appendix, false),
				new Node("n4", "Node 4" + appendix, false),
				new Node("n5", "Node 5" + appendix, false));

		requestParameters = new LinkedHashMap<String, Object>();
		requestParameters.put("node", "n2");
		requestParameters.put("foo", "f");

		nodes = (List<Node>) ControllerUtil.sendAndReceive(mockMvc, false, null, cookies,
				null, "remoteProviderOptional", "treeLoad1", false,
				new TypeReference<List<Node>>() {/* nothinghere */
				}, requestParameters);

		appendix = ":f;value;true;true;true;en";

		assertThat(nodes).hasSize(5).containsSequence(
				new Node("id1", "Node 2.1" + appendix, true),
				new Node("id2", "Node 2.2" + appendix, true),
				new Node("id3", "Node 2.3" + appendix, true),
				new Node("id4", "Node 2.4" + appendix, true),
				new Node("id5", "Node 2.5" + appendix, true));
	}

	@Test
	public void testTreeLoad2() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("aHeader", "false");

		Map<String, Object> requestParameters = new LinkedHashMap<String, Object>();
		requestParameters.put("node", "root");

		List<Node> nodes = (List<Node>) ControllerUtil.sendAndReceive(mockMvc, headers,
				"remoteProviderOptional", "treeLoad2", new TypeReference<List<Node>>() {/* nothinghere */
				}, requestParameters);

		String appendix = ":false;true;true";

		assertThat(nodes).hasSize(5).containsSequence(
				new Node("n1", "Node 1" + appendix, false),
				new Node("n2", "Node 2" + appendix, false),
				new Node("n3", "Node 3" + appendix, false),
				new Node("n4", "Node 4" + appendix, false),
				new Node("n5", "Node 5" + appendix, false));

		nodes = (List<Node>) ControllerUtil.sendAndReceive(mockMvc, (HttpHeaders) null,
				"remoteProviderOptional", "treeLoad2", new TypeReference<List<Node>>() {/* nothinghere */
				}, requestParameters);

		appendix = ":true;true;true";
		assertThat(nodes).hasSize(5).containsSequence(
				new Node("n1", "Node 1" + appendix, false),
				new Node("n2", "Node 2" + appendix, false),
				new Node("n3", "Node 3" + appendix, false),
				new Node("n4", "Node 4" + appendix, false),
				new Node("n5", "Node 5" + appendix, false));
	}

}
