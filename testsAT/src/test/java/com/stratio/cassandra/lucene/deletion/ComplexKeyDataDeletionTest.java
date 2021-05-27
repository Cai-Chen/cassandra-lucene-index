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
package com.stratio.cassandra.lucene.deletion;

import static com.stratio.cassandra.lucene.builder.Builder.wildcard;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data1;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data10;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data11;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data12;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data13;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data14;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data15;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data16;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data17;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data18;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data19;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data2;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data20;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data3;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data4;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data5;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data6;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data7;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data8;
import static com.stratio.cassandra.lucene.deletion.DataHelper.data9;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import com.datastax.driver.core.Row;
import com.stratio.cassandra.lucene.BaseTest;
import com.stratio.cassandra.lucene.util.CassandraUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ComplexKeyDataDeletionTest extends BaseTest {

    private CassandraUtils utils;

    @BeforeEach
    public void before() {
        utils = CassandraUtils.builder("complex_key_data_deletion")
            .withPartitionKey("integer_1", "ascii_1")
            .withClusteringKey("double_1")
            .withColumn("ascii_1", "ascii")
            .withColumn("bigint_1", "bigint")
            .withColumn("blob_1", "blob")
            .withColumn("boolean_1", "boolean")
            .withColumn("decimal_1", "decimal")
            .withColumn("date_1", "timestamp")
            .withColumn("double_1", "double")
            .withColumn("float_1", "float")
            .withColumn("integer_1", "int")
            .withColumn("inet_1", "inet")
            .withColumn("text_1", "text")
            .withColumn("varchar_1", "varchar")
            .withColumn("uuid_1", "uuid")
            .withColumn("timeuuid_1", "timeuuid")
            .withColumn("list_1", "list<text>")
            .withColumn("set_1", "set<text>")
            .withColumn("map_1", "map<text,text>")
            .build()
            .createKeyspace()
            .createTable()
            .createIndex()
            .insert(data1,
                    data2,
                    data3,
                    data4,
                    data5,
                    data6,
                    data7,
                    data8,
                    data9,
                    data10,
                    data11,
                    data12,
                    data13,
                    data14,
                    data15,
                    data16,
                    data17,
                    data18,
                    data19,
                    data20);
    }

    @AfterEach
    public void after() {
        CassandraUtils.dropKeyspaceIfNotNull(utils);
    }

    @Test
    public void columnDeletionTest() {
        List<Row> rows = utils.delete("bigint_1")
            .where("integer_1", 1)
            .and("ascii_1", "ascii")
            .and("double_1", 1)
            .refresh()
            .filter(wildcard("ascii_1", "*"))
            .get();

        assertEquals("Expected 20 results!", 20, rows.size());

        int integerValue;
        String asciiValue;
        double doubleValue;
        for (Row row : rows) {
            integerValue = row.getInt("integer_1");
            asciiValue = row.getString("ascii_1");
            doubleValue = row.getDouble("double_1");
            if ((integerValue == 1) && (asciiValue.equals("ascii")) && (doubleValue == 1)) {
                assertTrue("Must be null!", row.isNull("bigint_1"));
            }
        }
    }

    @Test
    public void testMapElementDeletion() {
        List<Row> rows = utils.delete("map_1['k1']")
            .where("integer_1", 1)
            .and("ascii_1", "ascii")
            .and("double_1", 1)
            .refresh()
            .filter(wildcard("ascii_1", "*"))
            .get();

        assertEquals("Expected 20 results!", 20, rows.size());

        int integerValue;
        String asciiValue;
        double doubleValue;
        Map<String, String> mapValue = null;
        for (Row row : rows) {
            integerValue = row.getInt("integer_1");
            asciiValue = row.getString("ascii_1");
            doubleValue = row.getDouble("double_1");
            if ((integerValue == 1) && (asciiValue.equals("ascii")) && (doubleValue == 1)) {
                mapValue = row.getMap("map_1", String.class, String.class);
            }
        }

        assertNotNull("Must not be null!", mapValue);
        assertNull("Must be null!", mapValue.get("k1"));
    }

    @Test
    public void testListElementDeletion() {
        List<Row> rows = utils.delete("list_1[0]")
            .where("integer_1", 1)
            .and("ascii_1", "ascii")
            .and("double_1", 1)
            .refresh()
            .filter(wildcard("ascii_1", "*"))
            .get();

        assertEquals("Expected 20 results!", 20, rows.size());

        int integerValue;
        String asciiValue;
        double doubleValue;
        List<String> listValue = null;
        for (Row row : rows) {
            integerValue = row.getInt("integer_1");
            asciiValue = row.getString("ascii_1");
            doubleValue = row.getDouble("double_1");
            if ((integerValue == 1) && (asciiValue.equals("ascii")) && (doubleValue == 1)) {
                listValue = row.getList("list_1", String.class);
            }
        }

        assertNotNull("Must not be null!", listValue);
        assertEquals("Length unexpected", 1, listValue.size());
    }

    @Test
    public void testTotalPartitionDeletion() {
        utils.delete()
            .where("integer_1", 1)
            .where("ascii_1", "ascii")
            .where("double_1", 1)
            .refresh()
            .filter(wildcard("ascii_1", "*"))
            .check(19);
    }

    @Test
    public void testPartialPartitionDeletion() {
        utils.delete()
            .where("integer_1", 1)
            .and("ascii_1", "ascii")
            .refresh()
            .filter(wildcard("ascii_1", "*"))
            .check(18);
    }
}
