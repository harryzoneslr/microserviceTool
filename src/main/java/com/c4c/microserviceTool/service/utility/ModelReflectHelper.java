package com.c4c.microserviceTool.service.utility;

import com.c4c.microserviceTool.dto.BaseDto;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Utility Class to provide some reflective methods
 */
@Service
public class ModelReflectHelper {

    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

   /**
     * Check if this model type is sub class of specified super class
     * @param modelType
     * @param baseClassName
     * @return
     */
    public static boolean checkSuperClassExtends(Class<?> modelType,
                                                 String baseClassName) {
        if (modelType == null) {
            return false;
        }
        if (baseClassName.equals(modelType.getSimpleName())) {
            return true;
        }
        if (Object.class.getSimpleName().equals(modelType.getSimpleName())) {
            return false;
        }
        return checkSuperClassExtends(modelType.getSuperclass(), baseClassName);
    }

    /**
     * using reflection tech to set value to Object
     *
     * @param field
     * @param value
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void reflectSetValue(Field field, Object instance,
                                       Object value, SimpleDateFormat dateFormat)
            throws NumberFormatException, IllegalArgumentException,
            IllegalAccessException {
        if (value == null) {
            return;
        }
        String dateTypeName = Date.class.getSimpleName();
        String intTypeName = int.class.getSimpleName();
        String floatTypeName = float.class.getSimpleName();
        String integarTypeName = Integer.class.getSimpleName();
        String doubleTypeName = double.class.getSimpleName();
        String doubleUpTypeName = Double.class.getSimpleName();
        String floatUpTypeName = Float.class.getSimpleName();
        field.setAccessible(true);
        String fieldType = field.getType().getSimpleName();
        if (doubleUpTypeName.equals(fieldType)
                || doubleTypeName.equals(fieldType)) {
            field.setDouble(instance, Double.parseDouble(value.toString()));
            return;
        }
        if (floatTypeName.equals(fieldType)
                || floatUpTypeName.equals(fieldType)) {
            field.setFloat(instance, Float.parseFloat(value.toString()));
            return;
        }
        if (integarTypeName.equals(fieldType) || intTypeName.equals(fieldType)) {
            field.setInt(instance, Integer.parseInt(value.toString()));
            return;
        }
        if (dateTypeName.equals(fieldType)) {
            try{
                if (dateFormat == null) {
                    // using the default Date format
                    Date dateValue = DATE_TIME_FORMAT.parse(value.toString());
                    field.set(instance, dateValue);
                } else {
                    Date dateValue = dateFormat.parse(value.toString());
                    field.set(instance, dateValue);
                }
            }catch(ParseException ex){
                // Continue when parse date format error
            }
            return;
        }
        field.set(instance, value);
    }

    /**
     * Get Embedded Base model fields in reflective way
     * @param modelClass
     */
    public static List<Field>  getFields(Class<?> modelClass){
        List<Field> fieldList = new ArrayList<>();
        List<Class<?>> classes = getDtoModelInheritanceList(modelClass);
        for (Class<?> clsType : classes) {
            Field[] allFields = clsType.getDeclaredFields();
            for (Field field : allFields) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    // Filter out the constant fields
                    if (Modifier.isStatic(field.getModifiers()))
                        continue;
                    fieldList.add(field);
                }
            }
        }
        return fieldList;
    }

    /**
     * Get Embedded Base model fields in reflective way
     * @param modelClass
     */
    public static List<Field>  getListTypeFields(Class<?> modelClass){
        List<Field> listFields = new ArrayList<>();
        List<Class<?>> classes = getDtoModelInheritanceList(modelClass);
        for (Class<?> clsType : classes) {
            Field[] allFields = clsType.getDeclaredFields();
            for (Field field : allFields) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    // Filter out the constant fields
                    if (Modifier.isStatic(field.getModifiers()))
                        continue;
                    if (field.getType().isAssignableFrom(List.class)) {
                        listFields.add(field);
                    }
                }
            }
        }
        return listFields;
    }

    public static List<String>  getListTypeFieldNames(Class<?> modelClass){
        List<String> resultList = new ArrayList<>();
        List<Field> listFields = getListTypeFields(modelClass);
        if(listFields == null){
            return null;
        }
        listFields.forEach(field -> {
            resultList.add(field.getName());
        });
        return resultList;
    }


    /**
     * Get Current Model's Class list to BaseModel
     * @param modelClass
     * @return
     */
    private static List<Class<?>> getDtoModelInheritanceList(Class<?> modelClass) {
        List<Class<?>> clsList = Collections
                .synchronizedList(new ArrayList<Class<?>>());
        if (modelClass.getSimpleName().equals(BaseDto.class.getSimpleName()) ||
                Object.class.getSimpleName().equals(BaseDto.class.getSimpleName())) {
            clsList.add(modelClass);
        } else {
            clsList.addAll(getDtoModelInheritanceList(modelClass.getSuperclass()));
            clsList.add(modelClass);
        }
        return clsList;
    }

}
