package com.deng.recipes.persit;

import com.deng.recipes.utils.ConfigManager;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by hcdeng on 2017/4/27.
 */
public class PersistUtils {

    private static String dbPath;//db name or dir name

    private static Persistor persistor;

    private static final PersistUtils INSTANCE = new PersistUtils();

    private static final Executor executor = Executors.newFixedThreadPool(
            Integer.parseInt(ConfigManager.instance().getProperty("persistor.threads", "4")));

    private PersistUtils() {
        String pname = ConfigManager.instance().getProperty("persistor");
        dbPath = ConfigManager.instance().getProperty("db.path");

        try {
            Class c = Class.forName(pname);
            persistor = (Persistor) c.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param o        需要保存的对象
     * @param typeName 如果是文件存储-->类型子目录名， DB-->table名，ES-->typeName
     * @return 保存结果
     */
    public static void save(Object o, String typeName) {
        executor.execute(() -> persistor.save(o, dbPath, typeName));
    }

    /**
     * @param collection 需要保存的对象集合
     * @param typeName   如果是文件存储-->类型子目录名， DB-->table名，ES-->typeName
     * @return 保存结果
     */
    public static void saveAll(List<?> collection, String typeName) {
        executor.execute(() -> persistor.saveAll(collection, dbPath, typeName));
    }

    /**
     * @param o 需要保存的对象
     * @return 保存结果
     */
    public static void save(Object o) {
        executor.execute(() -> persistor.save(o, dbPath));
    }

    /**
     * @param collection 需要保存的对象集合
     * @return 保存结果
     */
    public static void saveAll(List<?> collection) {
        executor.execute(() -> persistor.saveAll(collection, dbPath));
    }
}