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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFBoxTextExtractor implements ITextExtractor {
    public PDFBoxTextExtractor() {
    }

    public Reader getText(File file) throws IOException {
        File scratchDir = new File(System.getProperty("java.io.tmpdir"));
        final File tmpFile = File.createTempFile("isbn", "tmp", scratchDir);
        PDDocument document = null;
        OutputStreamWriter output = null;

        try {
            document = PDDocument.load(file);
            output = new OutputStreamWriter(new FileOutputStream(tmpFile));
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.writeText(document, output);
        } finally {
            if (output != null) {
                output.close();
            }

            if (document != null) {
                document.close();
            }

        }

        return new FileReader(tmpFile) {
            public void close() throws IOException {
                try {
                    super.close();
                } finally {
                    tmpFile.delete();
                }

            }
        };
    }

    public boolean hasPageBreaks() {
        return false;
    }
}
