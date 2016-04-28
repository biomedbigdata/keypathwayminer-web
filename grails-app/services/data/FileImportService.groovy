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

import org.apache.commons.lang.StringUtils

/**
 * This service converts various file types and extracts table headers.
 */
class FileImportService {

    def grailsApplication

    def convertCSV2(String content)
    {
        content = StringUtils.replace(content, ".", "")
        content = StringUtils.replace(content, ",", ".")
        content = StringUtils.replace(content, ";", ",")

        return content
    }

    def convertCSVtoTSV(String content){
        convertCustom(content, "comma", ".", ",")
    }
    def convertCustom(String content, String columnSeparator, String decimalSeparator, String thousandSeparator)
    {
        switch(columnSeparator){
        case "comma":
            columnSeparator = ","
            break
        case "semicolon":
            columnSeparator = ";"
            break
        case "tab":
            columnSeparator = "\t"
            break
        }

        //remove thousand separator
        if(thousandSeparator != "")
        content = StringUtils.replace(content, thousandSeparator, "")

        //temporarily substitute column separator with tab to avoid comma confusion with decimal separator
        if(columnSeparator != "\t")
        {
            content = StringUtils.replace(content, columnSeparator, "\t")
        }

        //replace decimal separator
        content = StringUtils.replace(content, decimalSeparator, ".")

        return (content)
    }

    /**
     * Skip lines and read header, then parse it to array
     * @param content
     * @return
     */
    def extractHeader(def content, def skipLines)
    {
        Scanner scanner = new Scanner(content)

        //skipping lines

        for( int i = 0; i < skipLines; i++ )
        {
            if(scanner.hasNext()) scanner.nextLine()
        }

        //reading and parsing header
        def header = scanner.nextLine()
        scanner.close()

        header = header.split('\t')
        header = Arrays.asList(header)

        return (header)
    }
}
