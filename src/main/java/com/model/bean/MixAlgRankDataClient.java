package com.model.bean;

import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;
import com.model.Document;
import com.util.LogicDateUtil;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


public class MixAlgRankDataClient {
    private static final Logger log = Logger.getLogger(MixAlgRankDataClient.class);


    protected static ReentrantLock lockPool = new ReentrantLock();
    protected static ReentrantLock lockJedis = new ReentrantLock();


    private static ShardedJedisPool jedisPool_master = null;
    private static ShardedJedisPool jedisPool_slave = null;

    /**
     * @throws
     * @Title: redisPoolInit
     * @Description: 初始化jedisPool
     * @author liu_yi
     */
    private static void redisPoolInit() {
        // 当前锁是否已经锁住?if锁住了，do nothing; else continue
        assert !lockPool.isHeldByCurrentThread();
//
//	#最大分配的对象数
//			redis_pool_max_active=3000
//	#最大能够保持idel状态的对象数
//			redis_pool_max_idle=3000
//	#当池内没有返回对象时，最大等待时间
//			redis_pool_max_wait=100000
        try {
            String[] redis_master_hosts = "10.90.1.47#10.90.1.48#10.90.1.59#10.90.9.56#10.90.9.57#10.90.9.58#10.90.9.59#10.90.9.60".split("#");
            String[] redis_master_ports = "6379".split("#");
            String[] redis_slave_hosts = "10.90.1.47#10.90.1.48#10.90.1.59#10.90.9.56#10.90.9.57#10.90.9.58#10.90.9.59#10.90.9.60".split("#");
            String[] redis_slave_ports = "6380".split("#");

            List<JedisShardInfo> master_shards = new ArrayList<JedisShardInfo>();
            master_shards = buildJedisShard(redis_master_hosts, redis_master_ports);

            List<JedisShardInfo> slave_shards = new ArrayList<JedisShardInfo>();
            slave_shards = buildJedisShard(redis_slave_hosts, redis_slave_ports);

            JedisPoolConfig config = new JedisPoolConfig();
            // 是否启用后进先出, 默认true
            config.setLifo(true);

            // 最大空闲连接数
            config.setMaxIdle(10);

            // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted), 如果超时就抛异常,小于零:阻塞不确定的时间
            config.setMaxWaitMillis(2000);

            // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
            config.setMinEvictableIdleTimeMillis(1800000);

            // 最小空闲连接数, 默认0
            config.setMinIdle(0);

            // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
            config.setNumTestsPerEvictionRun(3);

            // 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
            config.setSoftMinEvictableIdleTimeMillis(1800000);

            // 在获取连接的时候检查有效性, 默认false
            config.setTestOnBorrow(false);
            config.setMaxTotal(50000);

            // 在空闲时检查有效性, 默认false
            config.setTestWhileIdle(false);

            log.info("build jedisPool_master...");
            jedisPool_master = new ShardedJedisPool(config, master_shards, Hashing.MURMUR_HASH);

            log.info("build jedisPool_slave...");
            jedisPool_slave = new ShardedJedisPool(config, slave_shards, Hashing.MURMUR_HASH);
        } catch (Exception ex) {
            log.error("redisPoolInit failed#", ex);
        }
    }

    public static void close() {
        if (jedisPool_master != null) {
            jedisPool_master.close();
        }
        if (jedisPool_slave != null) {
            jedisPool_slave.close();
        }
    }

    /**
     * @param hosts hosts列表
     * @param ports port列表
     * @return
     * @throws
     * @Title: buildJedisShard
     * @Description: 根据参数列表，构建shardInfo List
     * @author liu_yi
     */
    public static List<JedisShardInfo> buildJedisShard(String[] hosts, String[] ports) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();

        // 这是个固定名字，否则主从不一致
        String shardName = "FIXEDNAME";

        for (int i = 0; i != hosts.length; i++) {
            String tempHost = hosts[i];
            for (int j = 0; j != ports.length; j++) {
                String tempPort = ports[j];
                int tempPortsNum = Integer.valueOf(tempPort);

                String tempJedisShardNodeName = shardName + "_" + tempHost + "_addFixedPort" + j;
                JedisShardInfo tempJedisShardNode = new JedisShardInfo(tempHost, tempPortsNum, tempJedisShardNodeName);
                shards.add(tempJedisShardNode);
            }
        }

