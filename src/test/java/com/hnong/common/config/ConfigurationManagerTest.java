package com.hnong.common.config;

import com.hnong.common.config.Configuration;
import com.hnong.common.config.ConfigurationManager;
import junit.framework.Assert;
import org.junit.Test;

public class ConfigurationManagerTest {

    private static final ConfigurationManager manager = ConfigurationManager.getInstance();

    @Test
    public void getValueFromClasses() {
        Assert.assertEquals("ibatis.xml", manager.getConfigData().get("ibatis.config"));
    }

    @Test
    public void testForGetClassesValues() {
        Assert.assertEquals("4", manager.getConfigData().get("test2_redis_feed_client_pool_max_active"));
        Assert.assertEquals("4", manager.getConfigData().get("test2_redis_feed_client_pool_min_idle"));
        Assert.assertEquals("4", manager.getConfigData().get("test2_redis_feed_client_pool_max_idle"));
        Assert.assertEquals("4", manager.getConfigData().get("test2_redis_feed_client_pool_max_wait"));
    }

    //@Test
    public void getValueFromJar() {
        Assert.assertEquals("redisindex.dajie.com_dev", manager.getConfigData().get("test1_redis_feed_server_club_index_host"));
        Assert.assertEquals("8888_dev", manager.getConfigData().get("test1_redis_feed_server_club_index_port"));
        Assert.assertEquals("redisindex.dajie.com_dev", manager.getConfigData().get("test2_redis_feed_server_club_index_host"));
        Assert.assertEquals("8888_dev", manager.getConfigData().get("test2_redis_feed_server_club_index_port"));

        Assert.assertEquals("1_dev", manager.getConfigData().get("test1_redis_feed_client_pool_max_active"));
        Assert.assertEquals("1_dev", manager.getConfigData().get("test1_redis_feed_client_pool_min_idle"));
        Assert.assertEquals("1_dev", manager.getConfigData().get("test1_redis_feed_client_pool_max_idle"));
        Assert.assertEquals("1_dev", manager.getConfigData().get("test1_redis_feed_client_pool_max_wait"));
    }

    //@Test
    public void getOverride() {
        Configuration configuration = manager.getByFileName("dajie_test2");
        Assert.assertEquals("4", configuration.getString("test2_redis_feed_client_pool_max_active"));
        Assert.assertEquals("4", configuration.getString("test2_redis_feed_client_pool_min_idle"));
        Assert.assertEquals("4", configuration.getString("test2_redis_feed_client_pool_max_idle"));
        Assert.assertEquals("4", configuration.getString("test2_redis_feed_client_pool_max_wait"));

        configuration = manager.getByFileName("dajie_test1");
        Assert.assertEquals("1_dev", configuration.getString("test1_redis_feed_client_pool_max_active"));
        Assert.assertEquals("1_dev", configuration.getString("test1_redis_feed_client_pool_min_idle"));
        Assert.assertEquals("1_dev", configuration.getString("test1_redis_feed_client_pool_max_idle"));
        Assert.assertEquals("1_dev", configuration.getString("test1_redis_feed_client_pool_max_wait"));
    }

    //@Test
    public void getByFileNameTest() {
        Configuration configuration = manager.getByFileName("easou_cn_product_test.properties");
        Assert.assertNotNull(configuration);
    }

}
