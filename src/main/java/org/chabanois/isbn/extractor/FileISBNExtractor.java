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

public class FileISBNExtractor {
    protected Log logger = LogFactory.getLog(this.getClass());
    private TextReaderFactory textReaderFactory = new TextReaderFactory();
    private long searchMaxBytes = 9223372036854775807L;
    private long searchMinBytes = 9223372036854775807L;
    private float minCandidateScore = 0.0F;

    public FileISBNExtractor() {
    }

    public void setSearchMaxBytes(long searchFirstBytes) {
        this.searchMaxBytes = searchFirstBytes;
    }

    public void setSearchMinBytes(long searchMinBytes) {
        this.searchMinBytes = searchMinBytes;
    }

    public void setMinCandidateScore(float minCandidateScore) {
        this.minCandidateScore = minCandidateScore;
    }

    public TextReaderFactory getTextReaderFactory() {
        return this.textReaderFactory;
    }

    public ISBNCandidates getIsbnCandidates(File file) throws IOException {
        try {
            Reader reader = this.textReaderFactory.getTextReader(file);
            ISBNExtractor isbnExtractor = new ISBNExtractor(reader);
            isbnExtractor.setSearchMaxBytes(this.searchMaxBytes);
            isbnExtractor.setSearchMinBytes(this.searchMinBytes);
            isbnExtractor.setMinCandidateScore(this.minCandidateScore);
            ISBNCandidates isbnCandidates = isbnExtractor.searchISBNs();
            if (isbnCandidates.isEmpty()) {
                this.logger.warn("isbn not found for " + file.toString());
            }

            return isbnCandidates;
        } catch (IOException var5) {
            this.logger.error("Could not get isbn", var5);
            throw var5;
        }
    }
}
