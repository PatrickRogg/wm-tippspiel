package edu.hm.cs.swt2ss18.wmtipp.mvc;

import org.apache.poi.ss.usermodel.*;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import edu.hm.cs.swt2ss18.wmtipp.service.spiele.TurnierStatus;
import edu.hm.cs.swt2ss18.wmtipp.service.teams.TeamService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public class ExcelReader {
	private static final String FILE_NAME = "ImportDaten.xlsx";
	
	Sheet datatypeSheet;
	
	@Autowired
	TeamService teamService;
	
	public ExcelReader(int workbookNumber) throws IOException{
		FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
	    Workbook workbook = new XSSFWorkbook(excelFile);
		try {
		    this.datatypeSheet = workbook.getSheetAt(workbookNumber);
		} finally {
			workbook.close();
		}
	}
    
	public Sheet getDatatypeSheet() {
		return datatypeSheet;
	}

	public void setDatatypeSheet(Sheet datatypeSheet) {
		this.datatypeSheet = datatypeSheet;
	}

	public String getTeamName(int zeile) {
		CellReference cellReference = new CellReference(zeile, 1); 
		Row row = datatypeSheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		return cell.getStringCellValue();
	}

	public String getWappen(int zeile) {
		CellReference cellReference = new CellReference(zeile, 2); 
		Row row = datatypeSheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		return cell.getStringCellValue();
	}

	public GruppenTyp getGruppenTyp(int zeile) {
		CellReference cellReference = new CellReference(zeile, 0); 
		Row row = datatypeSheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		return GruppenTyp.valueOf(cell.getStringCellValue());
	}
	
	public String getHeimMannschaft(int zeile) {
		CellReference cellReference = new CellReference(zeile, 3); 
		Row row = datatypeSheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		return cell.getStringCellValue();
	}
	
	public String getGastMannschaft(int zeile) {
		CellReference cellReference = new CellReference(zeile, 4); 
		Row row = datatypeSheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		return cell.getStringCellValue();
	}
	
	public String getSpielort(int zeile) {
		CellReference cellReference = new CellReference(zeile, 5); 
		Row row = datatypeSheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		return cell.getStringCellValue();
	}
	
	public LocalDateTime getSpielbeginn(int zeile) {
		DateHelper dateHelper = new DateHelper();
		CellReference cellReference = new CellReference(zeile, 2); 
		Row row = datatypeSheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		return dateHelper.stringToDateExcel(cell.getStringCellValue());
	}
	
	public TurnierStatus getSpielStatus(int zeile) {
		CellReference cellReference = new CellReference(zeile, 0); 
		Row row = datatypeSheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		return TurnierStatus.valueOf(cell.getStringCellValue());
	}

	public int getRows() {
		return datatypeSheet.getLastRowNum() + 1;
	}
}
