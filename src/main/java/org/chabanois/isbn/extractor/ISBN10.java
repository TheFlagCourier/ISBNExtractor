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

public class ISBN10 extends ISBN {
    public ISBN10(String isbn) {
        this(isbn, 1.0F);
    }

    public ISBN10(String isbn, float score) {
        super(isbn, score);
    }

    public String getIsbnWithoutSeparators() {
        if (this.isbn.length() == 0) {
            return this.isbn;
        } else {
            StringBuffer isbnWithoutSeparator = new StringBuffer();

            for(int i = 0; i < this.isbn.length() - 1; ++i) {
                char c = this.isbn.charAt(i);
                if (Character.isDigit(c)) {
                    isbnWithoutSeparator.append(c);
                }
            }

            char c = this.isbn.charAt(this.isbn.length() - 1);
            if (Character.isDigit(c) || c == 'X') {
                isbnWithoutSeparator.append(c);
            }

            return isbnWithoutSeparator.toString();
        }
    }
}
