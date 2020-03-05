package com.gerrywen.seckill.third.rabbitmq.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: 这里需要使用单例模式来确保请求的队列的对象只有一个
 *
 * @author wenguoli
 * @date 2020/3/5 8:47
 */
public class RequestQueue {
    /**
     * 构造器私有化，这样就不能通过new来创建实例对象
     * 这里构造器私有化 这点跟枚举一样的，所以我们也可以通过枚举来实现单例模式，详见以后的博文
     */
    private RequestQueue() {
    }

    /**
     * 内存队列
     */
    private List<ArrayBlockingQueue<Request>> queues = new ArrayList<ArrayBlockingQueue<Request>>();
    /**
     * 标志位
     */
    private Map<Integer, Boolean> tagMap = new ConcurrentHashMap<>(1);

    /**
     * 私有的静态内部类来实现单例
     */
    private static class Singleton {
        private static RequestQueue queue;

        static {
            queue = new RequestQueue();
        }

        private static RequestQueue getInstance() {
            return queue;
        }
    }

    /**
     * 获取 RequestQueue 对象
     *
     * @return
     */
    public static RequestQueue getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 向容器中添加队列
     *
     * @param queue
     */
    public void add(ArrayBlockingQueue<Request> queue) {
        this.queues.add(queue);
    }

    /**
     * 获取队列大小
     *
     * @return
     */
    public int getSize() {
        return this.queues.size();
    }

    /**
     * 返回标志位
     *
     * @return
     */
    public Map<Integer, Boolean> getTagMap() {
        return this.tagMap;
    }
}
