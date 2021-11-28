/*
    ISBNExtractor - A library to extract ISBN numbers from PDF Files.
    Copyright (C) 2008 Cédric Chabanois.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.chabanois.isbn.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileISBNExtractorFactory {
    protected static Log logger = LogFactory.getLog(FileISBNExtractorFactory.class);

    public FileISBNExtractorFactory() {
    }

    public static FileISBNExtractor getFileISBNExtractor(File propertiesFile) {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(propertiesFile));
        } catch (FileNotFoundException var3) {
            logger.warn("Config properties file not found. Using default configuration");
        } catch (IOException var4) {
            logger.warn("Could not read config properties file. Using default configuration");
        }

        return getFileISBNExtractor(properties);
    }

    public static FileISBNExtractor getFileISBNExtractor(Properties properties) {
        FileISBNExtractor fileISBNExtractor = new FileISBNExtractor();
        long searchMaxBytes = 9223372036854775807L;
        long searchMinBytes = 9223372036854775807L;
        float minCandidateScore = 0.0F;
        String textExtractorClassName = null;
        textExtractorClassName = properties.getProperty("textExtractorClass", (String)null);

        try {
            searchMaxBytes = Long.parseLong(properties.getProperty("searchMaxBytes", Long.toString(9223372036854775807L)));
        } catch (NumberFormatException var15) {
        }

        try {
            searchMinBytes = Long.parseLong(properties.getProperty("searchMinBytes", Long.toString(9223372036854775807L)));
        } catch (NumberFormatException var14) {
        }

        try {
            minCandidateScore = Float.parseFloat(properties.getProperty("minCandidateScore", Float.toString(0.0F)));
        } catch (NumberFormatException var13) {
        }

        ITextExtractor preferredTextExtractor = null;
        if (textExtractorClassName != null) {
            try {
                Class clazz = Class.forName(textExtractorClassName);
                preferredTextExtractor = (ITextExtractor)clazz.newInstance();
            } catch (ClassNotFoundException var10) {
                logger.error("Could not instanciate '" + textExtractorClassName + "'", var10);
            } catch (InstantiationException var11) {
                logger.error("Could not instanciate '" + textExtractorClassName + "'", var11);
            } catch (IllegalAccessException var12) {
                logger.error("Could not instanciate '" + textExtractorClassName + "'", var12);
            }
        }

        if (preferredTextExtractor != null) {
            fileISBNExtractor.getTextReaderFactory().setPreferredPdfExtractor(preferredTextExtractor);
        }

        fileISBNExtractor.setSearchMaxBytes(searchMaxBytes);
        fileISBNExtractor.setSearchMinBytes(searchMinBytes);
        fileISBNExtractor.setMinCandidateScore(minCandidateScore);
        return fileISBNExtractor;
    }
}
