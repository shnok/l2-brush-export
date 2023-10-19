package com.shnok.export;

import acmi.l2.clientmod.crypt.L2Crypt;
import acmi.l2.clientmod.io.*;
import acmi.l2.clientmod.io.ObjectInput;
import acmi.l2.clientmod.io.ObjectInputStream;
import acmi.l2.clientmod.unreal.Environment;
import acmi.l2.clientmod.unreal.UnrealRuntimeContext;
import acmi.l2.clientmod.unreal.UnrealSerializerFactory;
import acmi.l2.clientmod.unreal.core.*;
import acmi.l2.clientmod.unreal.core.Object;
import acmi.l2.clientmod.unreal.properties.*;
import com.shnok.export.utils.HashUtils;

import java.io.*;
import java.lang.Class;
import java.util.*;

public class Main {
    static Environment environment;
    static UnrealSerializerFactory serializerFactory;
    public static void main(String[] args) throws IOException {
        String l2Folder = "D:\\Games\\Lineage II";
        environment = Environment.fromIni(new File(l2Folder + "\\system", "l2.ini"));
        serializerFactory = new UnrealSerializerFactory(environment);

        UnrealPackage up = new UnrealPackage(new File(l2Folder + "\\maps", "17_25_Classic.unr"), true);



        UnrealPackage.ExportEntry entry = getEntry(up, "Model54");
        /*readModel(entry);
        entry = getEntry(up, "Model138");
        readModel(entry);
        entry = getEntry(up, "Model59");
        readModel(entry);*/

        /*entry = getEntry(up, "Polys933");
        readPoly(entry);
        entry = getEntry(up, "Polys12");
        readPoly(entry);
        entry = getEntry(up, "Polys44");
        readPoly(entry);*/
        /*entry = getEntry(up, "Polys16");
        readPoly(entry);
        entry = getEntry(up, "Polys953");
        readPoly(entry);*/

        //UnrealPackage.Entry e = up.objectReference(10827);
        //GetEntryAtOffset(up, 4194568);

               /*List<L2Property> properties = getProperties(up, "Brush213");
        for (L2Property property : properties) {
            printType(up, property);
        }*/
        System.out.printf("Done");

        BrushExporter exporter = new BrushExporter(serializerFactory);
        exporter.processAllBrushes(up);
    }



    static UnrealPackage.ExportEntry getEntry(UnrealPackage up, String entryName) {
        List<UnrealPackage.ExportEntry> exportEntries = up.getExportTable();
        for (int i = 0; i < exportEntries.size(); i++) {
            if (exportEntries.get(i).getObjectInnerFullName().equals(entryName)) {
                System.out.println("========================> " + exportEntries.get(i).getObjectFullName() + " - " + exportEntries.get(i).getObjectClass().getFullClassName());
                UnrealPackage.ExportEntry entry = (UnrealPackage.ExportEntry) up.getAt(i + 1);
                return entry;
            }
        }

        return null;
    }

    static UnrealPackage.ExportEntry GetEntryAtOffset(UnrealPackage up, int offset) {
        List<UnrealPackage.ExportEntry> exportEntries = up.getExportTable();
        for (int i = 0; i < exportEntries.size(); i++) {
            if (Math.abs(exportEntries.get(i).getOffset() - offset) <= 100) {
                System.out.println("========================> BINGO " + exportEntries.get(i).getObjectFullName() + " - " + exportEntries.get(i).getObjectClass().getFullClassName());
                UnrealPackage.ExportEntry entry = (UnrealPackage.ExportEntry) up.getAt(i + 1);
                return entry;
            }
        }

        return null;
    }

