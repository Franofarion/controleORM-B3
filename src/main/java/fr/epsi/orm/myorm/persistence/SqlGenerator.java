package fr.epsi.orm.myorm.persistence;

import fr.epsi.orm.myorm.annotation.Column;
import fr.epsi.orm.myorm.annotation.Entity;
import fr.epsi.orm.myorm.annotation.Id;
import fr.epsi.orm.myorm.lib.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by fteychene on 14/05/17.
 */
public class SqlGenerator {

    public static String getColumnNameForField(Field field) {
        return ReflectionUtil.getAnnotationForField(field, Column.class)
                .map((column) -> {
                    if (column.name().equals("")) {
                        return null;
                    }
                    return column.name();
                }).orElse(field.getName());
    }


    public static String getTableForEntity(Class<?> clazz) {
        return ReflectionUtil.getAnnotationForClass(clazz, Entity.class)
                .map((entity) -> {
                    if (entity.table().equals("")) {
                        return null;
                    }
                    return entity.table();
                }).orElse(clazz.getSimpleName());
    }

    public static String generateDeleteSql(Class<?> entityClass) {
        return ReflectionUtil.getFieldDeclaringAnnotation(entityClass, Id.class)
                .map((f) ->
                        "DELETE FROM "+
                        getTableForEntity(entityClass)+
                        " WHERE "+ getColumnNameForField(f) + " = :"+f.getName())
                .orElse("");
    }
}
