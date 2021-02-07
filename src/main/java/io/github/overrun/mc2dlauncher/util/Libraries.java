/*
 * MIT License
 *
 * Copyright (c) 2021 Over-Run
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.overrun.mc2dlauncher.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author squid233
 * @since 2021/02/06
 */
public final class Libraries {
    private String[] libs;

    public Libraries(String[] libs) {
        this.libs = libs;
    }

    public String[] getLibs() {
        return libs;
    }

    public static final class Serializer extends TypeAdapter<Libraries> {
        @Override
        public void write(JsonWriter out, Libraries value) throws IOException {
            out.beginObject().name("libs").beginArray();
            for (String lib : value.getLibs()) {
                out.value(lib);
            }
            out.endArray().endObject();
        }

        @Override
        public Libraries read(JsonReader in) throws IOException {
            List<String> list = new ArrayList<>();
            in.beginObject();
            in.nextName();
            in.beginArray();
            while (in.hasNext()) {
                list.add(in.nextString());
            }
            in.endArray();
            in.endObject();
            return new Libraries(list.toArray(new String[0]));
        }
    }
}
