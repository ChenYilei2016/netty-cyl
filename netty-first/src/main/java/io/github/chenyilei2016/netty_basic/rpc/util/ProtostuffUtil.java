package io.github.chenyilei2016.netty_basic.rpc.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Netty在实际应用级开发中，有时候某些特定场景下会需要使用Java对象类型进行传输，但是如果使用Java本身序列化进行传输，那么对性能的损耗比较大。为此我们需要借助protostuff-core的工具包将对象以二进制形式传输并做编码解码处理。与直接使用protobuf二进制传输方式不同，这里不需要定义proto文件，而是需要实现对象类型编码解码器，用以传输自定义Java对象。
 *
 * protostuff 基于Google protobuf，但是提供了更多的功能和更简易的用法。
 * 其中，protostuff-runtime 实现了无需预编译对java bean进行protobuf序列化/反序列化的能力。
 * protostuff-runtime的局限是序列化前需预先传入schema，反序列化不负责对象的创建只负责复制，
 * 因而必须提供默认构造函数。此外，protostuff 还可以按照protobuf的配置序列化成json/yaml/xml等格式。
 * 在性能上，protostuff不输原生的protobuf，甚至有反超之势。
 *
 * 支持protostuff-compiler产生的消息
 * 支持现有的POJO对象
 * 支持现有的protoc产生的Java消息
 * 与各种移动平台的互操作能力（Android、Kindle、j2me）
 * 支持转码
 * #开发环境
 */
public class ProtostuffUtil {

    private static Objenesis objenesis = new ObjenesisStd();

    // 使用ConcurrentHashMap缓存Schema，提高性能
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>(); // 定义一个ConcurrentHashMap，用于缓存Schema

    private ProtostuffUtil() {

    }

    /**
     * 序列化(对象 -> 字节数组)
     *
     * @param obj 对象
     * @return 字节数组
     */
    public static <T> byte[] serialize(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化(字节数组 -> 对象)
     *
     * @param data
     * @param cls
     * @param <T>
     */
    public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            T message = objenesis.newInstance(cls);
            Schema<T> schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            cachedSchema.put(cls, schema);
        }
        return schema;
    }
}
