package com.echoes.service;

import com.echoes.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;

/**
 * <p>
 * 菜品及套餐分类 服务类
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-27
 */
public interface CategoryService extends IService<Category> {
    public Boolean remove(Long id);
}
