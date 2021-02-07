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
import java.util.HashMap;
import java.util.Map;

/**
 * @author squid233
 * @since 2021/02/06
 */
public final class Mc2dVersion {
    private final Map<String, String> map = new HashMap<>();

    public Map<String, String> getMap() {
        return map;
    }

    public static final class Serializer extends TypeAdapter<Mc2dVersion> {
        @Override
        public void write(JsonWriter out, Mc2dVersion value) throws IOException {
            out.beginObject();
            for (Map.Entry<String, String> entry : value.getMap().entrySet()) {
                String key = entry.getKey();
                if (!"latest".equals(key) && !"stable".equals(key)) {
                    out.name(entry.getKey()).value(entry.getValue());
                }
            }
            out.endObject();
        }

        @Override
        public Mc2dVersion read(JsonReader in) throws IOException {
            Mc2dVersion mc2dVersion = new Mc2dVersion();
            in.beginObject();
            while (in.hasNext()) {
                String key = in.nextName();
                String value = in.nextString();
                if (!"latest".equals(key) && !"stable".equals(key)) {
                    mc2dVersion.getMap().put(key, value);
                }
            }
            in.endObject();
            return mc2dVersion;
        }
    }
}
