package com.baidu.buycar;

import com.baidu.buycar.MyDao.DaoMaster;
import com.baidu.buycar.MyDao.DaoSession;
import com.baidu.buycar.MyDao.UserDao;

import org.greenrobot.greendao.DbUtils;

import java.util.List;

public class DaoUtils {

    private static DaoUtils sDaoUtils;
    private UserDao myDao;

    private DaoUtils(){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(BaseApp.getApp(), "user.db");
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        myDao = daoSession.getUserDao();
    }

    public static DaoUtils getDbUtils() {
        if (sDaoUtils == null) {
            synchronized (DbUtils.class) {
                if (sDaoUtils == null) {
                    sDaoUtils = new DaoUtils();
                }
            }
        }
        return sDaoUtils;
    }

    public long insert(User bean) {
        if (!has(bean)) {
            return myDao.insertOrReplace(bean);
        }
        return -1;
    }

    public List<User> query() {
        return myDao.queryBuilder().list();
    }

    public void deleteAll() {
        myDao.deleteAll();
    }

    public boolean delete(User bean) {
        if (has(bean)) {
            myDao.delete(bean);
            return true;
        }
        return false;
    }

    public boolean has(User bean) {
        List<User> list = myDao.queryBuilder().where(UserDao.Properties.Name.eq(bean.getName())).list();

        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }
}
