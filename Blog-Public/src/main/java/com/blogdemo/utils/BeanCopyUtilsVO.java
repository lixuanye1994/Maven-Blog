package com.blogdemo.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 封装VO类的拷贝过程
 * 参数:传入源数据,VO类（过滤条件）（目标对象）
 * 操作：1.实例化VO类
 *      2.源数据拷贝到1.中
 *      3.返回结果
 */
public class BeanCopyUtilsVO {
    private BeanCopyUtilsVO(){

    }

    //因为不确定传入的参数属性,使用泛型,通过传入的参数，确定类型,
    // <V>声明泛型定义
    public static <V> V copyBean(Object scores,Class<V> clazz){
        //创建目标对象
        V result;
        try {
            //使用反射,实例化传入的类
            /**
             * 反射实例化类 class.newInstance()弃用,
             * 用getDeclaredConstructor().newInstance()
             */
            result =  clazz.getDeclaredConstructor().newInstance();
            //实现属性拷贝 (原数据属性集合,)
            BeanUtils.copyProperties(scores,result);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return result;
    }



    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o,clazz))
                .collect(Collectors.toList());

    }


//    测试先行
//    public static void main(String[] args) {
//        Article article = new Article();
//        article.setId(1L);
//        article.setViewCount(999L);
//
//        HotArticleListVO hotArticleList = copyBean(article, HotArticleListVO.class);
//
//        System.out.println(hotArticleList.getId()+hotArticleList.getViewCount());
//    }

}
