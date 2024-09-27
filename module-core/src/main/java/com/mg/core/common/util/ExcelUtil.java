package com.mg.core.common.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Excel 파일을 읽고 쓰는 유틸리티 클래스입니다.
 */
public class ExcelUtil {
    /**
     * 지정된 Excel 파일에서 데이터를 읽어옵니다.
     *
     * @param file     Excel 파일
     * @param keys     각 열에 대응하는 키 배열
     * @param haveHead 테이블 헤더가 있는지 여부
     * @param reverse  데이터를 역순으로 읽을지 여부
     * @return 각 행을 맵으로 변환한 데이터 리스트
     */
    public static List<Map<String, String>> readExcel(File file, String[] keys, boolean haveHead, boolean reverse) {
        List<Map<String, String>> resultList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 접근
            int rowNum = sheet.getLastRowNum(); // 마지막 행 번호
            int startRow = haveHead ? 1 : 0; // 헤더가 있으면 1부터 시작

            for (int rowIndex = startRow; rowIndex <= rowNum; rowIndex++) {
                Row row = sheet.getRow(rowIndex); // 현재 행 가져오기
                if (row == null)
                    continue;
                Cell firstCell = row.getCell(0); // 첫 번째 셀 가져오기
                if (firstCell == null || firstCell.toString().trim().isEmpty())
                    continue;

                Map<String, String> rowMap = new HashMap<>();
                for (int colIndex = 0; colIndex < keys.length; colIndex++) {
                    Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    rowMap.put(keys[colIndex], cell == null ? "" : cell.toString());
                }
                resultList.add(rowMap);
            }

            if (reverse) {
                Collections.reverse(resultList); // 리스트 역순으로 정렬
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * 새 Excel 파일을 생성합니다.
     *
     * @param path       파일 경로
     * @param sheetName  시트 이름
     * @param dataTitles 열 제목
     * @param dataList   데이터 목록
     * @param keys       데이터의 키 배열
     */
    public static void createExcel(String path, String sheetName, String[] dataTitles,
            List<Map<String, String>> dataList, String[] keys) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName); // 새 시트 생성
            int rowIndex = 0;

            // 헤더 행 생성
            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < dataTitles.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(dataTitles[i]);
            }

            // 데이터 행 채우기
            for (Map<String, String> dataMap : dataList) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 0; i < keys.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(dataMap.getOrDefault(keys[i], ""));
                }
            }

            // 파일로 쓰기
            try (FileOutputStream out = new FileOutputStream(path)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
