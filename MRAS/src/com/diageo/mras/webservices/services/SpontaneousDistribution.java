package com.diageo.mras.webservices.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.diageo.mras.webservices.dao.SqlStatements;
import com.diageo.mras.webservices.init.ConnectionPool;

public class SpontaneousDistribution {
	/**
	 * Description of ReadCSV()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param offerId
	 *            This is the offer Id
	 * @param path
	 *            This is the path where the consumer list is place
	 * 
	 * @return Inserts the rewards into the Distribution table.
	 */

	static Logger logger = Logger.getLogger(SpontaneousDistribution.class
			.getName());

	public static Vector readUserList(String fileName) {
		Vector cellVectorHolder = new Vector();
		try {
			
			FileInputStream myInput = new FileInputStream(fileName);
			POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
		
			HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
			HSSFSheet mySheet = myWorkBook.getSheetAt(0);
			Iterator rowIter = mySheet.rowIterator();
			while (rowIter.hasNext()) {
				HSSFRow myRow = (HSSFRow) rowIter.next();
				Iterator cellIter = myRow.cellIterator();
				Vector cellStoreVector = new Vector();
				while (cellIter.hasNext()) {
					HSSFCell myCell = (HSSFCell) cellIter.next();
					cellStoreVector.addElement(myCell);
				}
				cellVectorHolder.addElement(cellStoreVector);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cellVectorHolder;
	}

	public static List<Integer> getConsumerList(Vector dataHolder) {

		List<Integer> consuemrIdList = new ArrayList<Integer>();
		int dataHolderSize = dataHolder.size();
		
		
		
		for (int i = 1; i < dataHolderSize; i++) {
			
			Vector cellStoreVector = (Vector) dataHolder.elementAt(i);
			int cellStoreVectorSize = cellStoreVector.size();
			for (int j = 0; j < cellStoreVectorSize; j++) {
				HSSFCell myCell = (HSSFCell) cellStoreVector.elementAt(j);
				String stringCellValue = myCell.toString();
			
				try {
					
					double valueDouble = Double.parseDouble(stringCellValue);
					int valueInteger = (int) valueDouble;
					logger.debug("double " + valueInteger);
					
					consuemrIdList.add(valueInteger);
				} catch (Exception e) {
					logger.info("Exception :" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
		return consuemrIdList;
	}

	
}
