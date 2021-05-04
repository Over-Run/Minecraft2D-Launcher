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
public final class VersionJson {
    private final String[] libs;
    private int schemaVersion;

    public VersionJson(String[] libs) {
        this.libs = libs;
    }

    public String[] getLibs() {
        return libs;
    }

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public static final class Serializer extends TypeAdapter<VersionJson> {
        @Override
        public void write(JsonWriter out, VersionJson value) throws IOException {
            out.beginObject()
                    .name("schemaVersion").value(1)
                    .name("libs").beginArray();
            for (String lib : value.getLibs()) {
                out.value(lib);
            }
            out.endArray().endObject();
        }

        @Override
        public VersionJson read(JsonReader in) throws IOException {
            List<String> list = new ArrayList<>();
            int schemaVersion = 0;
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "schemaVersion":
                        schemaVersion = in.nextInt();
                        break;
                    case "libs":
                        in.beginArray();
                        while (in.hasNext()) {
                            list.add(in.nextString());
                        }
                        in.endArray();
                    default:
                }
            }
            in.endObject();
            VersionJson json = new VersionJson(list.toArray(new String[0]));
            json.setSchemaVersion(schemaVersion);
            return json;
        }
    }
}
