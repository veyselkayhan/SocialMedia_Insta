package com.veysel.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/*
    Bu sınıf hibernate.cfg.cml dosyasını kullanarak connection olusutrmak için kullanılacaktır.
    Istenirse hibarnate dosyasının konumu burada belirtilerek farklı lokasyonlardan okunabilir.

 */
public class HibernateUtility {
    private static final SessionFactory SESSION;

    static {
        try {
//            SESSION = new Configuration().configure("c:\\config\\hibernate.cfg.xml").buildSessionFactory();
        SESSION = new Configuration().configure().buildSessionFactory();
        } catch (Exception exception) {
            System.out.println("Hibarnate başlatılırken hata olustu :" + exception);
            throw new ExceptionInInitializerError(exception);
        }

    }
    public static SessionFactory getSessionFactory(){
        return SESSION;
    }
}

