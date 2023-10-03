package com.veysel.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MyFactoryRepository <T,ID> implements ICrud <T, ID>{


    private Session session;

    private Transaction transaction;

    private CriteriaBuilder criteriaBuilder;

    private EntityManager entityManager;

    T t;


    public MyFactoryRepository(T entity) {
        entityManager=HibernateUtility.getSessionFactory().createEntityManager();
        criteriaBuilder=entityManager.getCriteriaBuilder();
        this.t=entity;
    }

    private void openSession(){
        session= HibernateUtility.getSessionFactory().openSession();
        transaction=session.beginTransaction();
    }

    private void closeSession(){
        transaction.commit();
        session.close();
    }


    @Override
    public T save(T entity) {
        openSession();
        session.save(entity);
        closeSession();
        return entity;
    }

    @Override
    public Iterable saveAll(Iterable<T> entites) {
        openSession();
        entites.forEach(t->{
            session.save(t);
        });
        closeSession();
        return entites;
    }

    @Override
    public void delete(T entity) {
        openSession();
        session.delete(entity);
        closeSession();

    }

    @Override
    public void deleteById(ID id) {
        CriteriaQuery<T>criteria=(CriteriaQuery<T>) criteriaBuilder.createQuery(t.getClass());
        Root<T>root=(Root<T>) criteria.from(t.getClass());
        criteria.select(root);
        criteria.where(criteriaBuilder.equal(root.get("id"),id));
        T result=entityManager.createQuery(criteria).getSingleResult();
        session.delete(result);
        closeSession();

    }

    /*
    Select * FRom tbl_ Where id=?
    SQL sorgusu
     */
    @Override
    public Optional<T> findById(ID id) {//optional nullcheck yapmak için kullanılır.

    CriteriaQuery<T>criteria = (CriteriaQuery<T>) criteriaBuilder.createQuery(t.getClass());
    Root<T>root = (Root<T>) criteria.from(t.getClass());
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get("id"),id));
    T result=entityManager.createQuery(criteria).getSingleResult();
    return  Optional.ofNullable(result);
//    List<T>result=entityManager.createQuery(criteria).getResultList();
//    if(result.isEmpty()){
//        return Optional.empty();
//    }else
//        return Optional.of(result.get(0));//Eğer en az bir değer dönecektir.İlk dönen değeri Optional olarak dön.
    }

    @Override
    public boolean existById(ID id) {
        CriteriaQuery<T>criteria = (CriteriaQuery<T>) criteriaBuilder.createQuery(t.getClass());
        Root<T>root = (Root<T>) criteria.from(t.getClass());
        criteria.select(root);
        criteria.where(criteriaBuilder.equal(root.get("id"),id));
        T result=entityManager.createQuery(criteria).getSingleResult();
        return result!=null;//eger kayit var ise (null değil ise)=> true | eğer kayit yok ise (result null)->false
    }

    /*
    Select * From tlb_?????
     */
    @Override
    public List<T> findAll() {
        CriteriaQuery<T>criteria = (CriteriaQuery<T>) criteriaBuilder.createQuery(t.getClass());
        Root<T>root = (Root<T>) criteria.from(t.getClass());
        criteria.select(root);
        return entityManager.createQuery(criteria).getResultList();
    }
    /*
    Select * From tbl_???? WHERE column = value
    columnName
    value ->
     */
    @Override
    public List<T> findByColumnNameAndValue(String columnName, String value) {

        CriteriaQuery<T>criteria = (CriteriaQuery<T>) criteriaBuilder.createQuery(t.getClass());
        Root<T>root = (Root<T>) criteria.from(t.getClass());
        criteria.select(root);
//        criteria.where(criteriaBuilder.equal(root.get(columnName),value));//kullanıcının girdiği kolonadı ve değerin eşliğini kontorl eder.
        criteria.where(criteriaBuilder.like(root.get(columnName),value));
        return entityManager.createQuery(criteria).getResultList();
    }

    /*
    Burada yapılmak istenilen detay sudur.Bir sınıf içindeki alan adlari.(değişkenler) yazolım tarafından okunulcak ve bu değişkenlerin değerleri
    kontrol edirecek null olmayanlar soruya dahil edilecek.Böylece esnek sorgularını işlemini otomatikleştirilmiş olacak.
    Bu işlemi yazabilmek için Java REflection Api kullanılacaktır.Bu işlemlerin adına reserve Engineering
    Tersine Mühendislik denir.

        Musteri musteri = new Musteri();
        musteri.setAd("M");
        musteri.setSoyad("Hoca");

        SELECT * FROM tbl_musteri WHERE ad LIKE '%M%' and  soyad LIKE '%HOCA%'


     */
    @Override
    public List<T> findByEntity(T entity) {

        List<T>result=null;
        Class cl=entity.getClass();//entitynin class özelliklerini geçiyorum
        Field[] fl=cl.getDeclaredFields();//id,ad,soyad vs...//class içindeki tüm değişkenleri bir liste içine alıyorum.id,ad,soyad vs
        try {
            CriteriaQuery<T>criteria = (CriteriaQuery<T>) criteriaBuilder.createQuery(t.getClass());
            Root<T>root = (Root<T>) criteria.from(t.getClass());
            criteria.select(root);
            List<Predicate>predicateList=new ArrayList<>();//sorgu için gerekli kriterlerin listesini ekleyeceğimiz liste.
            for(int i=0;i<fl.length;i++){
                fl[i].setAccessible(true);//erişmek istediğimiz alanlarını erişime açıyoruz.Bunu unutursanız null gelecektir.
                /*
                okumakta olduğum alan null değil ise,
                ayrıca okuduğum alan id değil ise,
                 */
                if(fl[i].get(entity.getClass())!=null && !fl[i].get(entity).equals("id")){
                    /*
                    Sorgulari yazarken değişkenlerin tipi önemlidir.Mesela int bir deger için LIKE kullanamazsınız.
                     */
                    if(fl[i].getType().isAssignableFrom(String.class)){//okumakta olduğun field tipi String ise bunu yap.
                       predicateList.add(criteriaBuilder.like(root.get(fl[i].getName()),"%"+fl[i].get(entity)+"%")) ;
                    }if(fl[i].getType().isAssignableFrom(Number.class)){
                        predicateList.add(criteriaBuilder.equal(root.get(fl[i].getName()),fl[i].get(entity)));
                    }
                }
            }
            /*
            [34,18,15,16,178,168,147,741] gibi bir veri istiyor.
             */
            criteria.where(predicateList.toArray(new Predicate[]{}));
            result=entityManager.createQuery(criteria).getResultList();
        }catch (Exception e){
            System.out.println("Beklenmeyen Bir Hata Oldu "+e.toString());
        }
        return result;
    }





}
