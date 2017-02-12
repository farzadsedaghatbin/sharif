package com.isc.npsd.sharif.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isc.npsd.sharif.model.entities.schemaobjects.trx.TXRList;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by farzad on 02/8/16.
 */
public class RedisUtil {

    static JedisPool pool;
    static Jedis jedis;

    public static Pipeline getPipeline() {
        return jedis.pipelined();
    }

    static {
//        try {
//            String vcap_services = System.getenv("VCAP_SERVICES");
//            if (vcap_services != null && vcap_services.length() > 0) {
        // parsing rediscloud credentials
//                JsonRootNode root = new JdomParser().parse(vcap_services);
//                JsonNode rediscloudNode = root.getNode("rediscloud");
//                JsonNode credentials = rediscloudNode.getNode(0).getNode("credentials");

        pool = new JedisPool(new JedisPoolConfig(), "redis-12950.c8.us-east-1-3.ec2.cloud.redislabs.com", 12950,
                Protocol.DEFAULT_TIMEOUT, "E16SIfkTOcI0ftDB");
        jedis = pool.getResource();
//            }
//        } catch (InvalidSyntaxException ex) {
//            // vcap_services could not be parsed.
//        }
    }

    public static void addItem(String key, String value) {
        try {


            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static TXRList.TXR getItem(String key) {

        try {
            return new ObjectMapper().readValue(jedis.get(key), TXRList.TXR.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TXRList.TXR getExpressionItem(String pattern) {

        try {
            Set<String> sets = jedis.keys(pattern);
            if (sets == null || sets.size() == 0)
                return null;
            return new ObjectMapper().readValue(jedis.get(sets.iterator().next()), TXRList.TXR.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Set<TXRList.TXR> getExpressionItems(String pattern) {

        try {
            Set<String> sets = jedis.keys(pattern);
            Set<TXRList.TXR> txrs = new HashSet<>();
            for (String set : sets) {
                txrs.add(new ObjectMapper().readValue(jedis.get(set), TXRList.TXR.class));
            }
            return txrs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void removeItem(String key) {

        jedis.del(key);
    }
//    public static void main(String[] args) {
////        test();
//        Set<String> sets=jedis.keys("3*");
//        for (String set : sets) {
//            jedis.del(
//set
//            );
//        }
//
////        jedis.lrem()
//        String value = jedis.get("omi,");
//// return the instance to the pool when you're done
//        pool.returnResource(jedis);
//    }
}
