/*
 * Copyright (C) 2014 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.cassandra.lucene.schema.mapping;

import org.apache.cassandra.db.marshal.*;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.util.BytesRef;

/**
 * A {@link Mapper} to map a string, tokenized field.
 *
 * @author Andres de la Pena {@literal <adelapena@stratio.com>}
 */
public class TextMapper extends SingleColumnMapper<String> {

    /**
     * Builds a new {@link TextMapper} using the specified Lucene {@link org.apache.lucene.analysis.Analyzer}.
     *
     * @param field    The name of the field.
     * @param column   The name of the column to be mapped.
     * @param indexed  If the field supports searching.
     * @param sorted   If the field supports sorting.
     * @param analyzer The name of the Lucene {@link org.apache.lucene.analysis.Analyzer} to be used.
     */
    public TextMapper(String field, String column, Boolean indexed, Boolean sorted, String analyzer) {
        super(field,
              column,
              indexed,
              sorted,
              analyzer,
              String.class,
              AsciiType.instance,
              UTF8Type.instance,
              Int32Type.instance,
              LongType.instance,
              IntegerType.instance,
              FloatType.instance,
              DoubleType.instance,
              BooleanType.instance,
              UUIDType.instance,
              TimeUUIDType.instance,
              TimestampType.instance,
              BytesType.instance,
              InetAddressType.instance);
    }

    /** {@inheritDoc} */
    @Override
    protected String doBase(String name, Object value) {
        return value.toString();
    }

    /** {@inheritDoc} */
    @Override
    public Field indexedField(String name, String value) {
        return new TextField(name, value, STORE);
    }

    /** {@inheritDoc} */
    @Override
    public Field sortedField(String name, String value) {
        BytesRef bytes = new BytesRef(value);
        return new SortedDocValuesField(name, bytes);
    }

    /** {@inheritDoc} */
    @Override
    public SortField sortField(String name, boolean reverse) {
        return new SortField(name, Type.STRING, reverse);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return toStringHelper(this).add("analyzer", analyzer).toString();
    }
}
