package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Joblevel;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
public interface IJoblevelService extends IService<Joblevel> {
    /**
     * 添加职称
     * @param joblevel
     */
    void addJobLevel(Joblevel joblevel);

    /**
     * 更新职称
     * @param joblevel
     */
    void updateJobLevel(Joblevel joblevel);

    /**
     * 删除职称
     * @param id
     */
    void deleteJobLevel(Integer id);

    /**
     * 批量删除
     * @param ids
     */
    void deleteJobLevels(Integer[] ids);
}
