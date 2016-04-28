/*
 * Copyright (C) 2014
 * Center for Excellence in Nanomedicine (NanoCAN)
 * Molecular Oncology
 * University of Southern Denmark
 * ###############################################
 * Written by:	Markus List
 * Contact: 	mlist'at'health'.'sdu'.'dk
 * Web:			http://www.nanocan.org/miracle/
 * ###########################################################################
 *	
 *	This file is part of MIRACLE.
 *
 *  MIRACLE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with this program. It can be found at the root of the project page.
 *	If not, see <http://www.gnu.org/licenses/>.
 *
 * ############################################################################
 */
package data
import com.monitorjbl.xlsx.StreamingReader
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row

class XlsxImportService {

    def parseXLSXSheetToTSV(inputFile, sheetIndex, indicator) {

        StringBuffer data = new StringBuffer()
        InputStream is = inputFile.inputStream

        try{
            StreamingReader reader = StreamingReader.builder()
                    .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .sheetIndex(sheetIndex)        // index of sheet to use (defaults to 0)
                    //.sheetName("sheet1")  // name of sheet to use (overrides sheetIndex)
                    .read(is);            // InputStream or File for XLSX file (required)

            for (Row r : reader) {
                for (Cell cell : r) {
                    if(indicator || cell.rowIndex == 0 || cell.columnIndex == 0) data.append(cell.getStringCellValue() + "\t");
                    else{
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_BOOLEAN:
                                data.append(cell.getBooleanCellValue()?"1":"0" + "\t");

                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                data.append(cell.getNumericCellValue() + "\t");

                                break;
                            case Cell.CELL_TYPE_STRING:
                                data.append(cell.getStringCellValue() + "\t");
                                break;

                            case Cell.CELL_TYPE_BLANK:
                                data.append("" + "\t");
                                break;
                            default:
                                data.append(cell + "\t");

                        }
                    }
                }
                data.append("\n")
            }

            return(data.toString());
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
}









