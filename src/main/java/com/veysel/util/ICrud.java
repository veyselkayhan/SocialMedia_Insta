package com.veysel.util;

import java.util.List;
import java.util.Optional;

public interface ICrud <T,ID>{

    T save(T entity);
    // bir entity i kaydedince onu dönmek en doğrusu olur.

    Iterable saveAll(Iterable <T> entites);
    //verdiğim müşteri or urun vb entityi kaydet bana onu geri dön

    void delete(T entity);

    void deleteById(ID id);

    Optional<T>findById(ID id);

    boolean existById(ID id);

    List<T>findAll();
    List<T>findByEntity(T entity);

    List<T>findByColumnNameAndValue(String columnName,String value);
 






}
