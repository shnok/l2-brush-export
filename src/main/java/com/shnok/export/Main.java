package com.shnok.export;

import acmi.l2.clientmod.io.*;
import acmi.l2.clientmod.unreal.Environment;
import acmi.l2.clientmod.unreal.UnrealSerializerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shnok.export.model.Brush;
import com.shnok.export.model.DataContainer;
import com.shnok.export.parser.DataParser;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String l2Folder = "D:\\Games\\Lineage II";
        String mapName = "16_24_Classic";
        String brushName = ""; //46 53

        Environment environment = Environment.fromIni(new File(l2Folder + "\\system", "l2.ini"));
        UnrealSerializerFactory serializerFactory = new UnrealSerializerFactory(environment);

        UnrealPackage up = new UnrealPackage(new File(l2Folder + "\\maps", mapName + ".unr"), true);

        ObjectMapper objectMapper = new ObjectMapper();
        String json;

        List<Brush> brushes = new ArrayList<>();
        if(brushName.isEmpty()) {
            brushes = BrushExporter.processAllBrushes(up, serializerFactory);
        } else {
            brushes = BrushExporter.processBrushByName(up, serializerFactory, brushName);
        }

        DataContainer dataContainer = new DataContainer(brushes);
        json = objectMapper.writeValueAsString(dataContainer);

        FileWriter fw = new FileWriter(mapName + ".json", false);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write(json);
        writer.close();
    }
}



