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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ISBNExtractor {
    protected Log logger = LogFactory.getLog(this.getClass());
    private PushbackMarkReader reader;
    private ISBNCandidates isbns = new ISBNCandidates();
    private long searchMaxBytes = 9223372036854775807L;
    private long searchMinBytes = 9223372036854775807L;
    private float minCandidateScore = 0.0F;

    public ISBNExtractor(Reader reader) {
        this.reader = new PushbackMarkReader(new BufferedReader(reader), 1000);
    }

    public void setSearchMaxBytes(long searchMaxBytes) {
        this.searchMaxBytes = searchMaxBytes;
    }

    public void setSearchMinBytes(long searchMinBytes) {
        this.searchMinBytes = searchMinBytes;
    }

    public void setMinCandidateScore(float minCandidateScore) {
        this.minCandidateScore = minCandidateScore;
    }

    public ISBNCandidates searchISBNs() throws IOException {
        if (this.logger.isInfoEnabled()) {
            String msg = "Searching ISBN";
            if (this.searchMaxBytes <= 0L) {
                msg = msg + " in the whole file ...";
            } else {
                msg = msg + " in the first " + this.searchMaxBytes + " bytes ...";
            }

            this.logger.info(msg);
        }

        long time1 = System.currentTimeMillis();
        Map<ISBN, Long> positions = new HashMap();
        long page = 1L;

        long isbnPosition;
        try {
            isbnPosition = -2147483648L;

            while(true) {
                long position = this.reader.getPosition();
                if (position > this.searchMinBytes && (double)this.isbns.getHighScore() >= 0.5D || position > this.searchMaxBytes) {
                    break;
                }

                boolean isIsbnString = this.readISBNString();
                if (isIsbnString) {
                    isbnPosition = position;
                }

                ISBN isbn = this.readISBN(position - isbnPosition);
                if (isbn != null && isbn.getScore() >= this.minCandidateScore) {
                    this.isbns.addISBN(isbn);
                    positions.put(isbn, position);
                }

                if (!isIsbnString && isbn == null) {
                    int ch = this.reader.read();
                    if (ch == -1) {
                        break;
                    }

                    if (ch == 12) {
                        ++page;
                    }
                }
            }
        } finally {
            this.reader.close();
        }

        isbnPosition = System.currentTimeMillis();
        Iterator i$ = this.isbns.iterator();

        while(i$.hasNext()) {
            ISBN isbn = (ISBN)i$.next();
            this.logger.info("ISBN : " + isbn + " at position : " + positions.get(isbn));
        }

        this.logger.info("Done (" + (isbnPosition - time1) + " ms). ");
        return this.isbns;
    }

    private boolean readISBNString() throws IOException {
        this.reader.mark();
        int I = this.reader.read();
        if (I != 73) {
            this.reader.unreadToMark();
            return false;
        } else {
            int S = this.reader.read();
            if (S != 83) {
                this.reader.unreadToMark();
                return false;
            } else {
                int B = this.reader.read();
                if (B != 66) {
                    this.reader.unreadToMark();
                    return false;
                } else {
                    int N = this.reader.read();
                    if (N != 78) {
                        this.reader.unreadToMark();
                        return false;
                    } else {
                        this.reader.unmark();
                        return true;
                    }
                }
            }
        }
    }

    private ISBN readISBN(long isbnWordDistance) throws IOException {
        ISBN isbn = this.readISBN13(isbnWordDistance);
        if (isbn != null) {
            return isbn;
        } else {
            isbn = this.readISBN10(isbnWordDistance);
            return isbn;
        }
    }

    private ISBN readISBN13(long isbnWordDistance) throws IOException {
        this.reader.mark();
        String firstNumber = this.readNumber();
        if (firstNumber == null) {
            this.reader.unreadToMark();
            return null;
        } else {
            float score = 1.1F;
            if (isbnWordDistance >= 10L) {
                score /= 3.0F;
            }

            if (firstNumber.length() == 13) {
                score /= 2.0F;
                if (ISBNVerifier.isIsbn13(firstNumber)) {
                    this.reader.unmark();
                    return new ISBN10(firstNumber, score);
                }
            }

            if (firstNumber != null && firstNumber.length() == 3) {
                int separator = this.reader.read();
                if (!this.isValidSeparator(separator)) {
                    this.reader.unreadToMark();
                    return null;
                } else {
                    String registrationGroup = this.readNumber();
                    if (registrationGroup != null && registrationGroup.length() <= 5) {
                        if (this.reader.read() != separator) {
                            this.reader.unreadToMark();
                            return null;
                        } else {
                            String registrationElement = this.readNumber();
                            if (registrationElement != null && registrationElement.length() <= 7) {
                                if (this.reader.read() != separator) {
                                    this.reader.unreadToMark();
                                    return null;
                                } else {
                                    String publicationElement = this.readNumber();
                                    if (publicationElement != null && publicationElement.length() <= 6) {
                                        if (this.reader.read() != separator) {
                                            this.reader.unreadToMark();
                                            return null;
                                        } else {
                                            char checkDigit = (char)this.reader.read();
                                            if (!Character.isDigit(checkDigit)) {
                                                this.reader.unreadToMark();
                                                return null;
                                            } else {
                                                StringBuffer isbn = new StringBuffer();
                                                isbn.append(firstNumber).append('-').append(registrationGroup).append('-').append(registrationElement).append('-').append(publicationElement).append('-').append(checkDigit);
                                                if (!ISBNVerifier.isIsbn13(isbn.toString())) {
                                                    this.reader.unreadToMark();
                                                    return null;
                                                } else {
                                                    this.reader.unmark();
                                                    return new ISBN13(isbn.toString(), score);
                                                }
                                            }
                                        }
                                    } else {
                                        this.reader.unreadToMark();
                                        return null;
                                    }
                                }
                            } else {
                                this.reader.unreadToMark();
                                return null;
                            }
                        }
                    } else {
                        this.reader.unreadToMark();
                        return null;
                    }
                }
            } else {
                this.reader.unreadToMark();
                return null;
            }
        }
    }

    private boolean isValidSeparator(int separator) {
        return separator == 32 || separator == 45 || separator >= 8208 && separator <= 8212 || separator == 8722;
    }

    private ISBN readISBN10(long isbnWordDistance) throws IOException {
        this.reader.mark();
        String firstNumber = this.readNumber();
        if (firstNumber == null) {
            this.reader.unreadToMark();
            return null;
        } else {
            float score = 1.0F;
            if (isbnWordDistance >= 10L) {
                score /= 3.0F;
            }

            if (this.reader.getPosition() > 5000L) {
                score /= 2.0F;
            }

            if (firstNumber.length() == 10) {
                score /= 2.0F;
                if (ISBNVerifier.isIsbn10(firstNumber)) {
                    this.reader.unmark();
                    return new ISBN10(firstNumber, score);
                }
            }

            if (firstNumber.length() > 5) {
                this.reader.unreadToMark();
                return null;
            } else {
                int separator = this.reader.read();
                if (!this.isValidSeparator(separator)) {
                    this.reader.unreadToMark();
                    return null;
                } else {
                    String publisherNumber = this.readNumber();
                    if (publisherNumber != null && publisherNumber.length() <= 7) {
                        if (this.reader.read() != separator) {
                            this.reader.unreadToMark();
                            return null;
                        } else {
                            String titleNumber = this.readNumber();
                            if (titleNumber != null && titleNumber.length() <= 6) {
                                if (this.reader.read() != separator) {
                                    this.reader.unreadToMark();
                                    return null;
                                } else {
                                    char checkDigit = (char)this.reader.read();
                                    if (!Character.isDigit(checkDigit) && checkDigit != 'X') {
                                        this.reader.unreadToMark();
                                        return null;
                                    } else {
                                        StringBuffer isbn = new StringBuffer();
                                        isbn.append(firstNumber).append('-').append(publisherNumber).append('-').append(titleNumber).append('-').append(checkDigit);
                                        if (!ISBNVerifier.isIsbn10(isbn.toString())) {
                                            this.reader.unreadToMark();
                                            return null;
                                        } else {
                                            this.reader.unmark();
                                            return new ISBN10(isbn.toString(), score);
                                        }
                                    }
                                }
                            } else {
                                this.reader.unreadToMark();
                                return null;
                            }
                        }
                    } else {
                        this.reader.unreadToMark();
                        return null;
                    }
                }
            }
        }
    }

    private String readNumber() throws IOException {
        this.reader.mark();
        StringBuffer number = new StringBuffer();

        while(true) {
            this.reader.mark();
            int ch = this.reader.read();
            if (!Character.isDigit(ch)) {
                this.reader.unreadToMark();
                if (number.length() == 0) {
                    this.reader.unreadToMark();
                    return null;
                } else {
                    this.reader.unmark();
                    return number.toString();
                }
            }

            number.append((char)ch);
            this.reader.unmark();
        }
    }
}
