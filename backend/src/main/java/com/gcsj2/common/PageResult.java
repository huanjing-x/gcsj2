package com.gcsj2.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * 分页统一返回结构
 */
@Data
public class PageResult<T> {

    /** 总条数 */
    private long total;

    /** 当前页数据 */
    private List<T> records;

    /** 当前页码 */
    private long current;

    /** 每页条数 */
    private long size;

    public PageResult() {}

    public PageResult(long total, List<T> records, long current, long size) {
        this.total = total;
        this.records = records;
        this.current = current;
        this.size = size;
    }

    /**
     * 从 MyBatis-Plus 分页对象转换
     */
    public static <T> PageResult<T> from(IPage<T> page) {
        return new PageResult<>(page.getTotal(), page.getRecords(), page.getCurrent(), page.getSize());
    }
}
