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

public abstract class ISBN {
    protected String isbn;
    protected float score;

    public ISBN(String isbn, float score) {
        this.isbn = isbn;
        this.score = score;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public abstract String getIsbnWithoutSeparators();

    public float getScore() {
        return this.score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String toString() {
        return "ISBN : " + this.isbn + " (" + this.score + ")";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ISBN)) {
            return false;
        } else {
            ISBN isbn = (ISBN)obj;
            return this.getIsbnWithoutSeparators().equals(isbn.getIsbnWithoutSeparators());
        }
    }
}
