package com.shnok.export.parser;

import acmi.l2.clientmod.io.ObjectInput;
import acmi.l2.clientmod.io.ObjectInputStream;
import acmi.l2.clientmod.io.UnrealPackage;
import acmi.l2.clientmod.unreal.UnrealRuntimeContext;
import acmi.l2.clientmod.unreal.UnrealSerializerFactory;
import com.shnok.export.model.Model;
import com.shnok.export.utils.HashUtils;

import java.io.ByteArrayInputStream;

public class ModelParser extends DataParser {
    public static Model parse(UnrealPackage.ExportEntry entry, UnrealSerializerFactory serializerFactory) {
        ObjectInput<UnrealRuntimeContext> input = new ObjectInputStream<UnrealRuntimeContext>(
                new ByteArrayInputStream(entry.getObjectRawDataExternally()),
                entry.getUnrealPackage().getFile().getCharset(),
                entry.getOffset(), serializerFactory,
                new UnrealRuntimeContext(entry, serializerFactory)
        );

        Model model = new Model();
        model.setName(entry.getObjectFullName());
        System.out.println(HashUtils.byteArrayToHexString(entry.getObjectRawDataExternally()));

        readByte(input);
        model.setVec1(readVector3(input));
        model.setVec2(readVector3(input));
        readByte(input);
        model.setVec3(readVector3(input));
        model.setF1(readFloat(input));
        readInt(input);
        readByte(input);
        readByte(input);
        readInt(input);
        readByte(input);
        readByte(input);
        readByte(input);
        int polyIndex = readObject(input);
        System.out.println("Poly index: " + polyIndex);
        model.setPolyIndex(polyIndex);
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

        System.out.println(model);
        return model;
    }
}
