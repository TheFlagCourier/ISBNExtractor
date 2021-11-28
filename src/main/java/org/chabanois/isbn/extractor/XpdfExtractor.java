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
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class XpdfExtractor implements ITextExtractor {
    protected Log logger = LogFactory.getLog(this.getClass());

    public XpdfExtractor() {
    }

    public Reader getText(File file) throws IOException {
        List<String> cmdLine = new ArrayList();
        cmdLine.add("/usr/bin/pdftotext");
        cmdLine.add(file.getAbsolutePath());
        cmdLine.add("-");

        Process process;
        try {
            process = Runtime.getRuntime().exec((String[])cmdLine.toArray(new String[0]), (String[])null);
        } catch (IOException var5) {
            this.logger.error("Could not execute pdftotxt", var5);
            throw var5;
        }

        return new InputStreamReader(process.getInputStream());
    }

    public boolean hasPageBreaks() {
        return true;
    }
}
