package com.c4c.microserviceTool.service;

import com.c4c.microserviceTool.service.utility.UtilityService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class ExcelParseService {

    private static final String fileUrl = "";

    // constant: From which line should data start reading
    private static final int DATA_START_ROWINDEX = 1;

    private static final int DATA_MAX_COL = 50;

    public List<JSONObject> parseExcelToJSONObject(String excelFileUrl) {
        try {
            File file = new File(excelFileUrl);
            InputStream targetStream = new FileInputStream(file);
            List<JSONObject> baseObjList = parseExcelToJSONModel(targetStream);
            return baseObjList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<JSONObject>  parseExcelToJSONModel(InputStream in) {
        try {
            List<JSONObject> resultJsonList = new ArrayList<JSONObject>();
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
            for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                List<JSONObject>  tmpJsonList = readDataArrayFromSheet(xssfSheet);
                if (!UtilityService.checkEmptyList(tmpJsonList)) {
                    resultJsonList.addAll(tmpJsonList);
                }
            }
            return resultJsonList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private List<JSONObject> readDataArrayFromSheet(XSSFSheet curSheet) {
        Map<String, Integer> excelColMap = getFieldColumMap(curSheet, 0);
        List<JSONObject> jsonObjList = new ArrayList<>();
        for (int rowIndex = DATA_START_ROWINDEX; rowIndex <= curSheet
                .getLastRowNum(); rowIndex++) {
            JSONObject rootObject = new JSONObject();
            readRowToJSON(curSheet, rootObject, rowIndex, excelColMap);
            jsonObjList.add(rootObject);
        }
        return jsonObjList;
    }

    private void readRowToJSON(XSSFSheet curSheet, JSONObject jsonObject, int rowIndex, Map<String, Integer> excelColMap) {
        XSSFRow xssfRow = curSheet.getRow(rowIndex);
        if (xssfRow == null) {
            return;
        }
        Set<String> keySet = excelColMap.keySet();
        Iterator<String> it = keySet.iterator();
        Map<Integer, Map<String, List<String>>> deepPropMap = new HashMap<>();
        while (it.hasNext()) {
            String key = it.next();
            String[] arrayList = key.split("\\.");
            if (arrayList == null || arrayList.length == 0) {
                continue;
            }
            if (arrayList.length == 1) {
                // in case top node property
                if (!excelColMap.containsKey(key)) {
                    continue;
                }
                int col = excelColMap.get(key);
                XSSFCell cell = xssfRow.getCell(col);
                jsonObject.put(key, getValue(cell));
            }
            if (arrayList.length >= 2) {
                groupByHeaderWrapper(key, deepPropMap);
            }
        }
        // Process deep property
        Set<Integer> deepkeySet = deepPropMap.keySet();
        Iterator<Integer> itDeep = deepkeySet.iterator();
        while (itDeep.hasNext()) {
            Integer deepth = itDeep.next();
            if (deepth > 2 || !deepPropMap.containsKey(deepth)) {
                // temporary
                continue;
            }
            Map<String, List<String>> subPropMap = deepPropMap.get(deepth);
            parseToJSONArray(jsonObject, subPropMap, xssfRow, excelColMap);
        }
    }

    private void groupByHeaderWrapper(String key, Map<Integer, Map<String, List<String>>> deepPropMap) {
        String[] arrayList = key.split("\\.");
        if (arrayList == null || arrayList.length == 1) {
            return;
        }
        if (deepPropMap.containsKey(arrayList.length)) {
            Map<String, List<String>> subPropMap = deepPropMap.get(arrayList.length);
            groupByHeader(key, subPropMap);
        } else {
            Map<String, List<String>> subPropMap = new HashMap<>();
            groupByHeader(key, subPropMap);
            deepPropMap.put(arrayList.length, subPropMap);
        }
    }

    private void groupByHeader(String key, Map<String, List<String>> subPropMap) {
        String[] arrayList = key.split("\\.");
        if (arrayList == null || arrayList.length == 1) {
            return;
        }
        String header = arrayList[0];
        if (subPropMap.containsKey(header)) {
            List<String> subList = subPropMap.get(header);
            subList.add(key);
        } else {
            List<String> subList = new ArrayList<String>();
            subList.add(key);
            subPropMap.put(header, subList);
        }
    }

    private void parseToJSONArray(JSONObject parentObject, Map<String, List<String>> subPropMap, XSSFRow xssfRow, Map<String, Integer> excelColMap) {
        Set<String> keySet = subPropMap.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            // traverse each header
            JSONArray propArray = new JSONArray();
            JSONObject propValue = new JSONObject();
            propArray.put(propValue);
            String header = it.next();
            if (!subPropMap.containsKey(header)) {
                continue;
            }
            List<String> fullPropList = subPropMap.get(header);
            if (UtilityService.checkEmptyList(fullPropList)) {
                continue;
            }
            for (String prop : fullPropList) {
                parseToJSONPropCore(excelColMap, prop, xssfRow, propValue);
            }
            parentObject.put(header, propArray);
        }
    }

    private void parseToJSONPropCore(Map<String, Integer> excelColMap, String propName, XSSFRow xssfRow, JSONObject jsonObject) {
        if (xssfRow == null) {
            return;
        }
        if (!excelColMap.containsKey(propName)) {
            return;
        }
        String[] arrayList = propName.split("\\.");
        if (arrayList == null) {
            return;
        }
        String key = arrayList[arrayList.length - 1];
        int col = excelColMap.get(propName);
        XSSFCell cell = xssfRow.getCell(col);
        jsonObject.put(key, getValue(cell));
    }


    private Map<String, Integer> getFieldColumMap(XSSFSheet curSheet, int rowIndex) {
        XSSFRow xssfRow = curSheet.getRow(rowIndex);
        if (xssfRow == null) {
            return null;
        }
        Map<String, Integer> resultMap = new HashMap<>();
        for (int col = 0; col < DATA_MAX_COL; col++) {
            XSSFCell cell = xssfRow.getCell(col);
            if (cell != null) {
                try {
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    String fieldValue = getValue(cell);
                    if (StringUtils.isEmpty(fieldValue)) {
                        continue;
                    }
                    resultMap.put(fieldValue, col);
                } catch (NumberFormatException e) {
                    // In case Number format exception, just ignore it.
                }
            }
        }
        return resultMap;
    }

    public static String getValue(XSSFCell xssfCell) {
        if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
            // return the boolean value
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
            // return the numeric value
            return String.valueOf(xssfCell.getNumericCellValue());
        } else {
            // return string type value
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }

}
