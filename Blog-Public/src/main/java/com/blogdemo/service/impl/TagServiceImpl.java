package com.blogdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.mapper.TagDao;
import com.blogdemo.domain.entity.Tag;
import com.blogdemo.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author vmlee
 * @since 2023-04-13 10:44:17
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagDao, Tag> implements TagService {

}