        return shards;
    }

    public static ShardedJedisPool getJedisPoolMaster() {
        if (jedisPool_master == null) {
            redisPoolInit();
        }

        return jedisPool_master;
    }

    public static ShardedJedisPool getJedisPoolSlave() {
        if (jedisPool_slave == null) {
            redisPoolInit();
        }

        return jedisPool_slave;
    }

    public static ShardedJedis getShardedJedis(String masterORslave) {
        assert !lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        ShardedJedisPool sjp = null;
        if ("master".equals(masterORslave)) {
            sjp = getJedisPoolMaster();
        }

        if ("slave".equals(masterORslave)) {
            sjp = getJedisPoolSlave();
        }

        if (sjp == null) {
            redisPoolInit();
        }

        // 再检测一次看是否为空(可能初始化失败)
        if (sjp == null) {
            log.error("can not get jedis instance, init again..");
            redisPoolInit();
        }

        // 还是失败了
        if (sjp == null) {
            log.error("can not get jedis instance");
            return null;
        }

        ShardedJedis shardJedis = null;

        try {
            shardJedis = sjp.getResource();
        } catch (Exception e) {
            log.error("got an exception:", e);

        } finally {
            returnResource(shardJedis, sjp);
            lockJedis.unlock();
        }

        return shardJedis;
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    public static void returnResource(final ShardedJedis jedis, ShardedJedisPool sjp) {
    /*if (jedis != null && sjp !=null) {  
        sjp.returnResourceObject(jedis);
    }*/
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * @return
     * @throws
     * @Title: getJedisClient
     * @Description: 获得jedis客户端
     * @author liu_yi
     */
    public synchronized static ShardedJedis getJedisClient(String masterORslave) {
        ShardedJedisPool sjp = null;
        if ("master".equals(masterORslave)) {
            sjp = getJedisPoolMaster();
        }

        if ("slave".equals(masterORslave)) {
            sjp = getJedisPoolSlave();
        }

        if (sjp == null) {
            redisPoolInit();
        }

        // 再检测一次看是否为空(可能初始化失败)
        if (sjp == null) {
            log.error("can not get jedis instance, init again..");
            redisPoolInit();
        }

        // 还是失败了
        if (sjp == null) {
            log.error("can not get jedis instance");
            return null;
        }

        ShardedJedis shardJedis = null;

        try {
            shardJedis = sjp.getResource();
        } catch (Exception e) {
            log.error("got an exception:", e);
        }

        return shardJedis;
    }

    public static byte[] getDataFromSlave(String key) throws Exception {
        ShardedJedisPool sjp = null;
        synchronized (MixAlgRankDataClient.class) {
            sjp = getJedisPoolSlave();

        }
        ShardedJedis shardJedis = null;

        try {
            byte[] k = key.getBytes();
            shardJedis = sjp.getResource();

            byte[] rt = shardJedis.get(k);
            if (rt == null) {
                return null;
            }
            return CompressUtil.uncompress(rt);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {

            returnResource(shardJedis, sjp);


        }

    }

    public static byte[] getDataFromMaster(String key) throws Exception {
        ShardedJedisPool sjp = null;
        synchronized (MixAlgRankDataClient.class) {
            sjp = getJedisPoolMaster();

        }

        ShardedJedis shardJedis = null;

        try {
            byte[] k = key.getBytes();
            shardJedis = sjp.getResource();

            byte[] rt = shardJedis.get(k);

            if (rt == null) {
                return null;
            }
            return CompressUtil.uncompress(rt);


        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {

            returnResource(shardJedis, sjp);


        }


    }

    public static Long ttlSlave(String key) {
        ShardedJedisPool sjp = null;
        synchronized (MixAlgRankDataClient.class) {
            sjp = getJedisPoolSlave();

        }
        ShardedJedis shardJedis = null;

        try {
            byte[] k = key.getBytes();
            shardJedis = sjp.getResource();

            return shardJedis.ttl(k);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            returnResource(shardJedis, sjp);
        }
        return null;
    }

    public static Long ttl(String key) {
        ShardedJedisPool sjp = null;
        synchronized (MixAlgRankDataClient.class) {
            sjp = getJedisPoolMaster();

        }
        ShardedJedis shardJedis = null;

        try {
            byte[] k = key.getBytes();
            shardJedis = sjp.getResource();

            return shardJedis.ttl(k);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            returnResource(shardJedis, sjp);
        }
        return null;
    }

    public static boolean setnx(String key, byte[] data, int expire) throws IOException {
        ShardedJedisPool sjp = null;
    /*HashFunction hf =com.google.common.hash.Hashing.murmur3_32();
    int r=hf.newHasher().putString(key, Charsets.UTF_8).hash().asInt();
	if(r%2==0){
		outputrt=CompressUtil.compress(outputrt);
	}*/
        synchronized (MixAlgRankDataClient.class) {
            sjp = getJedisPoolMaster();

        }
        ShardedJedis shardJedis = null;

        try {
            data = CompressUtil.compress(data);
            shardJedis = sjp.getResource();
            shardJedis.setnx(key.getBytes(), data);
            shardJedis.expire(key.getBytes(), expire);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            returnResource(shardJedis, sjp);
        }
        return false;
    }

    public static boolean exists(String key) throws IOException {
        ShardedJedisPool sjp = null;
	/*HashFunction hf =com.google.common.hash.Hashing.murmur3_32();
	int r=hf.newHasher().putString(key, Charsets.UTF_8).hash().asInt();
	if(r%2==0){
		outputrt=CompressUtil.compress(outputrt);
	}*/
        synchronized (MixAlgRankDataClient.class) {
            sjp = getJedisPoolMaster();

        }
        ShardedJedis shardJedis = null;

        try {
            shardJedis = sjp.getResource();
            return shardJedis.exists(key.getBytes());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            returnResource(shardJedis, sjp);
        }
        return false;
    }

    public static void outputData(String key, byte[] outputrt, int expire) throws IOException {
        ShardedJedisPool sjp = null;
	/*HashFunction hf =com.google.common.hash.Hashing.murmur3_32();
	int r=hf.newHasher().putString(key, Charsets.UTF_8).hash().asInt();
	if(r%2==0){*/
        outputrt = CompressUtil.compress(outputrt);
        //}
        synchronized (MixAlgRankDataClient.class) {
            sjp = getJedisPoolMaster();

        }
        ShardedJedis shardJedis = null;

        try {
            byte[] cid = key.getBytes();
            shardJedis = sjp.getResource();
            shardJedis.set(cid, outputrt);
            shardJedis.expire(cid, expire);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            returnResource(shardJedis, sjp);
        }
    }

    public static void deleteData(String key) {
        ShardedJedisPool sjp = null;
        synchronized (MixAlgRankDataClient.class) {
            sjp = getJedisPoolMaster();

        }
        ShardedJedis shardJedis = null;

        try {
            shardJedis = sjp.getResource();
            shardJedis.del(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {

            returnResource(shardJedis, sjp);


        }

    }

//    public static void main(String[] args) throws Exception {
//        String uid = "A0000059A2230D";
//
//        byte[] s = getDataFromSlave(uid);
//        if (s == null) {
//            return;
//        }
//        try {
//            Opb.MixRankBeanList b = Opb.MixRankBeanList.parseFrom(s);
//            int count = 0;
//            int count1 = 0;
//            for (Opb.MixRankBean bb : b.getBeansList()) {
//                if (MixAlgRankDataClient.exists("docId_" + bb.getId())) {
//                    byte[] doc = MixAlgRankDataClient.getDataFromSlave("docId_" + bb.getId());
//                    try {
//                        Opb.Doc d = Opb.Doc.parseFrom(doc);
//                        System.out.println(d.getDocId());
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    count++;
//                } else {
//                    System.out.println("id=" + bb.getId());
//                }
//                count1++;
//            }
//            System.out.println(count1 + "==" + count);
//
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    public static List<Document> getMixRankList(String uid) {
        List<Document> resultDoc = Lists.newArrayList();
        try {
            byte[] data = MixAlgRankDataClient.getDataFromSlave(uid);
            if (data == null) {
                return resultDoc;
            }
            Opb.MixRankBeanList b = Opb.MixRankBeanList.parseFrom(data);
            Document newDoc;
            for (Opb.MixRankBean bb : b.getBeansList()) {
                if (MixAlgRankDataClient.exists("docId_" + bb.getId())) {
                    byte[] doc = MixAlgRankDataClient.getDataFromSlave("docId_" + bb.getId());
                    String why = bb.getW();
                    try {
                        Opb.Doc d = Opb.Doc.parseFrom(doc);
                        String dateStr = d.getDate();
                        String docid = d.getDocId();
                        String hotbooast = d.getHotBoost();
                        String others = d.getOthers();
                        String readablefeatures = d.getReadableFeatures();
                        String simid = d.getSimId();
                        String title = d.getTitle();
                        String doctype = d.getDocType();
                        newDoc = new Document();
                        newDoc.setDocId(docid);
                        newDoc.setSimId(simid);
                        newDoc.setTitle(title);
                        newDoc.setWhy(why);
                        newDoc.setHotBoost(hotbooast);
                        newDoc.setOthers(others);
                        newDoc.setDoc_type(doctype);
                        newDoc.setReadableFeatures(readablefeatures);
                        newDoc.setDate(LogicDateUtil.strToDate(dateStr));
                        resultDoc.add(newDoc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return resultDoc;
        } catch (InvalidProtocolBufferException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            //   MixAlgRankDataClient.close();
            return resultDoc;
        }
    }
}
