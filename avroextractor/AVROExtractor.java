package avroextractor;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Anil Chowdhury
 *         Created on 11/14/2018.
 */
public class AVROExtractor {

    private static final Set<Type> PRIMITIVES = new HashSet<>();

    static {
        PRIMITIVES.add(Type.STRING);
        PRIMITIVES.add(Type.BYTES);
        PRIMITIVES.add(Type.INT);
        PRIMITIVES.add(Type.LONG);
        PRIMITIVES.add(Type.FLOAT);
        PRIMITIVES.add(Type.DOUBLE);
        PRIMITIVES.add(Type.BOOLEAN);
        PRIMITIVES.add(Type.NULL);
    }


    public static void main(String[] args) {
        AVROExtractor driver = new AVROExtractor();
//      driver.flatten("C:\\files\\avro\\twitter.avro");
//      driver.flatten("C:\\files\\avro\\detail.avro");
//      driver.flatten("C:\\files\\avro\\employee.avro");
//      driver.flatten("C:\\files\\avro\\sony.avro");
//      driver.flatten("C:\\files\\avro\\3k_manySmallMsgs.avro");
//      driver.flatten("C:\\files\\avro\\items.avro");
//      driver.flatten("C:\\files\\avro\\allTypesMix.avro");
//      driver.flatten("C:\\files\\avro\\MapForHiveOutput.avro");
//      driver.flatten("C:\\files\\avro\\rows_100_395col.avro");
    }

    private void flatten(String filePath) {
        try {
            DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
            DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(new File(filePath), datumReader);
            Schema schema = dataFileReader.getSchema();
            System.out.println("-------------------------" + new File(filePath).getName() + "-------------------------");
            System.out.println(schema.toString(true));

            List<String> flattenFields = flattenFields(schema);
            flattenFields.stream().forEach(System.out::println);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private List<String> flattenFields(Schema schema) {
        List<String> children = new ArrayList<>();

        if (isRecord(schema)) {
            String recordName = schema.getName();

            for (Schema.Field field : schema.getFields()) {
                if (isPrimitive(field.schema())) {
                    children.add("/" + recordName + "/" + field.name());
                } else {
                    List<String> flattenFields = flattenFields(field.schema());
                    if (flattenFields.isEmpty()) {
                        children.add("/" + recordName + "/" + field.name());
                    } else {
                        children.addAll(flattenFields.stream().
                                map(f -> "/" + recordName + "/" + field.name() + f).collect(Collectors.toList()));
                    }
                }
            }
        } else if (isMap(schema)) {
            children.addAll(flattenFields(schema.getValueType()));
        } else if (isArray(schema)) {
            children.addAll(flattenFields(schema.getElementType()));
        } else if (isUnion(schema)) {
            for (Schema unionSchema : schema.getTypes()) {
                children.addAll(flattenFields(unionSchema));
            }
        }
        return children;
    }

    private boolean isPrimitive(Schema schema) {
        return PRIMITIVES.contains(schema.getType());
    }

    private boolean isRecord(Schema schema) {
        return schema.getType() == Type.RECORD;
    }

    private boolean isMap(Schema schema) {
        return schema.getType() == Type.MAP;
    }

    private boolean isArray(Schema schema) {
        return schema.getType() == Type.ARRAY;
    }

    private boolean isUnion(Schema schema) {
        return schema.getType() == Type.UNION;
    }
}
