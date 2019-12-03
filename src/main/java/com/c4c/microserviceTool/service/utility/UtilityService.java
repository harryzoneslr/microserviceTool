package com.c4c.microserviceTool.service.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility Class to provide common functions
 * @Author:i043125
 *
 */
@Service
public class UtilityService {

    @Autowired
    HttpServletRequest httpServletRequest;

    /**
     * Get User Name Information from httpServletRequest;
     * @return
     */
    public String getUserNameFromHttpRequest(){
        // Get From request header: 'X-User-Name'
        if (httpServletRequest != null && httpServletRequest.getHeader("X-User-Name") != null) {
            return httpServletRequest.getHeader("X-User-Name");
        }
        return null;
    }

    public static boolean checkEmptyList(List<?> rawList){
        return(rawList == null || rawList.size() == 0);
    }

    public static String filterListByKey(List<String> strList, String elementStr){
        if(checkEmptyList(strList)){
            return null;
        }
        if(elementStr == null){
            return null;
        }
        for(String str:strList){
            if(elementStr.equals(str)){
                return str;
            }
        }
        return null;
    }

    public static List<String> filterListByContains(List<String> strList, String elementStr){
        if(checkEmptyList(strList)){
            return null;
        }
        if(elementStr == null){
            return null;
        }
        List<String> resultList = new ArrayList<>();
        strList.forEach(str->{
            if(str != null && str.contains(elementStr)){
                resultList.add(str);
            }
        });
        return resultList;
    }


    /**
     * String Utility Method: Set Header to Lower Case
     * @param str
     * @return
     */
    public static String headerToLowerCase(String str) {
        if(StringUtils.isEmpty(str)){
            return null;
        }
        String header = str.substring(0, 1);
        return str.replaceFirst(header, header.toLowerCase());
    }

    /**
     * String Utility Method: Set Header To Upper Case
     * @param str
     * @return
     */
    public static String headerToUpperCase(String str) {
        if(StringUtils.isEmpty(str)){
            return null;
        }
        String header = str.substring(0, 1);
        return str.replaceFirst(header, header.toLowerCase());
    }

}
