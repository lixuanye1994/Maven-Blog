package com.blogdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogdemo.constants.SystemConstants;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.vo.LinkVO;
import com.blogdemo.mapper.LinkDao;
import com.blogdemo.domain.entity.Link;
import com.blogdemo.service.LinkService;
import com.blogdemo.utils.BeanCopyUtilsVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author vmlee
 * @since 2022-12-13 11:48:54
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkDao, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的友链
        LambdaQueryWrapper<Link> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        //接收查询结果放到集合中
        List<Link> links = list(lambdaQueryWrapper);
        //vo类做筛选
        List<LinkVO> linkVos = BeanCopyUtilsVO.copyBeanList(links, LinkVO.class);
        return ResponseResult.okResult(linkVos);
    }
}

