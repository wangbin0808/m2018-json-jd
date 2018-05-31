package com.jrj.jd.util;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * @description xml和object转换类
 * @author qiushun.sun
 * @date 2018.05.08
 *
 */
public class Xml2ObjUtil {

	/**
	 * 将String类型的xml转换成对象
	 */
	public static Object convertXmlStrToObject(Class<?> clazz, String xmlStr) {
		Object xmlObject = null;
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshal = context.createUnmarshaller();
			StringReader sr = new StringReader(xmlStr);
			xmlObject = unmarshal.unmarshal(sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlObject;
	}
}
