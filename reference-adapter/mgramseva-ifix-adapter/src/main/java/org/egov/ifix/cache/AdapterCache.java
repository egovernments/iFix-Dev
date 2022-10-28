package org.egov.ifix.cache;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
	public class AdapterCache<T> {
		private RedisTemplate<String, T> redisTemplate;
		private HashOperations<String, Object, T> hashOperation;
		private ListOperations<String, T> listOperation;
		private ValueOperations<String, T> valueOperations;

		@Autowired
		AdapterCache(RedisTemplate<String,T> redisTemplate){
	        this.redisTemplate = redisTemplate;
	        this.hashOperation = redisTemplate.opsForHash();
	        this.listOperation = redisTemplate.opsForList();
	        this.valueOperations = redisTemplate.opsForValue();
	        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
	     }

		public void putMap(String redisKey, Object key, T data) {
			hashOperation.put(redisKey, key, data);
		}

		public T getMapAsSingleEntry(String redisKey, Object key) {
			return hashOperation.get(redisKey, key);
		}

		public Map<Object, T> getMapAsAll(String redisKey) {
			return hashOperation.entries(redisKey);
		}

		public void putValue(String key, T value) {
				valueOperations.set(key, value);			 
		}

		public void putValueWithExpireTime(String key, T value, long timeout, TimeUnit unit) {
			valueOperations.set(key, value, timeout, unit);
		}

		public T getValue(String key) {
			return valueOperations.get(key);
		}

		public void setExpire(String key, long timeout, TimeUnit unit) {
			redisTemplate.expire(key, timeout, unit);
		}
	}

