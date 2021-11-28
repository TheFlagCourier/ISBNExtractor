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

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.Stack;

public class PushbackMarkReader {
    private PushbackReader reader;
    private Stack<StringBuffer> unreadBuffers = new Stack();
    private long position = 0L;

    public PushbackMarkReader(Reader reader, int size) {
        this.reader = new PushbackReader(reader, size);
    }

    public void close() throws IOException {
        this.reader.close();
    }

    public void mark() {
        this.unreadBuffers.push(new StringBuffer());
    }

    public void unreadToMark() throws IOException {
        if (this.unreadBuffers.size() == 0) {
            throw new IOException("No mark to unread to");
        } else {
            String toUnread = ((StringBuffer)this.unreadBuffers.pop()).toString();
            this.reader.unread(toUnread.toCharArray());
            this.position -= (long)toUnread.length();
        }
    }

    public void unmark() throws IOException {
        if (this.unreadBuffers.size() == 0) {
            throw new IOException("No mark to unmark");
        } else {
            StringBuffer sb = (StringBuffer)this.unreadBuffers.pop();
            if (this.unreadBuffers.size() > 0) {
                ((StringBuffer)this.unreadBuffers.peek()).append(sb.toString());
            }

        }
    }

    public int read() throws IOException {
        int c = this.reader.read();
        ++this.position;
        if (this.unreadBuffers.size() > 0 && c != -1) {
            ((StringBuffer)this.unreadBuffers.peek()).append((char)c);
        }

        return c;
    }

    public long getPosition() {
        return this.position;
    }
}
