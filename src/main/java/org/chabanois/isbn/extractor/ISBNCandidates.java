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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ISBNCandidates implements Iterable<ISBN> {
    private Map<String, ISBN> isbns = new HashMap();
    private float highScore = 0.0F;

    public ISBNCandidates() {
    }

    public void addISBN(ISBN isbn) {
        ISBN actualIsbn = (ISBN)this.isbns.get(isbn.getIsbnWithoutSeparators());
        if (actualIsbn != null) {
            if (actualIsbn.getScore() < isbn.getScore()) {
                actualIsbn.setScore(isbn.getScore());
            }

            isbn = actualIsbn;
        } else {
            this.isbns.put(isbn.getIsbnWithoutSeparators(), isbn);
        }

        if (isbn.getScore() > this.highScore) {
            this.highScore = isbn.getScore();
        }

    }

    public ISBN getHighestScoreISBN() {
        float highScore = 0.0F;
        ISBN bestIsbn = null;
        Iterator i$ = this.isbns.values().iterator();

        while(i$.hasNext()) {
            ISBN isbn = (ISBN)i$.next();
            if (isbn.getScore() > highScore) {
                bestIsbn = isbn;
                highScore = isbn.getScore();
            }
        }

        return bestIsbn;
    }

    public float getHighScore() {
        return this.highScore;
    }

    public Iterator<ISBN> iterator() {
        List<ISBN> values = new ArrayList(this.isbns.values());
        Collections.sort(values, new Comparator<ISBN>() {
            public int compare(ISBN isbn1, ISBN isbn2) {
                return Float.valueOf(isbn2.getScore()).compareTo(isbn1.getScore());
            }
        });
        return values.iterator();
    }

    public boolean isEmpty() {
        return this.isbns.isEmpty();
    }

    public int size() {
        return this.isbns.size();
    }
}
