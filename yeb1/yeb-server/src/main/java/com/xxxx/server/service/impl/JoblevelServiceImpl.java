package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.enums.LevelEnum;
import com.xxxx.server.mapper.JoblevelMapper;
import com.xxxx.server.pojo.Joblevel;
import com.xxxx.server.service.IJoblevelService;
import com.xxxx.server.utils.AssertUtil;
import com.xxxx.server.utils.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
@Service
public class JoblevelServiceImpl extends ServiceImpl<JoblevelMapper, Joblevel> implements IJoblevelService {
    @Resource
    private JoblevelMapper joblevelMapper;

    @Override
    public void addJobLevel(Joblevel joblevel) {
        checkJobLevelData(joblevel);

        //判断职称名称是否可用   两种方法都能用，同等效果
        //Joblevel dbJobLevel = query().eq("name", joblevel.getName()).one();
        QueryWrapper<Joblevel> wrapper = new QueryWrapper<>();
        wrapper.eq("name", joblevel.getName());
        Joblevel dbJobLevel = joblevelMapper.selectOne(wrapper);
        AssertUtil.isTrue(dbJobLevel != null, "职称已经存在，不能添加重复职称");

        //判断职称等级是否在自定义枚举类中
        AssertUtil.isTrue(!EnumUtils.isExist(LevelEnum.values(), joblevel.getTitleLevel()), "请输入规范的职称等级");

        joblevel.setCreateDate(LocalDateTime.now());
        joblevel.setEnabled(true);

        //增加操作
        AssertUtil.isTrue(joblevelMapper.insert(joblevel) < 1, "职称添加失败，请重试");
    }

    private void checkJobLevelData(Joblevel joblevel) {
        AssertUtil.isTrue(StringUtils.isBlank(joblevel.getName()), "请输入职称名称");
        AssertUtil.isTrue(joblevel.getTitleLevel() == null, "请输入职称等级");
    }

    @Override
    public void updateJobLevel(Joblevel joblevel) {
        AssertUtil.isTrue(joblevel.getId() == null, "请输入需要更新的职称编号");
        Joblevel dbJobLevel = joblevelMapper.selectById(joblevel.getId());
        AssertUtil.isTrue(dbJobLevel == null, "需要更新职称的编号不存在！");
        AssertUtil.isTrue(joblevel == null, "数据异常");
        AssertUtil.isTrue(!dbJobLevel.getId().equals(joblevel.getId()), "数据异常");

        checkJobLevelData(joblevel);

        //判断职称等级是否在自定义枚举类中
        AssertUtil.isTrue(!EnumUtils.isExist(LevelEnum.values(), joblevel.getTitleLevel()), "请输入规范的职称等级");

        AssertUtil.isTrue(joblevelMapper.updateById(joblevel) < 1, "职称更新失败");
    }

    @Override
    public void deleteJobLevel(Integer id) {
        AssertUtil.isTrue(id == null, "职称编号不能为空");
        AssertUtil.isTrue(joblevelMapper.selectById(id) == null, "请输入正确的职称编号");
        AssertUtil.isTrue(joblevelMapper.deleteById(id) < 1, "删除职称失败");
    }

    @Override
    public void deleteJobLevels(Integer[] ids) {
        AssertUtil.isTrue(ids == null || ids.length < 0, "没有需要删除的数据");
        AssertUtil.isTrue(joblevelMapper.deleteBatchIds(Arrays.asList(ids)) != ids.length, "删除数据不匹配，请重试");
    }
}
