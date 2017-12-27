package com.framework.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.io.CharacterEscapes;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.TypeReference;

/**
 * @author wangxiao
 */
public class JsonStringUtils {

	private static Logger logger = Logger.getLogger(JsonStringUtils.class.getName());
	
	private static HtmlCharacterEscapes hce = new HtmlCharacterEscapes();

	/**
	 * list 转换 json string
	 * @param <T>
	 * @param ls
	 * @return String
	 */
	public static <T> String listToJsonString(List<T> ls) {
		String json = "";
		JsonGenerator jgen = null;
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();

		try {
			jgen = new JsonFactory().createJsonGenerator(sw);
			mapper.writeValue(jgen, ls);
			json = sw.toString();
			jgen.flush();
		} catch (JsonGenerationException e) {
			logger.error("JsonStringUtils " + e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("JsonStringUtils " + e.getMessage());
		} catch (IOException e) {
			logger.error("JsonStringUtils " + e.getMessage());
		} finally {
			try {
				jgen.close();
				jgen = null;
			} catch (IOException e) {
				logger.error("JsonStringUtils " + e.getMessage());
			}
			try {
				sw.close();
				sw = null;
			} catch (IOException e) {
				logger.error("JsonStringUtils " + e.getMessage());
			}
		}
		return json;
	}

	/**
	 * 根据传入的对象 生成json字符串
	 * 
	 * @param <T>
	 * @param t
	 * @return String
	 */
	public static <T> String objectToJsonString(T t) {
		String json = "";
		JsonGenerator jgen = null;
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();

		try {
			jgen = new JsonFactory().createJsonGenerator(sw);
			mapper.writeValue(jgen, t);
			json = sw.toString();
			jgen.flush();
		} catch (JsonGenerationException e) {
			logger.error("JsonStringUtils " + e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("JsonStringUtils " + e.getMessage());
		} catch (IOException e) {
			logger.error("JsonStringUtils " + e.getMessage());
		} finally {
			try {
				jgen.close();
				jgen = null;
			} catch (IOException e) {
				logger.error("JsonStringUtils " + e.getMessage());
			}
			try {
				sw.close();
				sw = null;
			} catch (IOException e) {
				logger.error("JsonStringUtils " + e.getMessage());
			}
		}
		return json;
	}
	
	/**
	 * 转换HTML 特殊字符
	 * @param t
	 * @return
	 */
	public static <T> String objectToJsonStringEscapes(T t) {
		String json = "";
		JsonGenerator jgen = null;
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();

		try {
			jgen = new JsonFactory().setCharacterEscapes(hce).createJsonGenerator(sw);
			mapper.writeValue(jgen, t);
			json = sw.toString();
			jgen.flush();
		} catch (JsonGenerationException e) {
			logger.error("JsonStringUtils " + e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("JsonStringUtils " + e.getMessage());
		} catch (IOException e) {
			logger.error("JsonStringUtils " + e.getMessage());
		} finally {
			try {
				jgen.close();
				jgen = null;
			} catch (IOException e) {
				logger.error("JsonStringUtils " + e.getMessage());
			}
			try {
				sw.close();
				sw = null;
			} catch (IOException e) {
				logger.error("JsonStringUtils " + e.getMessage());
			}
		}
		return json;
	}
	
	/**
	 * 转换HTML 特殊字符
	 * @param ls
	 * @return
	 */
	public static <T> String listToJsonStringEscapes(List<T> ls) {
		String json = "";
		JsonGenerator jgen = null;
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();

		try {
			jgen = new JsonFactory().setCharacterEscapes(hce).createJsonGenerator(sw);
			mapper.writeValue(jgen, ls);
			json = sw.toString();
			jgen.flush();
		} catch (JsonGenerationException e) {
			logger.error("JsonStringUtils " + e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("JsonStringUtils " + e.getMessage());
		} catch (IOException e) {
			logger.error("JsonStringUtils " + e.getMessage());
		} finally {
			try {
				jgen.close();
				jgen = null;
			} catch (IOException e) {
				logger.error("JsonStringUtils " + e.getMessage());
			}
			try {
				sw.close();
				sw = null;
			} catch (IOException e) {
				logger.error("JsonStringUtils " + e.getMessage());
			}
		}
		return json;
	}

	/**
	 * 根据JSON 字符串 返回 POJO
	 * 例如：Goods a = JsonStringUtils.jsonStringToObject(jsonString, Goods.class);
	 * @param <T>
	 * @param jsonString json 格式字符串
	 * @param clazz Object.class
	 * @return T
	 * @throws Exception
	 */
	public static <T> T jsonStringToObject(String jsonString, Class<T> clazz) throws Exception {
		T t = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (StringUtils.isNotEmpty(jsonString)) {
				t = mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true).readValue(jsonString, clazz);
			}
		} catch (JsonParseException e) {
			logger.error("JsonStringUtils " + e.getMessage());
			throw new Exception(e);
		} catch (JsonMappingException e) {
			logger.error("JsonStringUtils " + e.getMessage());
			throw new Exception(e);
		} catch (IOException e) {
			logger.error("JsonStringUtils " + e.getMessage());
			throw new Exception(e);
		}
		return t;
	}

	/**
	 * 返回 List Map 封装对象 List<Map<String, String>>;
	 * List<Map<String, String>> ls = JsonStringUtils.jsonStringToList(jsonString);
	 * @param jsonString json格式字符串
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	public static List<Map<String, String>> jsonStringToList(String jsonString) throws Exception {
		List<Map<String, String>> ls = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (StringUtils.isNotEmpty(jsonString)) {
				ls = mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true).readValue(jsonString, new TypeReference<List<Map<String, String>>>() {});
			}
		} catch (JsonParseException e) {
			logger.error("JsonStringUtils " + e.getMessage());
			throw new Exception(e);
		} catch (JsonMappingException e) {
			logger.error("JsonStringUtils " + e.getMessage());
			throw new Exception(e);
		} catch (IOException e) {
			logger.error("JsonStringUtils " + e.getMessage());
			throw new Exception(e);
		}
		return ls;
	}

	/**
	 * 根据 JSON 字符串 返回 LIST 对象，LIST ELEMENT 为 POJO || VO
	 * List<Goods> lsa = JsonStringUtils.jsonStringToList(jsonString, Goods.class);
	 * @param <T>
	 * @param jsonString
	 * @param cl
	 * @return List<T>
	 * @throws Exception
	 */
	public static <T> List<T> jsonStringToList(String jsonString, Class<T> cl) throws Exception {
		List<T> ls = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (StringUtils.isNotEmpty(jsonString)) {
				ls = mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true).readValue(jsonString, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, cl));
			}
		} catch (JsonParseException e) {
			logger.error("JsonStringUtils " + e.getMessage());
			throw new Exception(e);
		} catch (JsonMappingException e) {
			logger.error("JsonStringUtils " + e.getMessage());
			throw new Exception(e);
		} catch (IOException e) {
			logger.error("JsonStringUtils " + e.getMessage());
			throw new Exception(e);
		}
		return ls;
	}

	/**
	 * 根据 JSON 字符串 返回 LIST 对象，LIST ELEMENT 为 POJO || VO 此方法保留，
	 * 不推荐使用，此方法由 jsonStringToList(String jsonString, Class<T> cl) 方法替换
	 * 例如：List<Goods> lsa = JsonStringUtils.fromJsonStringList(jsonString, Goods.class);
	 * @deprecated
	 * @param <T>
	 * @param jsonString 符合规则的字符串
	 * @param cl POJO CLASS
	 * @return List<T>
	 * @throws Exception
	 */
	public static <T> List<T> fromJsonStringList(String jsonString, Class<T> cl) throws Exception {
		List<T> ls = new ArrayList<T>();
		if (StringUtils.isNotEmpty(jsonString)) {
			List<Map<String, String>> lsm = JsonStringUtils.jsonStringToList(jsonString);
			if (lsm != null) {
				for (Map<String, String> m : lsm) {
					T t = cl.newInstance();
					BeanUtils.populate(t, m);
					ls.add(t);
				}
			}
		}
		return ls;
	}
	
	private static class HtmlCharacterEscapes extends CharacterEscapes {

		private final int[] asciiEscapes;

		public HtmlCharacterEscapes() {
			int[] esc = CharacterEscapes.standardAsciiEscapesForJSON();
			esc['<'] = CharacterEscapes.ESCAPE_CUSTOM;
			esc['>'] = CharacterEscapes.ESCAPE_CUSTOM;
			esc['&'] = CharacterEscapes.ESCAPE_CUSTOM;
			esc['\''] = CharacterEscapes.ESCAPE_CUSTOM;
			esc['"'] = CharacterEscapes.ESCAPE_CUSTOM;
			asciiEscapes = esc;
		}

		@Override
		public int[] getEscapeCodesForAscii() {
			return asciiEscapes;
		}

		@Override
		public SerializableString getEscapeSequence(int ch) {
			return new SerializedString(StringEscapeUtils.escapeHtml4(Character.toString((char) ch)));
		}
	}
}