package com.gerrywen.seckill.third.rabbitmq.util;

import com.gerrywen.seckill.third.rabbitmq.exception.MqException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.List;

/**
 * description: 校验工具类
 *
 * @author wenguoli
 * @date 2020/3/5 8:53
 */
public class ValidateUtils {

    /**
     * 校验对象<p/>
     * 根据对象中的@NotNull注解来判断
     *
     * @param object
     * @return
     */
    public static void validate(Object object) {
        if (StringUtils.isEmpty(object)) {
            throw new MqException("参数不能为空");
        }

        List<Field> fields = ClassUtils.getAllField(object.getClass());
        validateFields(fields, object);
    }

    /**
     * 校验属性中是否存在空的数据
     *
     * @param list
     * @param data
     * @return
     */
    public static void validateFields(List<Field> list, Object data) {
        if (CollectionUtils.isEmpty(list)) {
            throw new MqException("获取不到对象的属性");
        }
        for (Field field : list) {
            if (!field.isAnnotationPresent(NotNull.class)) {
                continue;
            }
            NotNull notNull = field.getAnnotation(NotNull.class);
            boolean isAccessable = field.isAccessible();
            try {
                field.setAccessible(Boolean.TRUE);
                Object value = field.get(data);
                if (StringUtils.isEmpty(value)) {
                    throw new MqException(notNull.message());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                field.setAccessible(isAccessable);
            }
        }
    }
}
