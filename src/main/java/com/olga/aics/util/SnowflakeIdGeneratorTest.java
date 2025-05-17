package com.olga.aics.util;

/**
 * Snowflake ID 生成器測試類
 * 可以直接執行此類來測試 ID 生成功能
 */
public class SnowflakeIdGeneratorTest {
    
    /**
     * 主方法，可以直接執行此類來生成 ID
     * 使用方式：
     * 1. 直接執行此類
     * 2. 或使用命令列：java -cp <classpath> com.olga.aics.util.SnowflakeIdGeneratorTest [數量]
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                int count = Integer.parseInt(args[0]);
                Long[] ids = SnowflakeIdGenerator.generateIds(count);
                System.out.println("生成的 " + count + " 個 Snowflake ID：");
                for (int i = 0; i < ids.length; i++) {
                    System.out.println((i + 1) + ". " + ids[i]);
                }
            } catch (NumberFormatException e) {
                System.out.println("請輸入有效的數字！");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } else {
            // 預設生成一個 ID
            Long id = SnowflakeIdGenerator.generateId();
            System.out.println("生成的 Snowflake ID：" + id);
            System.out.println("\n使用方式：");
            System.out.println("1. 直接執行此類生成一個 ID");
            System.out.println("2. 執行時帶入參數指定生成數量，例如：java SnowflakeIdGeneratorTest 5");
        }
    }
} 