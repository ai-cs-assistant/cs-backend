package com.olga.aics.util;

import cn.hutool.core.lang.Snowflake;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeIdGenerator {
    private final Snowflake snowflake;
    private static final SnowflakeIdGenerator INSTANCE = new SnowflakeIdGenerator();
    
    public SnowflakeIdGenerator() {
        // 參數分別是：工作機器ID(0~31)，數據中心ID(0~31)
        // 這裡使用固定的值，在實際生產環境中，應該根據部署環境來設定
        this.snowflake = new Snowflake(1, 1);
    }
    
    /**
     * 生成下一個 ID
     * @return 生成的 Snowflake ID
     */
    public Long nextId() {
        return snowflake.nextId();
    }

    /**
     * 獲取單例實例（用於非 Spring 環境）
     * @return SnowflakeIdGenerator 實例
     */
    public static SnowflakeIdGenerator getInstance() {
        return INSTANCE;
    }

    /**
     * 生成一個新的 Snowflake ID（靜態方法）
     * @return 生成的 Snowflake ID
     */
    public static Long generateId() {
        return INSTANCE.nextId();
    }

    /**
     * 批量生成指定數量的 ID（靜態方法）
     * @param count 需要生成的 ID 數量
     * @return 生成的 ID 陣列
     */
    public static Long[] generateIds(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("生成數量必須大於 0");
        }
        Long[] ids = new Long[count];
        for (int i = 0; i < count; i++) {
            ids[i] = INSTANCE.nextId();
        }
        return ids;
    }
} 