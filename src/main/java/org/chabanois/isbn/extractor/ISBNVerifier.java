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

public class ISBNVerifier {
    public ISBNVerifier() {
    }

    public static boolean isIsbn10(String isbn) {
        StringBuffer isbnWithoutSeparator = new StringBuffer();

        for(int i = 0; i < isbn.length() - 1; ++i) {
            char c = isbn.charAt(i);
            if (Character.isDigit(c)) {
                isbnWithoutSeparator.append(c);
            }
        }

        char c = isbn.charAt(isbn.length() - 1);
        if (Character.isDigit(c) || c == 'X') {
            isbnWithoutSeparator.append(c);
        }

        if (isbnWithoutSeparator.length() != 10) {
            return false;
        } else {
            long sum = 0L;

            for(int i = 0; i < 10; ++i) {
                String ch = isbnWithoutSeparator.substring(i, i + 1);
                int digit;
                if (ch.equals("X")) {
                    digit = 10;
                } else {
                    digit = Integer.parseInt(ch);
                }

                sum += (long)(digit * (10 - i));
            }

            if (sum % 11L != 0L) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static boolean isIsbn13(String isbn) {
        StringBuffer isbnWithoutSeparator = new StringBuffer();

        int sum;
        for(sum = 0; sum < isbn.length(); ++sum) {
            char c = isbn.charAt(sum);
            if (Character.isDigit(c)) {
                isbnWithoutSeparator.append(c);
            }
        }

        if (isbnWithoutSeparator.length() != 13) {
            return false;
        } else {
            sum = 0;

            for(int i = 0; i < 13; ++i) {
                String ch = isbnWithoutSeparator.substring(i, i + 1);
                int digit = Integer.parseInt(ch);
                if (i % 2 == 0) {
                    sum += digit;
                } else {
                    sum += digit * 3;
                }
            }

            if (sum % 10 != 0) {
                return false;
            } else {
                return true;
            }
        }
    }
}