    static List<L2Property> getProperties(UnrealPackage up, String entryName) {
        List<UnrealPackage.ExportEntry> exportEntries = up.getExportTable();

        System.out.println("Total entries: " + exportEntries.size());
        for (int i = 0; i < exportEntries.size(); i++) {
            if (exportEntries.get(i).getObjectInnerFullName().equals(entryName)) {
                System.out.println("========================> " + exportEntries.get(i).getObjectFullName() + " - " + exportEntries.get(i).getObjectClass().getFullClassName());
                try {
                    UnrealPackage.ExportEntry entry = (UnrealPackage.ExportEntry) up.getAt(i + 1);
                    Object object = serializerFactory.getOrCreateObject(entry);
                    List<L2Property> properties = object.properties;
                    System.out.println("=====> Properties:" + properties.size());
                    return properties;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        System.out.printf("Done");
        return null;
    }

    static void printType(UnrealPackage up, L2Property property) {
        Property template = property.getTemplate();

        for (int i = 0; i < template.arrayDimension; i++) {
            java.lang.Object obj = property.getAt(i);

            if (template instanceof ByteProperty ||
                    template instanceof ObjectProperty ||
                    template instanceof NameProperty ||
                    template instanceof ArrayProperty) {
                assert obj != null;
            }

            if (template instanceof StructProperty) {
                if (obj == null || ((List) obj).isEmpty())
                    continue;
            }

            if (template instanceof ByteProperty) {
               // System.out.println(property.getName() + " is of type ByteProperty");
            } else if (template instanceof IntProperty) {
                //System.out.println(property.getName() + " is of type IntProperty");
            } else if (template instanceof BoolProperty) {
                //System.out.println(property.getName() + " is of type BoolProperty");
            } else if (template instanceof FloatProperty) {
                // System.out.println(property.getName() + " is of type FloatProperty");
            } else if (template instanceof ObjectProperty) {
                // System.out.println(property.getName() + " is of type ObjectProperty");
                UnrealPackage.Entry entry = up.objectReference((Integer) obj);

                if(entry != null) {
                    System.out.println(entry.getObjectInnerFullName() + " found at index " + (Integer) obj);
                }
                if (entry == null) {
                    // System.out.println(entry.getObjectInnerFullName() + " is null");
                } else if (entry instanceof UnrealPackage.ImportEntry) {
                    System.out.println(entry.getObjectInnerFullName() + " is ImportEntry");
                } else if (entry instanceof UnrealPackage.ExportEntry) {

                    Object object = serializerFactory.getOrCreateObject(entry);
                    List<L2Property> properties = object.properties;

                    if(object.unreadBytes.length > 0) {
                        if(object.getFullName().contains("Model")) {
                            //readModel((UnrealPackage.ExportEntry) entry);
                            //;
                            System.out.println(object.getFullName());
                        }
                    }
                } else {
                    throw new IllegalStateException("wtf");
                }
            } else if (template instanceof NameProperty) {
                //System.out.println(property.getName() + " is of type NameProperty");
            } else if (template instanceof ArrayProperty) {
               // System.out.println(property.getName() + " is of type ArrayProperty");
            } else if (template instanceof StructProperty) {
                //System.out.println(property.getName() + " is of type StructProperty");
            } else if (template instanceof StrProperty) {
               // System.out.println(property.getName() + " is of type StrProperty");
            } else {
              //  System.out.println(property.getName() + " is of an unknown type");
            }
        }

    }

    /*static void readModel(UnrealPackage.ExportEntry entry) {
        ObjectInput<UnrealRuntimeContext> input = new ObjectInputStream<UnrealRuntimeContext>(
                new ByteArrayInputStream(entry.getObjectRawDataExternally()),
                entry.getUnrealPackage().getFile().getCharset(),
                entry.getOffset(), serializerFactory,
                new UnrealRuntimeContext(entry, serializerFactory)
        );

        System.out.println(byteArrayToHexString(entry.getObjectRawDataExternally()));

        // Byte
        readByte(input);

        readVector3(input);
        readVector3(input);

        readByte(input);

        readVector3(input);
        readFloat(input);

        readInt(input);

        readByte(input);

        readByte(input);

        readInt(input);
        readByte(input);
        readByte(input);
        readByte(input);

        //POLYS ADDRESS!!!!!!!!!!!!!!!!!!!
        int polyIndex = readObject(input);
        System.out.println("Poly index: " + polyIndex);

        readByte(input);
        readByte(input);
        readByte(input);
        readByte(input);
        readByte(input);
        readByte(input);
        readByte(input);
        readByte(input);
        readByte(input);
        readByte(input);
        readByte(input);
        readByte(input);
        readByte(input);
    }*/
   /* static void readPoly(UnrealPackage.ExportEntry entry) {
        ObjectInput<UnrealRuntimeContext> input = new ObjectInputStream<UnrealRuntimeContext>(
                new ByteArrayInputStream(entry.getObjectRawDataExternally()),
                entry.getUnrealPackage().getFile().getCharset(),
                entry.getOffset(), serializerFactory,
                new UnrealRuntimeContext(entry, serializerFactory)
        );

        System.out.println(byteArrayToHexString(entry.getObjectRawDataExternally()));

        readByte(input);

        int polyCount = readByte(input);
        System.out.println("Data contains " + polyCount + " polys");
        readByte(input);
        readByte(input);
        readByte(input);
        int polyCount2 = readByte(input);
        System.out.println("Data contains " + polyCount2 + " polys");
        readByte(input);
        readByte(input);
        readByte(input);

        for(int p = 0 ; p < polyCount; p++) {
            if(p >= 1) {
                return;
            }
            System.out.println("=======> Poly Number " + (p+1));
            int vertexCount;
            while((vertexCount = readByte(input)) >= 255) {
                System.out.println("Skipped byte");
            }

            System.out.println("Poly contains " + vertexCount + " vertex");
            readVector3(input);
            readVector3(input);
            readVector3(input);
            readVector3(input);

            for (int i = 0; i < vertexCount; i++) {
                System.out.println("Vertex " + (i + 1) + ": ");
                readVector3(input);
            }

            readByte(input);

            System.out.println(input.readInt());




            readByte(input);


            int polyIndex = readByte(input);
            System.out.println("Poly index: " + polyIndex);

            readByte(input);
            readByte(input);
            readByte(input);
            readByte(input);
            readByte(input);
            readByte(input);
            readByte(input);
            readByte(input);
            readByte(input);
        }
    }*/

    /*static float readFloat( ObjectInput<UnrealRuntimeContext> input) {
        // Float
        float f = (float) PropertiesUtil.read(input, PropertiesUtil.Type.FLOAT, false, null, null);
        System.out.println("Float " + f);
        return f;
    }

    static String readString( ObjectInput<UnrealRuntimeContext> input) {
        // String
        String s = (String) PropertiesUtil.read(input, PropertiesUtil.Type.STR, false, null, null);
        System.out.println("String " + s);
        return s;
    }

    static int readName( ObjectInput<UnrealRuntimeContext> input) {
        // String
        int s = (int) PropertiesUtil.read(input, PropertiesUtil.Type.NAME, false, null, null);
        System.out.println("Name " + s);
        return s;
    }

    static int readObject( ObjectInput<UnrealRuntimeContext> input) {
        // String
        int s = (int) PropertiesUtil.read(input, PropertiesUtil.Type.OBJECT, false, null, null);
        System.out.println("Name " + s);
        return s;
    }

    static int readInt( ObjectInput<UnrealRuntimeContext> input) {
        // Byte
        int i = (int) PropertiesUtil.read(input, PropertiesUtil.Type.INT, false, null, null);
        System.out.println("Int " + i);
        return i;
    }

    static int readByte( ObjectInput<UnrealRuntimeContext> input) {
        // Byte
        int b = (int) PropertiesUtil.read(input, PropertiesUtil.Type.BYTE, false, null, null);
        System.out.println("Byte " + b);
        return b;
    }

    static void readVector3( ObjectInput<UnrealRuntimeContext> input) {
        // Float
        float x = (float) PropertiesUtil.read(input, PropertiesUtil.Type.FLOAT, false, null, null);
        float y = (float) PropertiesUtil.read(input, PropertiesUtil.Type.FLOAT, false, null, null);
        float z = (float) PropertiesUtil.read(input, PropertiesUtil.Type.FLOAT, false, null, null);
        System.out.println("Vector3 (" + x + "," + y + "," + z + ")");
    }*/

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    static void printClass(String folder, String fileName, String entryName) {
        try (UnrealPackage up = new UnrealPackage(new File(folder + "\\", fileName), true)) {
            TextBuffer textBuffer = up.getExportTable()
                    .parallelStream()
                    .filter(e -> e.getObjectInnerFullName().equals(entryName))
                    .findAny()
                    .map(serializerFactory::getOrCreateObject)
                    .map(o -> (TextBuffer) o)
                    .orElseThrow(() -> new IllegalArgumentException(entryName + " not found"));
            System.out.println(textBuffer.text);
           /* String textBuffer = up.getExportTable()
                    .parallelStream()
                    .filter(e -> e.getObjectInnerFullName().equals(entryName))
                    .findAny()
                    .map(serializerFactory::getOrCreateObject)
                    .map(o -> o.toString())
                    .orElseThrow(() -> new IllegalArgumentException(entryName + " not found"));*/
            //System.out.println(textBuffer);
        }
    }

    static void readEntry(String folder, String filename, String entryName) {

        UnrealPackage up = new UnrealPackage(new File(folder, filename), true);
        List<UnrealPackage.ExportEntry> exportEntries = up.getExportTable();
        for (int i = 0; i < exportEntries.size(); i++) {
            //if (exportEntries.get(i).getObjectFullName().contains("Model")) {
            System.out.println(exportEntries.get(i).getObjectInnerFullName());
            if (exportEntries.get(i).getObjectInnerFullName().equals(entryName)) {
                System.out.println("========================> " + exportEntries.get(i).getObjectFullName() + " - " + exportEntries.get(i).getObjectClass().getFullClassName());
                try {
                    UnrealPackage.ExportEntry entry = (UnrealPackage.ExportEntry) up.getAt(i + 1);
                    loadRAW(entry);
                    byte[] unreadBytes = load(serializerFactory, entry);

                    Object object = serializerFactory.getOrCreateObject(entry);
                    List<L2Property> properties = object.properties;
                    System.out.println("=====> Properties:" + properties.size());
                    for (L2Property property : properties) {
                        System.out.println("=====> " + property.toString());
                        for (int j = 0; j < property.getSize(); j++) {
                            System.out.println(property.getAt(j).toString());
                        }
                    }
                    if(unreadBytes.length > 0) {
                        System.out.println("Unread bytes:" + unreadBytes.length);
                        System.out.println(HashUtils.byteArrayToHexString(unreadBytes));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        System.out.printf("Done");
    }

    static void decryptFile(String cryptedFile, String destFile) throws IOException {
        try(InputStream is = L2Crypt.getInputStream(new File(cryptedFile));
            OutputStream os = new FileOutputStream(destFile)){
            byte[] buffer = new byte[0x1000];
            int r;
            while ((r = is.read(buffer)) != -1){
                os.write(buffer, 0, r);
            }
        }
    }
    /*public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }*/

    private static byte[] loadRAW(UnrealPackage.ExportEntry entry) {
        byte[] data = entry.getObjectRawData();
        System.out.println(data.length);
        System.out.println(HashUtils.byteArrayToHexString(data));
        return data;
    }

    private static byte[] load(UnrealSerializerFactory unrealSerializerFactory, UnrealPackage.ExportEntry entry) {
        ObjectInput<UnrealRuntimeContext> input = new ObjectInputStream<UnrealRuntimeContext>(new ByteArrayInputStream(entry.getObjectRawDataExternally()), entry.getUnrealPackage().getFile().getCharset(), entry.getOffset(), unrealSerializerFactory, new UnrealRuntimeContext(entry, unrealSerializerFactory)) {
            public java.lang.Object readObject(Class clazz) throws UncheckedIOException {
                if (this.getSerializerFactory() == null) {
                    throw new IllegalStateException("SerializerFactory is null");
                } else {
                    Serializer serializer = getSerializerFactory().forClass(clazz);
                    java.lang.Object obj = serializer.instantiate((ObjectInput) unrealSerializerFactory);
                    if (!(obj instanceof Object)) {
                        throw new RuntimeException("USE input.getSerializerFactory().forClass(class).readObject(object, input);");
                    } else {
                        return obj;
                    }
                }
            }
        };


        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            while(true) {
                baos.write(input.readUnsignedByte());
            }
        } catch (UncheckedIOException var10) {
            return baos.toByteArray();
        }
    }

    /*static void checkPolyFlags(int value) {
        System.out.println("Polyflags:");

        if(value == 0) {
            System.out.println("Default");
            return;
        }

        Map<String, Integer> flags = new HashMap<>();
        flags.put("PF_Invisible", 1);
        flags.put("PF_Masked", 2);
        flags.put("PF_Translucent", 4);
        flags.put("PF_NotSolid", 8);
        flags.put("PF_Environment", 16);
        flags.put("PF_ForceViewZone", 16);
        flags.put("PF_Semisolid", 32);
        flags.put("PF_Modulated", 64);
        flags.put("PF_FakeBackdrop", 128);
        flags.put("PF_TwoSided", 256);
        flags.put("PF_AutoUPan", 512);
        flags.put("PF_AutoVPan", 1024);
        flags.put("PF_NoSmooth", 2048);
        flags.put("PF_BigWavy", 4096);
        flags.put("PF_SpecialPoly", 4096);
        flags.put("PF_SmallWavy", 8192);
        flags.put("PF_Flat", 16384);
        flags.put("PF_LowShadowDetail", 32768);
        flags.put("PF_NoMerge", 65536);
        flags.put("PF_CloudWavy", 131072);
        flags.put("PF_DirtyShadows", 262144);
        flags.put("PF_BrightCorners", 524288);
        flags.put("PF_SpecialLit", 1048576);
        flags.put("PF_Gouraud", 2097152);
        flags.put("PF_NoBoundRejection", 2097152);
        flags.put("PF_Unlit", 4194304);
        flags.put("PF_HighShadowDetail", 8388608);
        flags.put("PF_Memorized", 16777216);
        flags.put("PF_RenderHint", 16777216);
        flags.put("PF_Selected", 33554432);
        flags.put("PF_Portal", 67108864);
        flags.put("PF_Mirrored", 134217728);
        flags.put("PF_Highlighted", 268435456);
        flags.put("unused", 536870912);
        flags.put("PF_FlatShaded", 1073741824);
        flags.put("PF_EdProcessed", 1073741824);
        flags.put("PF_RenderFog", 1073741824);
            //flags.put("PF_EdCut", 2147483648);
            //flags.put("PF_Occlude", 2147483648);

        List<String> setFlags = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : flags.entrySet()) {
            if ((value & entry.getValue()) != 0) {
                setFlags.add(entry.getKey());
            }
        }

        for (String flag : setFlags) {
            System.out.println(flag);
        }
    }*/

}



