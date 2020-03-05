package com.gerrywen.seckill.third.rabbitmq.util;

import com.gerrywen.seckill.third.rabbitmq.exception.MqException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * description:  操作类的工具方法
 *
 * @author wenguoli
 * @date 2020/3/5 8:52
 */
public class ClassUtils {
    /**
     * 获取类中所有的field
     *
     * @param clazz
     * @return
     */
    public static List<Field> getAllField(Class clazz) {
        if (StringUtils.isEmpty(clazz)) {
            throw new MqException("参数不能为空");
        }
        Field[] fields = FieldUtils.getAllFields(clazz);
        if (fields != null && fields.length > 0) {
            return Arrays.asList(fields);
        }
        return new ArrayList<>();
    }
}
