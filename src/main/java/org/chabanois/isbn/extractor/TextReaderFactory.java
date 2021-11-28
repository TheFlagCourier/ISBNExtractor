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
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TextReaderFactory {
    protected Log logger = LogFactory.getLog(TextReaderFactory.class);
    private ITextExtractor preferredPdfExtractor = new XpdfExtractor();

    public TextReaderFactory() {
    }

    public void setPreferredPdfExtractor(ITextExtractor pdfExtractor) {
        this.preferredPdfExtractor = pdfExtractor;
    }

    public Reader getTextReader(File file) throws IOException {
        this.logger.info("Extracting text from : " + file.toString());
        long time1 = System.currentTimeMillis();
        Reader reader = this.preferredPdfExtractor.getText(file);
        long time2 = System.currentTimeMillis();
        this.logger.info("Extraction of text done (" + (time2 - time1) + " ms)");
        return reader;
    }
}
