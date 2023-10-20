package com.shnok.export.parser;

import acmi.l2.clientmod.io.ObjectInput;
import acmi.l2.clientmod.io.ObjectInputStream;
import acmi.l2.clientmod.io.UnrealPackage;
import acmi.l2.clientmod.unreal.UnrealRuntimeContext;
import acmi.l2.clientmod.unreal.UnrealSerializerFactory;
import com.shnok.export.Main;
import com.shnok.export.model.Poly;
import com.shnok.export.model.PolyData;
import com.shnok.export.model.Vector3;
import com.shnok.export.utils.HashUtils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class PolyParser extends DataParser {
    public static Poly parse(UnrealPackage up, UnrealPackage.ExportEntry entry, UnrealSerializerFactory serializerFactory) {
        ObjectInput<UnrealRuntimeContext> input = new ObjectInputStream<UnrealRuntimeContext>(
                new ByteArrayInputStream(entry.getObjectRawDataExternally()),
                entry.getUnrealPackage().getFile().getCharset(),
                entry.getOffset(), serializerFactory,
                new UnrealRuntimeContext(entry, serializerFactory)
        );


        System.out.println(HashUtils.byteArrayToHexString(entry.getObjectRawDataExternally()));

        Poly poly = new Poly();
        List<PolyData> polys = new ArrayList<>();

        poly.setName(entry.getObjectFullName());

        readByte(input);
        readByte(input);
        readByte(input);
        readByte(input);
        readByte(input);
        poly.setPolyCount(readByte(input));
        readByte(input);
        readByte(input);
        readByte(input);

        for(int p = 0 ; p < poly.getPolyCount(); p++) {
            PolyData polyData = new PolyData();
            polyData.setPolyIndex(p);

            System.out.println("=======> Poly Number " + (p+1));
            int vertexCount;
            while((vertexCount = readByte(input)) >= 255) {
                System.out.println("Skipped byte");
            }
            polyData.setVertexCount(vertexCount);

            System.out.println("Poly contains " + vertexCount + " vertex");
            polyData.setOrigin(readVector3(input));
            polyData.setNormal(readVector3(input));
            polyData.setTextureU(readVector3(input));
            polyData.setTextureV(readVector3(input));

            // Vertices
            List<Vector3> vertices = new ArrayList<>();
            for (int i = 0; i < vertexCount; i++) {
                vertices.add(readVector3(input));
            }

            polyData.setVertices(vertices);

            polyData.setPolyFlags(parsePolyFlags(readInt(input)));

            readByte(input);

            int textureRef = readObject(input);
            polyData.setTexture(up.objectReference(textureRef).getObjectFullName());

            readByte(input);
            readByte(input);
            readByte(input);
            readByte(input);
            readByte(input);
            readByte(input);
            readInt(input);
            readByte(input);
            polys.add(polyData);
        }

        poly.setPolyData(polys);

        System.out.println(poly);
        return poly;
    }
}
