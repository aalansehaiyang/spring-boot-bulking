package org.common.fastdfs.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

public class CustomSequenceInputStream extends InputStream {
    Enumeration<? extends String> e;
    InputStream in;

    public CustomSequenceInputStream(Enumeration<? extends String> var1) {
        this.e = var1;

        try {
            this.nextStream();
        } catch (IOException var3) {
            throw new Error("panic");
        }
    }

    final void nextStream() throws IOException {
        if (this.in != null) {
            this.in.close();
        }

        if (this.e.hasMoreElements()) {
            this.in = getUrlInputStream(this.e.nextElement());
            if (this.in == null) {
                throw new NullPointerException();
            }
        } else {
            this.in = null;
        }

    }

    private InputStream getUrlInputStream(String url) throws IOException {
        return new URL(url).openStream();
    }

    public int available() throws IOException {
        return this.in == null ? 0 : this.in.available();
    }

    public int read() throws IOException {
        while (in != null) {
            int c = in.read();
            if (c != -1) {
                return c;
            }
            nextStream();
        }
        return -1;
    }

    public int read(byte b[], int off, int len) throws IOException {
        if (in == null) {
            return -1;
        } else if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }
        do {
            int n = in.read(b, off, len);
            if (n > 0) {
                return n;
            }
            nextStream();
        } while (in != null);
        return -1;
    }

    public void close() throws IOException {
        do {
            this.nextStream();
        } while (this.in != null);

    }
}