package com.fengsheng.fastdfs.config.datasource;

public class DBContextHolder {

    private static final ThreadLocal<DBTypeEnum> contextHolder = new ThreadLocal<>();

//    private static final AtomicInteger counter = new AtomicInteger(-1);

    public static void set(DBTypeEnum dbType) {
        contextHolder.set(dbType);
    }

    public static DBTypeEnum get() {
        return contextHolder.get();
    }


    /**
     * 清除当前数据源
     */
    public static void clear() {
        contextHolder.remove();
    }

    public static void master() {
        set(DBTypeEnum.MASTER);
        System.out.println("切换到master");
    }

    public static void slave() {
        set(DBTypeEnum.SLAVE);
        System.out.println("切换到slave1");
        //  轮询
//        int index = counter.getAndIncrement() % 2;
//        if (counter.get() > 9999) {
//            counter.set(-1);
//        }
//        if (index == 0) {
//            set(DBTypeEnum.SLAVE1);
//            System.out.println("切换到slave1");
//        } else {
//            set(DBTypeEnum.SLAVE2);
//            System.out.println("切换到slave2");
//        }
    }
}
