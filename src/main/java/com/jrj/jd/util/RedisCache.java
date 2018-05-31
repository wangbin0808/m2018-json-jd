package com.jrj.jd.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @description redis缓存工具类
 * @author qiushun.sun
 * @date 2018.03.15
 *
 */
@Service("redisCache")
public class RedisCache {
	protected static Logger logger = Logger.getLogger(RedisCache.class);
	/**
	 * 设置 String
	 * 
	 * @param key
	 * @param value
	 */
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;

	/**
	 * 设置 过期时间
	 * 
	 * @param key
	 * @param seconds
	 *            以秒为单位
	 * @param value
	 */
	public void setString(String key, int seconds, String value) {
		if (StringUtils.isBlank(key) || StringUtils.isBlank(value))
			return;
		try {
			redisTemplate.opsForValue().set(key.getBytes(), value, seconds, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("Set keyex error : " + e);
		}
	}

	/**
	 * 获取String值
	 * 
	 * @param key
	 * @return value
	 */
	public String getString(String key) {

		if (StringUtils.isBlank(key)) {
			return null;
		}
		String name = null;
		try {
			if (redisTemplate.hasKey(key.getBytes())) {
				name = (String) redisTemplate.opsForValue().get(key.getBytes());
			}

		} catch (Exception e) {
			logger.error("Set key error : " + e);
		}
		return name;
	}

	/**
	 * 设置 list
	 * 
	 * @param <T>
	 * @param key
	 * @param value
	 * @param seconds
	 *            表示时间，s
	 */
	public <T> void setList(String key, Class<T> t, int seconds, List<T> list) {
		if (StringUtils.isBlank(key) || list == null)
			return;
		try {
			redisTemplate.opsForValue().set(key.getBytes(), ObjectTranscoder.serialize(list), seconds, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("Set key error : " + e);
		}
	}

	/**
	 * 获取list
	 * 
	 * @param <T>
	 * @param key
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(String key, Class<T> t) {
		if (StringUtils.isBlank(key))
			return null;
		List<T> list = null;
		try {
			Boolean hasKey = redisTemplate.hasKey(key.getBytes());
			if (hasKey) {
				byte[] in = (byte[]) redisTemplate.opsForValue().get(key.getBytes());
				list = (List<T>) ObjectTranscoder.deserialize(in);
			}
		} catch (Exception e) {
			logger.error("getList key error : " + e);
		}

		return list;
	}

	/**
	 * 设置 map
	 * 
	 * @param <T>
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public <T> void setMap(String key, int seconds, Map<String, T> map, Class<T> t) {

		if (StringUtils.isBlank(key) || map == null)
			return;
		try {
			redisTemplate.opsForValue().set(key.getBytes(), ObjectTranscoder.serialize(map), seconds, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("Set key error : " + e);
		}
	}

	/**
	 * 获取list
	 * 
	 * @param <T>
	 * @param key
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> getMap(String key, Class<T> t) {
		if (StringUtils.isBlank(key))
			return null;

		Map<String, T> map = null;
		try {
			Boolean hasKey = redisTemplate.hasKey(key.getBytes());
			if (hasKey) {
				byte[] in = (byte[]) redisTemplate.opsForValue().get(key.getBytes());
				map = (Map<String, T>) ObjectTranscoder.deserialize(in);
			}
		} catch (Exception e) {
			logger.error("getMap key error : " + e);
		}

		return map;
	}

	/**
	 * 删除指定的obj
	 */
	public void deleteObj(String key) {
		if (StringUtils.isBlank(key))
			return;
		redisTemplate.delete(key.getBytes());
	}

	/**
	 * 对象序列化后会报错 缓存object对象
	 * 
	 * @param key
	 * @param t
	 * @param seconds
	 */
	public <T> void setObj(String key, T t1, int seconds, Class<T> t) {
		if (StringUtils.isBlank(key) || t == null)
			return;
		try {
			// String name = MyJsonConverter.objectToString(t1);
			redisTemplate.opsForValue().set(key.getBytes(), ObjectTranscoder.serialize(t1), seconds, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("Set key error : " + e);
		}
	}

	/**
	 * 取存储的对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObj(String key, Class<T> t) {
		if (StringUtils.isBlank(key))
			return null;
		T t1 = null;
		try {
			// t1 = (T) redisTemplate.opsForValue().get(key.getBytes());
			if (redisTemplate.hasKey(key.getBytes())) {
				byte[] in = (byte[]) redisTemplate.opsForValue().get(key.getBytes());
				t1 = (T) ObjectTranscoder.deserialize(in);
			}

		} catch (Exception e) {
			logger.error("getObj key error : " + e);
		}

		return t1;
	}

	// 查看key是否存在
	public boolean hasKey(String key) {
		if (StringUtils.isBlank(key))
			return false;
		return redisTemplate.hasKey(key.getBytes());
	}
}
