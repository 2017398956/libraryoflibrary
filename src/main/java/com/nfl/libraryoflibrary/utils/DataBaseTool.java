//package com.nfl.libraryoflibrary.utils;
//
//import com.lidroid.xutils.DbUtils;
//import com.lidroid.xutils.db.sqlite.Selector;
//import com.lidroid.xutils.db.sqlite.WhereBuilder;
//import com.lidroid.xutils.exception.DbException;
//import com.nfl.libraryoflibrary.constant.ApplicationContext;
//
//import java.util.List;
//
///**
// * Created by fuli.niu on 2016/9/2.
// */
//public class DataBaseTool<T extends Object> {
//
//    private DbUtils.DbUpgradeListener dbUpgradeListener = new DbUtils.DbUpgradeListener() {
//        @Override
//        public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
//
//        }
//    };
//    private DbUtils dbUtils;
//    private DataBaseTool(){
//    }
//    public DataBaseTool(String dbName , int dbVersion){
//        dbUtils = DbUtils.create(ApplicationContext.applicationContext,
//                dbName , // 数据库名称
//                dbVersion , // 数据库版本
//                dbUpgradeListener // 更新接口
//        );
//    }
//
////    public DataBaseTool(String dbName , int dbVersion){
////        dbUtils = DbUtils.create(ApplicationContext.applicationContext,
////                "message_center_" + ApplicationContext.USERNAME, // 数据库名称
////                1, // 数据库版本
////                dbUpgradeListener // 更新接口
////        );
////    }
//
//    /**
//     * 为每一个用户创建一个单独的数据库
//     */
//    private void getInstance() {
//        if (null == dbUtils) {
//            synchronized (DataBaseTool.class) {
//                if (null == dbUtils) {
//                    dbUtils = DbUtils.create(ApplicationContext.applicationContext,
//                            "message_center_" + ApplicationContext.USERNAME, // 数据库名称
//                            1, // 数据库版本
//                            dbUpgradeListener // 更新接口
//                    );
//                }
//            }
//        }
//    }
//
//    public void add(Object object) {
//        try {
//            dbUtils.createTableIfNotExist(object.getClass());
//            dbUtils.save(object);
//        } catch (DbException e) {
//            e.printStackTrace();
//        } finally {
//        }
//    }
//
//    public void add(List<T> list) {
//        if (null == list || list.size() < 1) {
//            return;
//        }
//        try {
//            dbUtils.createTableIfNotExist(((T) list.get(0)).getClass());
//            for (int i = list.size() - 1; i >= 0; i--) {
//                dbUtils.save((T) list.get(i));
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        } finally {
//        }
//    }
//
//    public void delete(Object object) {
//        try {
//            if (dbUtils.tableIsExist(object.getClass())) {
//                dbUtils.delete(object);
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        } finally {
//        }
//    }
//
//    public void deleteAll(Class clazz) {
//        try {
//            dbUtils.dropTable(clazz);
//        } catch (DbException e) {
//            e.printStackTrace();
//        } finally {
//        }
//    }
//
//    public List get(Class clazz, String Column, int pageSize, int pageIndex, boolean desc) {
//        List temp = null;
//        try {
//            dbUtils.findAll(clazz);
//            temp = dbUtils.findAll(Selector.from(clazz).orderBy(Column, desc).limit(pageSize).offset(pageSize * pageIndex));
//            // select top 2 * from (select * from test where id<5) m order by m.id desc
//        } catch (DbException e) {
//            e.printStackTrace();
//        } finally {
//            return temp;
//        }
//    }
//
//    public List get(Class clazz, String column01, String column02 ,int pageSize, int pageIndex, boolean desc) {
//        List temp = null;
//        try {
//            dbUtils.findAll(clazz);
//            temp = dbUtils.findAll(Selector.from(clazz).orderBy(column02, desc).limit(pageSize).offset(pageSize * pageIndex));
//            // select top 2 * from (select * from test where id<5) m order by m.id desc
//        } catch (DbException e) {
//            e.printStackTrace();
//        } finally {
//            return temp;
//        }
//    }
//
////    public List get(Class clazz, String group, String orderParameter , int pageSize, int pageIndex, boolean desc) {
////        List temp = null;
////        try {
////            temp = dbUtils.findAll(
////                    Selector.from(clazz)
////                            .orderBy(group)
////                            .limit(pageSize)
////                            .offset(pageSize * pageIndex)
////            );
////            // select top 2 * from (select * from test where id<5) m order by m.id desc
////        } catch (DbException e) {
////            e.printStackTrace();
////        } finally {
////            return temp;
////        }
////    }
//
//    public void update(T t){
//        try {
//            dbUtils.update(t , "hasReaded");
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public List search(Class clazz, String Column, int pageSize, int pageIndex, boolean desc , String column2 , String column3 ,String str){
//        List temp = null;
//        try {
//            temp = dbUtils.findAll(Selector.from(clazz).orderBy(Column, desc)
//                    .limit(pageSize)
//                    .offset(pageSize * pageIndex).where(column2 , "LIKE" , "%" + str + "%").or(column3 , "LIKE" , "%" + str + "%").or("messagetype" , "LIKE" , "%" + str + "%"));
//            } catch (DbException e) {
//            e.printStackTrace();
//        }finally {
//            return temp ;
//        }
//    }
//
//    public void close(){
//        if(null != dbUtils){
//            dbUtils.close();
//        }
//    }
//}
