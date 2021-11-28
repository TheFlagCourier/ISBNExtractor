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

public class ISBN13 extends ISBN {
    public ISBN13(String isbn) {
        this(isbn, 1.0F);
    }

    public ISBN13(String isbn, float score) {
        super(isbn, score);
    }

    public String getIsbnWithoutSeparators() {
        StringBuffer isbnWithoutSeparator = new StringBuffer();

        for(int i = 0; i < this.isbn.length(); ++i) {
            char c = this.isbn.charAt(i);
            if (Character.isDigit(c)) {
                isbnWithoutSeparator.append(c);
            }
        }

        return isbnWithoutSeparator.toString();
    }
}
